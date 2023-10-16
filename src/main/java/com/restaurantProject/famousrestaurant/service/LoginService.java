package com.restaurantProject.famousrestaurant.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.scribejava.core.model.OAuth2AccessToken;
import com.restaurantProject.famousrestaurant.dto.Member;
import com.restaurantProject.famousrestaurant.entity.MemberEntity;
import com.restaurantProject.famousrestaurant.repository.MemberRepository;
import com.restaurantProject.famousrestaurant.sms.dto.Message;
import com.restaurantProject.famousrestaurant.sms.dto.SmsResponse;
import com.restaurantProject.famousrestaurant.sms.dto.VerificationCode;
import com.restaurantProject.famousrestaurant.util.KakaoMapApi;
import com.restaurantProject.famousrestaurant.util.NaverLoginApi;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import org.springframework.web.client.RestClientException;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

@Service
@RequiredArgsConstructor
public class LoginService {

    private String apiResult = null;
    private final MemberRepository memberRepository;
    private final NaverLoginApi naverLoginApi;
    private final KakaoMapApi kakaoMapApi;
    private final RegisterMail registerMail;
    private final SmsService smsService;

    private final LinkedHashMap<String,VerificationCode> authKey = new LinkedHashMap<>();

    @Scheduled(cron = "*/10 * * * *")
    private void cleanExpiredCodes() {
        long currentTime = System.currentTimeMillis();
        long EXPIRATION_TIME = 3 * 60 * 1000;

        Iterator<Map.Entry<String, VerificationCode>> iterator = authKey.entrySet().iterator();

        while (iterator.hasNext()) {
            Map.Entry<String, VerificationCode> entry = iterator.next();
            VerificationCode verificationCode = entry.getValue();
            if (verificationCode.getTimestamp() + EXPIRATION_TIME <= currentTime) iterator.remove();
        }
    }

    private String createNumber(Message dto) {
        StringBuilder key = new StringBuilder();
        Random rnd = new Random();

        // 인증코드 6자리
        for (int i = 0; i < 6; i++) key.append((rnd.nextInt(10)));

        VerificationCode verificationCode = new VerificationCode(key.toString());
        authKey.put(dto.getTo(),verificationCode);
        return key.toString();
    }

    private MemberEntity update(MemberEntity u, String naverId, String password){
        return MemberEntity.builder()
                .id(u.getId())
                .memberId(u.getMemberId())
                .memberPass(password)
                .memberNaverId(naverId)
                .memberAddress(u.getMemberAddress())
                .memberPhoneNumber(u.getMemberPhoneNumber())
                .memberProfile(u.getMemberProfile())
                .mapX(u.getMapX())
                .mapY(u.getMapY())
                .build();
    }

    private void setNewPassword(Member dto, String pw) {
        Optional<MemberEntity> user = memberRepository.findByMemberId(dto.getMemberId());
        if (user.isPresent()) {
            StringBuilder hexString = sha256(pw);
            MemberEntity u = user.get();
            MemberEntity m = update(u,u.getMemberNaverId(),hexString.toString());
            memberRepository.save(m);
        }
    }

    private String getMemberIdByPhoneNumber(Member dto){
        Optional<MemberEntity> result = memberRepository.findByMemberPhoneNumber(dto.getMemberPhoneNumber());
        return result.map(MemberEntity::getMemberId).orElse(null);
    }

    private void insertMember(Member dto) {
        StringBuilder hexString = sha256(dto.getMemberPass());
        Optional<MemberEntity> result = memberRepository.findByMemberId(dto.getMemberId());

        if (result.isEmpty()) {
            String address = dto.getMemberAddress() + " " + dto.getMemberDetailAddr();
            String[] map = kakaoMapApi.map(dto.getMemberAddress());
            MemberEntity m = MemberEntity.builder()
                    .memberId(dto.getMemberId())
                    .memberPass(hexString.toString())
                    .memberPhoneNumber(dto.getMemberPhoneNumber())
                    .memberAddress(address)
                    .memberProfile("default.jpeg")
                    .mapX(map[0])
                    .mapY(map[1])
                    .build();
            memberRepository.save(m);
        }
    }

    private StringBuilder sha256(String pw) {
        StringBuilder hexString = new StringBuilder();
        try {
            // MessageDigest 객체 생성
            MessageDigest sha256 = MessageDigest.getInstance("SHA-256");
            // 바이트 배열로 변환
            byte[] inputBytes = pw.getBytes();
            // 해시 계산
            byte[] hashBytes = sha256.digest(inputBytes);

            // 바이트 배열을 16진수 문자열로 변환
            for (byte b : hashBytes) {
                hexString.append(String.format("%02x", b));
            }
        } catch (NoSuchAlgorithmException e) {

        }
        return hexString;
    }

    public String reg(Member dto, Errors errors){
        if (errors.hasErrors()) { // 에러 발생시
            return "reg";
        } else {
            insertMember(dto);
            return "login";
        }
    }

    public int login(Member dto) {
        Optional<MemberEntity> result = memberRepository.findByMemberId(dto.getMemberId());
        if (result.isEmpty()) return 0;
        else {
            StringBuilder hexString = sha256(dto.getMemberPass());
            if (!hexString.toString().equals(result.get().getMemberPass())) return 1;
            else {
                return 2;
            }
        }
    }

    public int sync(Member dto) {
        Optional<MemberEntity> user = memberRepository.findByMemberId(dto.getMemberId());
        if (user.isEmpty()) return 0;
        else {
            StringBuilder hexString = sha256(dto.getMemberPass());
            if (!hexString.toString().equals(user.get().getMemberPass())) return 1;
            else {
                MemberEntity u = user.get();
                MemberEntity m = update(u,dto.getMemberNaverId(),u.getMemberPass());
                memberRepository.save(m);
                return 2;
            }
        }
    }

    public int confirm(String number, String memberPhoneNumber){
        VerificationCode key = authKey.get(memberPhoneNumber);
        if(number!=null && number.equals(key.getCode())) {
            long currentTime = System.currentTimeMillis();
            long codeTimestamp = key.getTimestamp();
            long EXPIRATION_TIME = 3 * 60 * 1000; // 3분을 밀리초로 변환

            if (currentTime - codeTimestamp <= EXPIRATION_TIME) {
                authKey.remove(memberPhoneNumber);
                return 1;
            } else {
                authKey.remove(memberPhoneNumber);
                return 2;
            }
        }else return 0;
    }

    public int changePasswordBySms(Message messageDto, Member dto) throws Exception {
        String pw = registerMail.createKey();
        messageDto.setContent("임시 비밀번호 [" + pw + "]가 발급되었습니다.");
        SmsResponse response = smsService.sendSms(messageDto);
        if (response.getStatusCode().equals("202")) {
            setNewPassword(dto, pw);
            return 1;
        } else return 0;
    }

    public int changePasswordByEmail(Member dto) throws Exception {
        String pw = registerMail.sendSimpleMessage(dto.getMemberNaverId());
        if (pw == null || pw.isEmpty()) return 0;
        else {
            setNewPassword(dto, pw);
            return 1;
        }
    }

    public int sendUserIdByPhoneNumber(Member dto, Message messageDto) throws UnsupportedEncodingException, URISyntaxException, NoSuchAlgorithmException, InvalidKeyException, JsonProcessingException {
        String memberId = getMemberIdByPhoneNumber(dto);
        if (memberId == null) return 0;
        else {
            messageDto.setContent("아이디는 [" + memberId + "]입니다");
            smsService.sendSms(messageDto);
            return 1;
        }
    }

    public int sendVerificationCodeBySms(Message messageDto) throws JsonProcessingException, RestClientException, URISyntaxException, InvalidKeyException, NoSuchAlgorithmException, UnsupportedEncodingException {
        Optional<MemberEntity> memberPhoneNumber = memberRepository.findByMemberPhoneNumber(messageDto.getTo());
        VerificationCode verificationCode = authKey.get(messageDto.getTo());
        if(memberPhoneNumber.isEmpty()&&verificationCode==null){
            messageDto.setContent("인증번호 [" + createNumber(messageDto) + "]를 입력해주세요");
            SmsResponse response = smsService.sendSms(messageDto);
            if (response.getStatusCode().equals("202")) return 1;
            else return 0;
        }else return 2;
    }

    public HashMap<String, String> callback(String code, String state, HttpSession session) throws IOException {
        HashMap<String, String> map = new HashMap<>();
        String naverId;

        OAuth2AccessToken oauthToken;
        oauthToken = naverLoginApi.getAccessToken(session, code, state);

        //로그인 사용자 정보를 읽어온다.
        apiResult = naverLoginApi.getUserProfile(oauthToken);

        //JSON 파싱
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(apiResult);
        naverId = rootNode.get("response").get("email").asText();

        Optional<MemberEntity> result = memberRepository.findByMemberNaverId(naverId);
        if (result.isEmpty()) {
            map.put("naverId", naverId);
            map.put("result", "1");
            return map;
        } else {
            map.put("memberId",result.get().getMemberId());
            map.put("result", "2");
            return map;
        }
    }

    public int dup(Member dto) {
        Optional<MemberEntity> result = memberRepository.findByMemberId(dto.getMemberId());
        if (result.isEmpty()) return 0;
        else return 1;
    }

    public String getAuthorizationUrl(HttpSession session) {
        return naverLoginApi.getAuthorizationUrl(session);
    }

    public HashMap<String, String> getNaverIdAndPhoneNumberByUserId(Member dto) {
        Optional<MemberEntity> result = memberRepository.findByMemberId(dto.getMemberId());
        HashMap<String, String> map = new HashMap<>();
        if(result.isPresent()){
            map.put("phoneNumber", result.get().getMemberPhoneNumber());
            map.put("naverId", result.get().getMemberNaverId());
        }else {
            map.put("result","false");
        }
        return map;
    }
}

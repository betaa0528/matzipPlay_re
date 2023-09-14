package com.restaurantProject.famousrestaurant.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.scribejava.core.model.OAuth2AccessToken;
import com.restaurantProject.famousrestaurant.dto.Member;
import com.restaurantProject.famousrestaurant.entity.MemberEntity;
import com.restaurantProject.famousrestaurant.repository.MemberRepository;
import com.restaurantProject.famousrestaurant.util.KakaoMapApi;
import com.restaurantProject.famousrestaurant.util.NaverLoginApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.util.HashMap;
import java.util.Optional;

@Component
public class LoginService {

    private String apiResult = null;
    private MemberRepository memberRepository;
    private NaverLoginApi naverLoginApi;
    private KakaoMapApi kakaoMapApi;

    @Autowired
    public void setMemberRepository(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Autowired
    private void setNaverLoginBO(NaverLoginApi naverLoginApi) {
        this.naverLoginApi = naverLoginApi;
    }

    @Autowired
    public void setKakaoMapService(KakaoMapApi kakaoMapApi) {
        this.kakaoMapApi = kakaoMapApi;
    }

    public int login(Member dto, HttpSession session) {
        Optional<MemberEntity> result = memberRepository.findByMemberId(dto.getMemberId());

        if (result.isEmpty()) return 0;
        else {
            StringBuilder hexString = sha256(dto.getMemberPass());
            if (!hexString.toString().equals(result.get().getMemberPass())) return 1;
            else{
                session.setAttribute("memberId",dto.getMemberId());
                return 2;
            }
        }
    }

    public int sync(Member dto, HttpSession session){
        Optional<MemberEntity> user = memberRepository.findByMemberId(dto.getMemberId());
        if (user.isEmpty()) return 0;
        else {
            StringBuilder hexString = sha256(dto.getMemberPass());
            if (!hexString.toString().equals(user.get().getMemberPass())) return 1;
            else{
                MemberEntity u = user.get();
                MemberEntity m = MemberEntity.builder()
                        .id(u.getId())
                        .memberId(u.getMemberId())
                        .memberPass(u.getMemberPass())
                        .memberNaverId(dto.getMemberNaverId())
                        .memberAddress(u.getMemberAddress())
                        .memberPhoneNumber(u.getMemberPhoneNumber())
                        .mapX(u.getMapX())
                        .mapY(u.getMapY())
                        .build();
                memberRepository.save(m);
                session.setAttribute("memberId",u.getMemberId());
                return 2;
            }
        }
    }

    public void forgotpw(Member dto, String pw){
        Optional<MemberEntity> user = memberRepository.findByMemberNaverId(dto.getMemberNaverId());

        if (user.isPresent()){
            StringBuilder hexString = sha256(pw);
            MemberEntity u = user.get();
            MemberEntity m = MemberEntity.builder()
                .id(u.getId())
                .memberId(u.getMemberId())
                .memberPass(hexString.toString())
                .memberNaverId(dto.getMemberNaverId())
                .memberAddress(u.getMemberAddress())
                .memberPhoneNumber(u.getMemberPhoneNumber())
                .mapX(u.getMapX())
                .mapY(u.getMapY())
                .build();
            memberRepository.save(m);
        }
    }

    public HashMap<String,String> callback(String code, String state, HttpSession session) throws IOException {
        HashMap<String,String> map = new HashMap<>();
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
            map.put("naverId",naverId);
            map.put("result","1");
            return map;
        } else {
            session.setAttribute("memberId",result.get().getMemberId());
            map.put("result","2");
            return map;
        }
    }

    public StringBuilder sha256(String pw){
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

    public void reg(Member dto) {
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
                    .mapX(map[0])
                    .mapY(map[1])
                    .build();
            memberRepository.save(m);
        }
    }

    public int dup(Member dto){
        Optional<MemberEntity> result = memberRepository.findByMemberId(dto.getMemberId());
        if(result.isEmpty()) return 0;
        else return 1;
    }

    public String getAuthorizationUrl(HttpSession session) {
        return naverLoginApi.getAuthorizationUrl(session);
    }

    public HashMap<String,String> getNaverIdAndPhoneNumberByUserId(Member dto){
        Optional<MemberEntity> result = memberRepository.findByMemberId(dto.getMemberId());
        HashMap<String,String> map = new HashMap<>();
        map.put("phoneNumber",result.get().getMemberPhoneNumber());
        map.put("naverId",result.get().getMemberNaverId());
        return map;
    }
}

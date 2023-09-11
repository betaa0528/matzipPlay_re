package com.restaurantProject.famousrestaurant.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.scribejava.core.model.OAuth2AccessToken;
import com.restaurantProject.famousrestaurant.dto.Member;
import com.restaurantProject.famousrestaurant.entity.MemberEntity;
import com.restaurantProject.famousrestaurant.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;

@Component
public class LoginService {

    private String apiResult = null;
    private MemberRepository memberRepository;
    private NaverLoginBO naverLoginBO;
    private KakaoMapService kakaoMapService;

    @Autowired
    public void setMemberRepository(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Autowired
    private void setNaverLoginBO(NaverLoginBO naverLoginBO) {
        this.naverLoginBO = naverLoginBO;
    }

    @Autowired
    public void setKakaoMapService(KakaoMapService kakaoMapService) {
        this.kakaoMapService = kakaoMapService;
    }

    public int login(Member dto) {
        Optional<MemberEntity> result = memberRepository.findByMemberId(dto.getMemberId());

        if (result.isEmpty()) return 0;
        else {
            String pw = result.get().getMemberPass();
            if (!pw.equals(dto.getMemberPass())) return 1;
            else return 2;
        }
    }

    public void callback(String code, String state, HttpSession session) throws IOException {
        String naverId;
        String id = (String) session.getAttribute("memberId");

        OAuth2AccessToken oauthToken;
        oauthToken = naverLoginBO.getAccessToken(session, code, state);

        //로그인 사용자 정보를 읽어온다.
        apiResult = naverLoginBO.getUserProfile(oauthToken);

        //JSON 파싱
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(apiResult);
        naverId = rootNode.get("response").get("email").asText();

        Optional<MemberEntity> result = memberRepository.findByMemberNaverId(naverId);
        if (result.isEmpty()) {
            Optional<MemberEntity> user = memberRepository.findByMemberId(id);

            MemberEntity u = user.get();
            MemberEntity m = MemberEntity.builder()
                    .id(u.getId())
                    .memberId(u.getMemberId())
                    .memberPass(u.getMemberPass())
                    .memberNaverId(naverId)
                    .memberAddress(u.getMemberAddress())
                    .memberPhoneNumber(u.getMemberPhoneNumber())
                    .build();
            memberRepository.save(m);

            System.out.println("네이버 아이디 생성완료");
        } else {
            System.out.println("이미 연동되어 있음");
        }
    }

    public void reg(Member dto) {
        StringBuilder hexString = new StringBuilder();

        try {
            // MessageDigest 객체 생성
            MessageDigest sha256 = MessageDigest.getInstance("SHA-256");

            // 바이트 배열로 변환
            byte[] inputBytes = dto.getMemberPass().getBytes();

            // 해시 계산
            byte[] hashBytes = sha256.digest(inputBytes);

            // 바이트 배열을 16진수 문자열로 변환
            for (byte b : hashBytes) {
                hexString.append(String.format("%02x", b));
            }
        } catch (NoSuchAlgorithmException e) {
        }

        Optional<MemberEntity> result = memberRepository.findByMemberId(dto.getMemberId());

        if (result.isEmpty()) {
            String address = dto.getMemberAddress() + " " + dto.getMemberDetailAddr();
            String[] map = kakaoMapService.map(address);

            MemberEntity m = MemberEntity.builder()
                    .memberId(dto.getMemberId())
                    .memberPass(hexString.toString())
                    .memberPhoneNumber(dto.getMemberPhoneNumber())
                    .memberAddress(address)
                    .mapX(map[0])
                    .mapY(map[1])
                    .build();
            memberRepository.save(m);
        } else {
            System.out.println("이미 있는 아이디");
        }
    }

    public int dup(Member dto){
        Optional<MemberEntity> result = memberRepository.findByMemberId(dto.getMemberId());
        if(result.isEmpty()) return 0;
        else return 1;
    }
    public String getAuthorizationUrl(HttpSession session) {
        return naverLoginBO.getAuthorizationUrl(session);
    }

}

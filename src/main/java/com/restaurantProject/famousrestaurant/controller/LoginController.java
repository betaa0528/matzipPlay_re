package com.restaurantProject.famousrestaurant.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.scribejava.core.model.OAuth2AccessToken;
import com.restaurantProject.famousrestaurant.dto.Member;
import com.restaurantProject.famousrestaurant.entity.MemberEntity;
import com.restaurantProject.famousrestaurant.repository.MemberRepository;
import com.restaurantProject.famousrestaurant.service.NaverLoginBO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;


@Controller
public class LoginController {

    /* NaverLoginBO */
    private NaverLoginBO naverLoginBO;
    private String apiResult = null;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    private void setNaverLoginBO(NaverLoginBO naverLoginBO) {
        this.naverLoginBO = naverLoginBO;
    }

    //로그인 첫 화면 요청 메소드
    @RequestMapping(value = "/login", method = { RequestMethod.GET})
    public String login(Model model, HttpSession session) {
        /* 네이버아이디로 인증 URL을 생성하기 위하여 naverLoginBO클래스의 getAuthorizationUrl메소드 호출 */
        String naverAuthUrl = naverLoginBO.getAuthorizationUrl(session);
        //네이버
        model.addAttribute("url", naverAuthUrl);

        return "login";
    }

    //네이버 로그인 성공시 callback호출 메소드
    @RequestMapping(value = "/callback", method = { RequestMethod.GET, RequestMethod.POST })
    public String callback(Model model, @RequestParam String code, @RequestParam String state, HttpSession session) throws IOException {

        String naverId;

        OAuth2AccessToken oauthToken;
        oauthToken = naverLoginBO.getAccessToken(session, code, state);

        //로그인 사용자 정보를 읽어온다.
        apiResult = naverLoginBO.getUserProfile(oauthToken);
        model.addAttribute("result", apiResult);

        //JSON 파싱
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(apiResult);
        naverId = rootNode.get("response").get("email").asText();

        System.out.println("Email: " + naverId);

        String id = "tjdgur";

        Optional<MemberEntity> result = memberRepository.findByMemberNaverId(naverId);

        if(result.isEmpty()){
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
        }else{
            System.out.println("이미 연동되어 있음");
        }

        /* 네이버 로그인 성공 페이지 View 호출 */
        return "home";
    }

    @GetMapping("home")
    public String h(){return "home";}

    @GetMapping("/reg")
    public String reg(){
        return "reg";
    }

    @PostMapping("/reg")
    public void reg(Member dto){

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

        if(result.isEmpty()) {
            MemberEntity m = MemberEntity.builder()
                    .memberId(dto.getMemberId())
                    .memberPass(hexString.toString())
                    .memberPhoneNumber(dto.getMemberPhoneNumber())
                    .memberAddress(dto.getMemberAddress() + " " + dto.getMemberDetailAddr())
                    .build();
            memberRepository.save(m);
        }else {
            System.out.println("이미 있는 아이디");
        }
    }

    @PostMapping("/login")
    @ResponseBody
    public int login(Member dto){
        Optional<MemberEntity> result = memberRepository.findByMemberId(dto.getMemberId());

        if(result.isEmpty()) return 0;
        else{
            String pw = result.get().getMemberPass();
            if(!pw.equals(dto.getMemberPass())) return 1;
            else return 2;
        }
    }



}
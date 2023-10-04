package com.restaurantProject.famousrestaurant.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.restaurantProject.famousrestaurant.dto.Member;
import com.restaurantProject.famousrestaurant.sms.dto.Message;
import com.restaurantProject.famousrestaurant.service.LoginService;
import com.restaurantProject.famousrestaurant.service.RegisterMail;
import com.restaurantProject.famousrestaurant.service.SmsService;
import com.restaurantProject.famousrestaurant.sms.dto.smsResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClientException;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

@Controller
@RequiredArgsConstructor
public class LoginController {

    private final LoginService loginService;
    private final RegisterMail registerMail;
    private final SmsService smsService;

    @PostMapping("send")
    @ResponseBody
    public int sendSms(Message messageDto) throws JsonProcessingException, RestClientException, URISyntaxException, InvalidKeyException, NoSuchAlgorithmException, UnsupportedEncodingException {
        messageDto.setContent("인증번호 [" + loginService.createNumber(messageDto) + "]를 입력해주세요");
        smsResponse response = smsService.sendSms(messageDto);
        if(response.getStatusCode().equals("202")) return 1;
        else return 0;
    }

    @PostMapping("confirm")
    @ResponseBody
    public int confirm(String number, String memberPhoneNumber) {
        return loginService.confirm(number, memberPhoneNumber);
    }

    @GetMapping("home")
    public String home() {
        return "home";
    }

    @GetMapping("login")
    public String login(Model model, HttpSession session) {
        /* 네이버아이디로 인증 URL을 생성하기 위하여 naverLoginBO클래스의 getAuthorizationUrl메소드 호출 */
        String naverAuthUrl = loginService.getAuthorizationUrl(session);
        model.addAttribute("url", naverAuthUrl);
        return "login";
    }

    @PostMapping("login")
    @ResponseBody
    public int login(Member dto, HttpSession session) {
        int result = loginService.login(dto);
        if(result==2) session.setAttribute("memberId", dto.getMemberId());
        return result;
    }

    @RequestMapping(value = "/callback", method = {RequestMethod.GET, RequestMethod.POST})
    public String callback(Model model, @RequestParam String code, @RequestParam String state, HttpSession session) throws IOException {
        HashMap<String, String> result = loginService.callback(code, state, session);
        if (result.get("result").equals("1")) {
            model.addAttribute("naverId", result.get("naverId"));
            return "sync";
        } else return "home";
    }

    @PostMapping("sync")
    @ResponseBody
    public int sync(Member dto, HttpSession session) {
        int result = loginService.sync(dto);
        if(result==2) session.setAttribute("memberId", dto.getMemberId());
        return result;
    }

    @GetMapping("reg")
    public String reg() {
        return "reg";
    }

    @PostMapping("reg")
    public String reg(Member dto) {
        loginService.reg(dto);
        return "login";
    }

    @PostMapping("dup")
    @ResponseBody
    public int dup(Member dto) {
        return loginService.dup(dto);
    }

    @PostMapping("resetPassEmail")
    @ResponseBody
    public int resetPassEmail(Member dto) throws Exception {
        String pw = registerMail.sendSimpleMessage(dto.getMemberNaverId());
        if(pw==null||pw.isEmpty()) return 0;
        else {
            loginService.forgotpw(dto, pw);
            return 1;
        }
    }

    @PostMapping("resetPassPhone")
    @ResponseBody
    public int resetPassPhone(Message messageDto, Member dto) throws Exception {
        String pw = registerMail.createKey();
        messageDto.setContent("임시 비밀번호 [" + pw + "]가 발급되었습니다.");
        smsResponse response = smsService.sendSms(messageDto);
        if(response.getStatusCode().equals("202")){
            loginService.forgotpw(dto, pw);
            return 1;
        }else return 0;
    }

    @PostMapping("forgotPw")
    @ResponseBody
    public HashMap<String, String> forgotPw(Member dto) {
        return loginService.getNaverIdAndPhoneNumberByUserId(dto);
    }

    @PostMapping("forgotId")
    @ResponseBody
    public int forgotId(Member dto, Message messageDto) throws UnsupportedEncodingException, URISyntaxException, NoSuchAlgorithmException, InvalidKeyException, JsonProcessingException {
        String memberId = loginService.getMemberIdByPhoneNumber(dto);
        if(memberId==null) return 0;
        else{
            messageDto.setContent("아이디는 [" + memberId + "]입니다");
            smsService.sendSms(messageDto);
            return 1;
        }
    }

    @GetMapping("forgot")
    public String forgotId() {
        return "forgot";
    }

}
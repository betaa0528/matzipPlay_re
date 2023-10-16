package com.restaurantProject.famousrestaurant.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.restaurantProject.famousrestaurant.dto.Member;
import com.restaurantProject.famousrestaurant.service.LoginService;
import com.restaurantProject.famousrestaurant.sms.dto.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClientException;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
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

    @RequestMapping(value = "/callback", method = {RequestMethod.GET, RequestMethod.POST})  /* 네이버 연동 콜백 */
    public String callback(Model model, @RequestParam String code, @RequestParam String state, HttpSession session) throws IOException {
        HashMap<String, String> result = loginService.callback(code, state, session);
        if (result.get("result").equals("1")) {
            model.addAttribute("naverId", result.get("naverId"));
            return "sync";
        } else {
            session.setAttribute("memberId", result.get("memberId"));
            return "index";
        }
    }

    @GetMapping("sync")
    public String sync(){return "sync";}

    @GetMapping("forgot") /* 아이디, 비밀번호 찾기 페이지 */
    public String forgotId() {
        return "forgot";
    }

    @GetMapping("reg") /* 회원가입 페이지 */
    public String reg() {
        return "reg";
    }

    @GetMapping("login") /* 로그인 페이지 */
    public String login(Model model, HttpSession session) {
        String naverAuthUrl = loginService.getAuthorizationUrl(session); /* 네이버아이디로 인증 URL을 생성하기 위하여 getAuthorizationUrl 메소드 호출 */
        model.addAttribute("url", naverAuthUrl);
        return "login";
    }

    @PostMapping("login") /* 로그인 */
    @ResponseBody
    public int login(Member dto, HttpSession session) {
        int result = loginService.login(dto);
        if (result == 2) session.setAttribute("memberId", dto.getMemberId());
        return result;
    }

    @PostMapping("sync") /* 네이버 아이디 연동하기 */
    @ResponseBody
    public int sync(Member dto, HttpSession session) {
        int result = loginService.sync(dto);
        if (result == 2) session.setAttribute("memberId", dto.getMemberId());
        return result;
    }

    @PostMapping("reg") /* 회원 가입 */
    public String reg(@Valid Member dto, Errors errors) { return loginService.reg(dto, errors); }

    @PostMapping("reg/send") /* sms 문자 보내기 */
    @ResponseBody
    public int sendSms(Message messageDto) throws JsonProcessingException, RestClientException, URISyntaxException, InvalidKeyException, NoSuchAlgorithmException, UnsupportedEncodingException {
        System.out.println(messageDto.getTo());
        return loginService.sendVerificationCodeBySms(messageDto);
    }

    @PostMapping("reg/confirm") /* sms 인증번호 확인 */
    @ResponseBody
    public int confirm(String number, String memberPhoneNumber) { return loginService.confirm(number, memberPhoneNumber); }

    @PostMapping("reg/dup") /* 아이디 중복 체크 */
    @ResponseBody
    public int dup(Member dto) { return loginService.dup(dto);}

    @PostMapping("forgot/reset-pass-email") /* 임시 비밀번호 이메일로 발급 */
    @ResponseBody
    public int resetPassEmail(Member dto) throws Exception { return loginService.changePasswordByEmail(dto); }

    @PostMapping("forgot/reset-pass-phone") /* 임시 비밀번호 휴대전화로 발급 */
    @ResponseBody
    public int resetPassPhone(Message messageDto, Member dto) throws Exception { return loginService.changePasswordBySms(messageDto,dto);}

    @PostMapping("forgot/member-pw") /* 회원 아이디를 받아 네이버 아이디와 전화번호를 return */
    @ResponseBody
    public HashMap<String, String> forgotPw(Member dto) { return loginService.getNaverIdAndPhoneNumberByUserId(dto);}

    @PostMapping("forgot/member-id") /* 회원 전화번호를 받아 전화번호에 회원 아이디 전송 */
    @ResponseBody
    public int forgotId(Member dto, Message messageDto) throws UnsupportedEncodingException, URISyntaxException, NoSuchAlgorithmException, InvalidKeyException, JsonProcessingException {
        return loginService.sendUserIdByPhoneNumber(dto,messageDto);
    }
}
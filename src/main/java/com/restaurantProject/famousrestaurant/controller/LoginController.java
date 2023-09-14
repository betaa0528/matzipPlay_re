package com.restaurantProject.famousrestaurant.controller;

import com.restaurantProject.famousrestaurant.dto.Member;
import com.restaurantProject.famousrestaurant.service.LoginService;
import com.restaurantProject.famousrestaurant.service.RegisterMail;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;

@Controller
@RequiredArgsConstructor
public class LoginController {

    private final LoginService loginService;
    private final RegisterMail registerMail;

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

    @PostMapping("/login")
    @ResponseBody
    public int login(Member dto, HttpSession session) {
        return loginService.login(dto, session);
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
        return loginService.sync(dto, session);
    }

    @GetMapping("reg")
    public String reg() {
        return "reg";
    }

    @PostMapping("reg")
    public void reg(Member dto) {
        loginService.reg(dto);
    }

    @PostMapping("/dup")
    @ResponseBody
    public int dup(Member dto) {
        return loginService.dup(dto);
    }

    @PostMapping("dlatl")
    public void dlatl(Member dto) throws Exception {
        String pw = registerMail.sendSimpleMessage(dto.getMemberNaverId());
        loginService.forgotpw(dto, pw);
    }

    @PostMapping("forgotPw")
    @ResponseBody
    public HashMap<String, String> forgotPw(Member dto) {
        return loginService.getNaverIdAndPhoneNumberByUserId(dto);
    }

    @GetMapping("forgotPw")
    public String fgp() {
        return "forgotPw";
    }
}
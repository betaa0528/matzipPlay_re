package com.restaurantProject.famousrestaurant.controller;

import com.restaurantProject.famousrestaurant.dto.Member;
import com.restaurantProject.famousrestaurant.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.io.IOException;


@Controller
public class LoginController {

    private LoginService loginService;

    @Autowired
    public void setLoginService(LoginService loginService) {
        this.loginService = loginService;
    }

    //로그인 첫 화면 요청 메소드
    @GetMapping("login")
    public String login(Model model, HttpSession session) {
        /* 네이버아이디로 인증 URL을 생성하기 위하여 naverLoginBO클래스의 getAuthorizationUrl메소드 호출 */
        String naverAuthUrl = loginService.getAuthorizationUrl(session);
        model.addAttribute("url", naverAuthUrl);
        return "login";
    }

    @PostMapping("/login")
    @ResponseBody
    public int login(Member dto) {
        return loginService.login(dto);
    }

    //네이버 로그인 성공시 callback호출 메소드
    @RequestMapping(value = "/callback", method = {RequestMethod.GET, RequestMethod.POST})
    public String callback(Model model, @RequestParam String code, @RequestParam String state, HttpSession session) throws IOException {
        loginService.callback(code, state, session);
        return "home";
    }

    @GetMapping("home")
    public String home() {
        return "home";
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

}
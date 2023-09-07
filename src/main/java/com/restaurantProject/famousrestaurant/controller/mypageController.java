package com.restaurantProject.famousrestaurant.controller;

import com.restaurantProject.famousrestaurant.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class mypageController {
    private final RestaurantRepository restaurantRepository;
    @GetMapping("/mypage")
    public String getMypage(Model model){
        //아이디 확인: session은 db두 개 다 확인필요
        String id = "hr1234";

        //찜목록 가져오기

        //리뷰목록 가져오기

        model.addAttribute("ss",id);

        return "mypage";
    }
}

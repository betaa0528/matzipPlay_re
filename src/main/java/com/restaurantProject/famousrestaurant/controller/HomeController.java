package com.restaurantProject.famousrestaurant.controller;

import com.restaurantProject.famousrestaurant.service.RestaurantService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpSession;

@Controller
public class HomeController {
    private final RestaurantService restaurantService;

    public HomeController(RestaurantService restaurantService) {
        this.restaurantService = restaurantService;
    }

    @GetMapping
    public String index(HttpSession session, Model model) {
//        restaurantService.saveDistance(session);
        model.addAttribute("session", session.getAttribute("memberId"));
        return "index";
    }
}

package com.restaurantProject.famousrestaurant.controller;

import com.restaurantProject.famousrestaurant.geo.GeoPoint;
import com.restaurantProject.famousrestaurant.service.RestaurantService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final RestaurantService restaurantService;

    @GetMapping("")
    public String index(HttpSession session, Model model) {
        restaurantService.saveDistance(session);
        model.addAttribute("session", session.getAttribute("memberId"));
        return "index";
    }
}

package com.restaurantProject.famousrestaurant.controller;

import com.restaurantProject.famousrestaurant.service.RestaurantService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final RestaurantService restaurantService;

    @GetMapping("/")
    public String index() {
        return "index";
    }
}

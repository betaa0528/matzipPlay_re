package com.restaurantProject.famousrestaurant.controller;

import com.restaurantProject.famousrestaurant.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class testController {
    private final RestaurantRepository restaurantRepository;

    @GetMapping("/")
    public String test() {
        restaurantRepository.findAll().forEach(System.out::println);
        return "index";
    }
}

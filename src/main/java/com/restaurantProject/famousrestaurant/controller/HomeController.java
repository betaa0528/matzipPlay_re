package com.restaurantProject.famousrestaurant.controller;

import com.restaurantProject.famousrestaurant.geo.GeoPoint;
import com.restaurantProject.famousrestaurant.service.RestaurantService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final RestaurantService restaurantService;

    @GetMapping("/test")
    public String testPage(HttpServletRequest request) {

        HttpSession session = request.getSession();
        GeoPoint pt1 = new GeoPoint(127.2345262, 37.383784);
        session.setAttribute("userMap", pt1);

        return "test";
    }
}

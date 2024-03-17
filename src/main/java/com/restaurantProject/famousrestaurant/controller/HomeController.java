package com.restaurantProject.famousrestaurant.controller;

import com.restaurantProject.famousrestaurant.dto.security.BoardPrincipal;
import com.restaurantProject.famousrestaurant.service.RestaurantService;
import org.apache.tomcat.util.descriptor.web.ContextHandler;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.security.Principal;

@Controller
public class HomeController {
    private final RestaurantService restaurantService;

    public HomeController(RestaurantService restaurantService) {
        this.restaurantService = restaurantService;
    }

    @GetMapping
    public String index(HttpSession session, Model model, @AuthenticationPrincipal BoardPrincipal principal) {
        model.addAttribute("principal", principal);
        return "redirect:/restaurant";
    }

    @GetMapping("/user")
    @ResponseBody
    public Authentication getUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth;
    }

    @GetMapping("/admin")
    @ResponseBody
    public Authentication getAdmin() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth;
    }
}

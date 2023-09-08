package com.restaurantProject.famousrestaurant.controller;

import com.restaurantProject.famousrestaurant.dto.Restaurant;
import com.restaurantProject.famousrestaurant.dto.Review;
import com.restaurantProject.famousrestaurant.service.RestaurantService;
import com.restaurantProject.famousrestaurant.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/review")
public class ReviewController {

    private final ReviewService reviewService;
    private final RestaurantService restaurantService;

    @GetMapping("/restaurant/{id}")
    public String reviewForm(@PathVariable Long id, Model model) {
//        System.out.println(restaurantService.findById(id));
        Restaurant byId = restaurantService.findById(id);
        model.addAttribute("byId", byId);
        return "review";
    }

    @PostMapping("/restaurant")
    public String reviewSave(@ModelAttribute Review review) throws IOException {
        System.out.println(review);
        reviewService.save(review);
        return "redirect:/restaurant/detail/" + review.getRestaurantId();
    }
}

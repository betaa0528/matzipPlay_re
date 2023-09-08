package com.restaurantProject.famousrestaurant.controller;

import com.restaurantProject.famousrestaurant.dto.Restaurant;
import com.restaurantProject.famousrestaurant.dto.Review;
import com.restaurantProject.famousrestaurant.entity.RestaurantEntity;
import com.restaurantProject.famousrestaurant.entity.ReviewEntity;
import com.restaurantProject.famousrestaurant.repository.RestaurantRepository;
import com.restaurantProject.famousrestaurant.service.RestaurantService;
import com.restaurantProject.famousrestaurant.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/restaurant")
@RequiredArgsConstructor
public class RestaurantController {

    private final RestaurantService restaurantService;
    private final ReviewService reviewService;

    @GetMapping("/list")
    public String RestaurantList(Model model) {
        List<Restaurant> list = restaurantService.findAll();
        model.addAttribute("restaurantlist" , list);
        return "restaurantList";
    }

    @GetMapping("/detail/{id}")
    public String RestaurantDetail(@PathVariable Long id, Model model) {
        Restaurant restaurant = restaurantService.findById(id);
        String query = restaurant.getRestaurantName();
        List<Review> reviews = reviewService.findAll(id);
        model.addAttribute("reviews", reviews);
        model.addAttribute("image", restaurantService.search(query));
        model.addAttribute("restaurant", restaurant);
        return "detail";
    }

}

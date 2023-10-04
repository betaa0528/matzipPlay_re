package com.restaurantProject.famousrestaurant.controller;

import com.restaurantProject.famousrestaurant.dto.Restaurant;
import com.restaurantProject.famousrestaurant.dto.Review;
import com.restaurantProject.famousrestaurant.dto.WishList;
import com.restaurantProject.famousrestaurant.service.RestaurantService;
import com.restaurantProject.famousrestaurant.service.ReviewService;
import com.restaurantProject.famousrestaurant.service.WishListService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Controller
@RequiredArgsConstructor
@RequestMapping("/review")
public class ReviewController {

    private final ReviewService reviewService;
    private final RestaurantService restaurantService;
    private final WishListService wishListService;

    @GetMapping("/restaurant/{id}")
    public String reviewForm(@PathVariable Long id, Model model, HttpSession session) {
        String memberId = session.getAttribute("memberId").toString();
        Restaurant byId = restaurantService.findById(id);
        model.addAttribute("byId", byId);
        model.addAttribute("memberId", memberId);
        return "review";
    }

    @PostMapping("/restaurant")
    public String reviewSave(@ModelAttribute Review review) throws IOException {
        reviewService.save(review);
        return "redirect:/restaurant/detail/" + review.getRestaurantId();
    }

    @PostMapping("/wishList")
    public ResponseEntity getWishList(WishList wishList){
        System.out.println("wishList : " + wishList);
        int wishListChk = wishListService.updateWishList(wishList);
        return new ResponseEntity<>(wishListChk, HttpStatus.OK);
    }
}

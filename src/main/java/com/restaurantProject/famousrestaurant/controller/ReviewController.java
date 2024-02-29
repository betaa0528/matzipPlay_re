package com.restaurantProject.famousrestaurant.controller;

import com.restaurantProject.famousrestaurant.dto.*;
import com.restaurantProject.famousrestaurant.service.RestaurantService;
import com.restaurantProject.famousrestaurant.service.ReviewService;
import com.restaurantProject.famousrestaurant.service.WishListService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.io.IOException;

@Controller
@RequiredArgsConstructor
@RequestMapping("/review")
public class ReviewController {

    private final ReviewService reviewService;
    private final RestaurantService restaurantService;
    private final WishListService wishListService;

    @GetMapping
    public String review(
            @PageableDefault(page = 1) Pageable pageable,
            Model model
    ) {
        Page<Review> reviews = reviewService.findAll(pageable);
        int blockLimit = 3;
        int startPage = (((int) (Math.ceil((double) pageable.getPageNumber() / blockLimit))) - 1) * blockLimit + 1; // 1 4 7 10 ~~
        int endPage = Math.min((startPage + blockLimit - 1), reviews.getTotalPages());
        model.addAttribute("reviews", reviews);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        return "review";
    }

    @GetMapping("/restaurant/{id}")
    public String reviewForm(@PathVariable Long id, Model model, HttpSession session) {
        String memberId = session.getAttribute("memberId").toString();
        Restaurant byId = restaurantService.findById(id);
        model.addAttribute("byId", byId);
        model.addAttribute("memberId", memberId);
        model.addAttribute("session", session.getAttribute("memberId"));
        return "reviewForm";
    }

    @PostMapping("/restaurant")
    public String reviewSave(@ModelAttribute Review review) throws IOException {
        System.out.println(review);
        reviewService.save(review);
        return "redirect:/restaurant/detail/" + review.getRestaurantId();
    }

    @PostMapping("/wishList")
    public ResponseEntity getWishList(WishList wishList) {
//        System.out.println("wishList : " + wishList);
        int wishListChk = wishListService.updateWishList(wishList);
        return new ResponseEntity<>(wishListChk, HttpStatus.OK);
    }

    @GetMapping("/update/{id}")
    public String reviewEdit(@PathVariable Long id, Model model) {
        Review review = reviewService.findById(id);
        Restaurant restaurant = restaurantService.findById(review.getRestaurantId());
        model.addAttribute("review", review);
        model.addAttribute("restaurant", restaurant);
        return "reviewUpdate";
    }

    @PostMapping("/update")
    public String reviewUpdate(@ModelAttribute ReviewUpdate reviewUpdate) throws IOException {
//        System.out.println("reviewController reviewUpdate post : " + reviewUpdate);
        reviewService.update(reviewUpdate);

        return "redirect:/restaurant/detail/" + reviewUpdate.getRestaurantId();
    }

}

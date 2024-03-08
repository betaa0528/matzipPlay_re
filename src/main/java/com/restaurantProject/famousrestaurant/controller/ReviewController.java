package com.restaurantProject.famousrestaurant.controller;

import com.restaurantProject.famousrestaurant.dto.*;
import com.restaurantProject.famousrestaurant.dto.security.BoardPrincipal;
import com.restaurantProject.famousrestaurant.entity.BaseEntity;
import com.restaurantProject.famousrestaurant.service.PaginationService;
import com.restaurantProject.famousrestaurant.service.RestaurantService;
import com.restaurantProject.famousrestaurant.service.ReviewService;
import com.restaurantProject.famousrestaurant.service.WishListService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/review")
public class ReviewController {

    private final ReviewService reviewService;
    private final RestaurantService restaurantService;
    private final WishListService wishListService;
    private final PaginationService paginationService;

    @GetMapping
    public String reviewPage(Model model) {
        Page<Review> reviews = reviewService.findAll(PageRequest.of(1, 10, Sort.by(Sort.Order.desc("createdAt"))));
        model.addAttribute("reviews", reviews);
        return "review";
    }

    @GetMapping("/list")
    public ResponseEntity review(
            @PageableDefault(page = 1, sort = "createdAt" , direction = Sort.Direction.DESC) Pageable pageable
//            @RequestParam("page") int page
    ) {
//        Page<Review> reviews;
//        System.out.println("page : " + page);
//        if(page > 0) {
//            reviews = reviewService.findAllMove(pageable, page);
//        } else {
//            reviews = reviewService.findAll(pageable);
//        }
        Page<Review> reviews = reviewService.findAll(pageable);
        if(reviews != null) {
            return new ResponseEntity<>(reviews, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("리뷰가 엄써용", HttpStatus.NOT_FOUND);
        }
//        List<Integer> barNumbers = paginationService.getPaginationBarNumbers(pageable.getPageNumber(), reviews.getTotalPages());
//        log.info("number : " + reviews.getNumber());
//        log.info("page : " + pageable.getPageNumber());
//        model.addAttribute("reviews", reviews);
//        model.addAttribute("pageBarNumbers", barNumbers);
//        return "review";
    }

    @GetMapping("/form/{id}")
    public String reviewForm(@PathVariable Long id, Model model, HttpSession session, @AuthenticationPrincipal BoardPrincipal principal) {
        Restaurant restaurant = restaurantService.findById(id);
        model.addAttribute("restaurant", restaurant);
        model.addAttribute("principal", principal);
        return "reviewForm";
    }

    @PostMapping("/form")
    public String reviewSave(@ModelAttribute Review review, @AuthenticationPrincipal BoardPrincipal principal) throws IOException {
//        System.out.println(review);
        reviewService.save(review);
        return "redirect:/restaurant/detail/" + review.getRestaurantId();
    }

    @PostMapping("/wishList")
    public ResponseEntity<WishList> getWishList(WishList wishList) {
//        System.out.println("wishList : " + wishList);
        int wishListChk = wishListService.updateWishList(wishList);
        ResponseEntity response = ResponseEntity.status(HttpStatus.OK).body("checked");
        return response;
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

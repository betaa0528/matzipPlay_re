package com.restaurantProject.famousrestaurant.controller;

import com.restaurantProject.famousrestaurant.dto.*;
import com.restaurantProject.famousrestaurant.dto.security.BoardPrincipal;
import com.restaurantProject.famousrestaurant.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
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
import java.util.HashMap;
import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/reviews")
public class ReviewController {

    private final ReviewService reviewService;
    private final RestaurantService restaurantService;
    private final WishListService wishListService;
    private final PaginationService paginationService;
    private final MemberService memberService;

    @GetMapping
    public String review(
            @PageableDefault(page = 1, size = 4, sort = "createdAt" , direction = Sort.Direction.DESC) Pageable pageable,
            @AuthenticationPrincipal BoardPrincipal principal,
            @RequestParam("id") Long id,
            Model model
    ) {
        Page<Review> reviewPages = reviewService.findByRestaurantId(id, pageable); // 해당 {id} 음식점의 리뷰 객체를 모두 가져옴
        HashMap<Long, List<String>> recommend = reviewService.changeRecommend(id); // 리뷰의 추천 버튼들을 가져옴
        HashMap<String, Member> members = memberService.getByMemberIdList(id);
        List<Integer> barNumbers = paginationService.getPaginationBarNumbers(pageable.getPageNumber() + 1, reviewPages.getTotalPages());
        model.addAttribute("reviews", reviewPages);
        model.addAttribute("recommend", recommend);
        model.addAttribute("members", members);
        model.addAttribute("principal", principal);
        model.addAttribute("paginationBarNumbers", barNumbers);
        return "restaurant/detail :: #review-div";
    }

    @GetMapping("/create/{id}")
    public String reviewForm(@PathVariable Long id, Model model, HttpSession session, @AuthenticationPrincipal BoardPrincipal principal) {
        Restaurant restaurant = restaurantService.findById(id);
        model.addAttribute("restaurant", restaurant);
        model.addAttribute("principal", principal);
        return "restaurant/reviewForm";
    }

    @PostMapping("/create")
    public String reviewSave(@ModelAttribute Review review, @AuthenticationPrincipal BoardPrincipal principal) throws IOException {
        reviewService.save(review);
        return "redirect:/restaurant/" + review.getRestaurantId();
    }

    @PostMapping("/wishlist")
    public int clickWishList(WishList wishList) {
        return wishListService.updateWishList(wishList);
    }

    @GetMapping("/update/{id}")
    public String reviewEdit(@PathVariable Long id, Model model) {
        Review review = reviewService.findById(id);
        Restaurant restaurant = restaurantService.findById(review.getRestaurantId());
        model.addAttribute("review", review);
        model.addAttribute("restaurant", restaurant);
        return "restaurant/reviewUpdate";
    }

    @PostMapping("/update")
    public String reviewUpdate(@ModelAttribute ReviewUpdate reviewUpdate) throws IOException {
        reviewService.update(reviewUpdate);

        return "redirect:/restaurant/" + reviewUpdate.getRestaurantId();
    }

}

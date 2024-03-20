package com.restaurantProject.famousrestaurant.controller;

import com.restaurantProject.famousrestaurant.dto.Member;
import com.restaurantProject.famousrestaurant.dto.Restaurant;
import com.restaurantProject.famousrestaurant.dto.Review;
import com.restaurantProject.famousrestaurant.dto.ReviewSummaryDto;
import com.restaurantProject.famousrestaurant.dto.security.BoardPrincipal;
import com.restaurantProject.famousrestaurant.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@Controller
@RequestMapping("/restaurant")
@RequiredArgsConstructor
@Slf4j
public class RestaurantController {

    private final RestaurantService restaurantService;
    private final ReviewService reviewService;
    private final WishListService wishListService;
    private final MemberService memberService;
    private final PaginationService paginationService;


    @GetMapping
    public String index(@AuthenticationPrincipal BoardPrincipal principal, Model model) {
        model.addAttribute("principal", principal);
        return "restaurant/index";
    }

    @GetMapping("/search")
    public String searchRestaurant(
            @AuthenticationPrincipal BoardPrincipal principal,
            @RequestParam("keyword") String keyword,
            @RequestParam(required = false, value = "order", defaultValue = "review") String order,
            @PageableDefault(page = 1) Pageable pageable,
            Model model) {
        Page<Restaurant> restaurantList = restaurantService.search(keyword, pageable, order);
        List<Integer> barNumbers = paginationService.getPaginationBarNumbers(
                pageable.getPageNumber(), restaurantList.getTotalPages());
        model.addAttribute("principal", principal);
        model.addAttribute("order", order);
        model.addAttribute("keyword", keyword);
        model.addAttribute("list", restaurantList);
        model.addAttribute("paginationBarNumbers", barNumbers);
        return "restaurant/search";
    }

    @GetMapping("/category/{category}")
    public String restaurantCategory(
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC, page = 1)
            Pageable pageable,
            @RequestParam(required = false, value = "order", defaultValue = "review") String order,
            @PathVariable(name = "category") String category, Model model,
            @AuthenticationPrincipal BoardPrincipal principal) {
        Page<Restaurant> restaurantPage = restaurantService.categoryPaging(category, pageable, order);
        List<Integer> barNumbers = paginationService.getPaginationBarNumbers(
                pageable.getPageNumber(), restaurantPage.getTotalPages());
        model.addAttribute("order", order);
        model.addAttribute("principal", principal);
        model.addAttribute("paginationBarNumbers", barNumbers);
        model.addAttribute("category", category);
        model.addAttribute("list", restaurantPage);
        return "restaurant/paging";
    }



    @GetMapping("/{id}")
    public String RestaurantDetail(
            @PathVariable Long id,
            @AuthenticationPrincipal BoardPrincipal principal,
            Model model) {
        Restaurant restaurant = restaurantService.findById(id); // 해당 {id} 음식점 정보를 가져옴
        if (principal != null) {
            int wishListChk = wishListService.wishListCheck(principal.getUsername(), restaurant.getId());
            model.addAttribute("wishListChk", wishListChk);
        }
        Page<Review> reviewPages = reviewService.findByRestaurantId(id, PageRequest.of(0, 4, Sort.by(Sort.Direction.DESC, "createdAt"))); // 해당 {id} 음식점의 리뷰 객체를 모두 가져옴
        HashMap<Long, List<String>> recommend = reviewService.changeRecommend(id); // 리뷰의 추천 버튼들을 가져옴
        HashMap<String, Member> members = memberService.getByMemberIdList(id);
        if (!recommend.isEmpty()) {
            ReviewSummaryDto overallRecommend = reviewService.getOverallRecommend(id);
            model.addAttribute("overall", overallRecommend);
        }
        List<Integer> barNumbers = paginationService.getPaginationBarNumbers(0, reviewPages.getTotalPages());
        model.addAttribute("reviews", reviewPages);
        model.addAttribute("recommend", recommend);
        model.addAttribute("restaurant", restaurant);
        model.addAttribute("principal", principal);
        model.addAttribute("members", members);
        model.addAttribute("paginationBarNumbers", barNumbers);
        return "restaurant/detail";
    }

}
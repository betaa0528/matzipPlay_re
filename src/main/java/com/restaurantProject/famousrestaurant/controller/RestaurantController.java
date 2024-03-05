package com.restaurantProject.famousrestaurant.controller;

import com.restaurantProject.famousrestaurant.dto.Member;
import com.restaurantProject.famousrestaurant.dto.Restaurant;
import com.restaurantProject.famousrestaurant.dto.Review;
import com.restaurantProject.famousrestaurant.dto.security.BoardPrincipal;
import com.restaurantProject.famousrestaurant.service.MemberService;
import com.restaurantProject.famousrestaurant.service.RestaurantService;
import com.restaurantProject.famousrestaurant.service.ReviewService;
import com.restaurantProject.famousrestaurant.service.WishListService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.Arrays;
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



    @GetMapping
    public String index(@AuthenticationPrincipal BoardPrincipal principal, Model model) {
        model.addAttribute("principal", principal);
        return "index";
    }

    @GetMapping("/search")
    public String searchRestaurant(
            @RequestParam("keyword") String keyword,
            @PageableDefault(page = 1) Pageable pageable,
            Model model) {
        Page<Restaurant> restaurantList = restaurantService.search(keyword, pageable);
        int blockLimit = 3;
        int startPage = (((int) (Math.ceil((double) pageable.getPageNumber() / blockLimit))) - 1) * blockLimit + 1; // 1 4 7 10 ~~
        int endPage = Math.min((startPage + blockLimit - 1), restaurantList.getTotalPages());
        model.addAttribute("keyword", keyword);
        model.addAttribute("list", restaurantList);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
//        model.addAttribute("session", session.getAttribute("memberId"));
//        log.info(String.valueOf(restaurantList.get(0).getDistance()));
        return "search";
    }

    @GetMapping("/category/{category}/paging")
    public String restaurantCategory(
            @PageableDefault(page = 1) Pageable pageable,
            @PathVariable(name = "category") String category, Model model, HttpSession session) {
//        session.setAttribute("memberId", memberService.getId(1L));
//        Member member = memberService.getByMemberId(session.getAttribute("memberId"));
        Page<Restaurant> restaurantsList = restaurantService.categoryPaging(category, pageable);
        int blockLimit = 3;
        int startPage = (((int) (Math.ceil((double) pageable.getPageNumber() / blockLimit))) - 1) * blockLimit + 1; // 1 4 7 10 ~~
        int endPage = Math.min((startPage + blockLimit - 1), restaurantsList.getTotalPages());
//        log.info("list.first : " + restaurantsList.isFirst());
        model.addAttribute("category", category);
        model.addAttribute("list", restaurantsList);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("session", session.getAttribute("memberId"));
//        model.addAttribute("restaurantList", restaurantService.findByCategory(category));
        return "paging";
    }


    @GetMapping("/detail/{id}")
    public String RestaurantDetail(@PathVariable Long id, Model model, @AuthenticationPrincipal BoardPrincipal principal) {
        Restaurant restaurant = restaurantService.findById(id); // 해당 {id} 음식점 정보를 가져옴
        if(principal != null) {
            int wishListChk = wishListService.wishListCheck(principal.getUsername(), restaurant.getId());
            model.addAttribute("wishListChk", wishListChk);
        }
        List<Review> reviews = reviewService.findByRestaurantId(id); // 해당 {id} 음식점의 리뷰 객체를 모두 가져옴
        HashMap<Long, List<String>> recommend = reviewService.changeRecommend(reviews); // 리뷰의 추천 버튼들을 가져옴
        HashMap<String, Member> members = memberService.getByMemberIdList(reviews); //

        model.addAttribute("reviews", reviews);
        model.addAttribute("recommend", recommend);
        model.addAttribute("restaurant", restaurant);
        model.addAttribute("principal", principal);
        model.addAttribute("members", members);
        return "detail";
    }

}
package com.restaurantProject.famousrestaurant.controller;

import com.restaurantProject.famousrestaurant.dto.Member;
import com.restaurantProject.famousrestaurant.dto.Restaurant;
import com.restaurantProject.famousrestaurant.dto.Review;
import com.restaurantProject.famousrestaurant.service.MemberService;
import com.restaurantProject.famousrestaurant.service.RestaurantService;
import com.restaurantProject.famousrestaurant.service.ReviewService;
import com.restaurantProject.famousrestaurant.service.WishListService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
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

//    @GetMapping("/category")
//    public String categoryPage(HttpServletRequest request) throws InterruptedException {
//        HttpSession session = request.getSession();
//        Member testMember = getTestMember();
//        session.setAttribute("memberPoint", new GeoPoint(testMember.getMapX(), testMember.getMapY()));
//        restaurantService.saveFindCategory(session);
//
//        return "category";
//    }

    @GetMapping("/search/{target}")
    public String searchRestaurant(@PathVariable String target, Model model) {
//        log.info(target);
//        model.addAttribute("target", target);
        List<Restaurant> restaurantList = restaurantService.search(target);
        model.addAttribute("list", restaurantList);
        return "search";
    }
    @GetMapping("/category/{category}/paging")
    public String restaurantCategory(
            @PageableDefault(page = 1) Pageable pageable,
            @PathVariable(name = "category") String category, Model model, HttpServletRequest request) {
        HttpSession session = request.getSession();

        Page<Restaurant> restaurantsList = restaurantService.categoryPaging(category, pageable);
        int blockLimit = 3;
        int startPage = (((int)(Math.ceil((double)pageable.getPageNumber() / blockLimit))) - 1) * blockLimit + 1; // 1 4 7 10 ~~
        int endPage = Math.min((startPage + blockLimit - 1), restaurantsList.getTotalPages());
        model.addAttribute("category", category);
        model.addAttribute("list" , restaurantsList);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
//        model.addAttribute("restaurantList", restaurantService.findByCategory(category));
        return "paging";
    }

    @GetMapping("/list")
    public String RestaurantList(Model model, HttpServletRequest request) {
        List<Restaurant> list = restaurantService.findAll();
        model.addAttribute("restaurantlist" , list);
        return "restaurantList";
    }

    @GetMapping("/detail/{id}")
    public String RestaurantDetail(@PageableDefault(page = 1)Pageable pageable,
                                   @PathVariable Long id, Model model, HttpServletRequest request) {
        HttpSession session = request.getSession();
        Member testMember = getTestMember();
        session.setAttribute("memberId" , testMember.getMemberId());
        Restaurant restaurant = restaurantService.findById(id); // 해당 {id} 음식점 정보를 가져옴
        int wishListChk = 0;
        if(session.getAttribute("memberId") != null){
            wishListChk = wishListService.wishListCheck(session.getAttribute("memberId").toString(), id);
        }
        String query = restaurant.getRestaurantName(); // naver API 에서 해당 {id} 음식점의 사진을 가져오는 검색어
        List<Review> reviews = reviewService.findAll(id); // 해당 {id} 음식점의 리뷰 객체를 모두 가져옴
        model.addAttribute("page", pageable.getPageNumber());
        model.addAttribute("wishListChk", wishListChk);
        model.addAttribute("reviews", reviews);
        model.addAttribute("image", restaurantService.imgSearch(query));
        model.addAttribute("restaurant", restaurant);
        return "detail";
    }

    public Member getTestMember(){
        return memberService.findById(1L);
    }

}

package com.restaurantProject.famousrestaurant.controller;

import com.restaurantProject.famousrestaurant.dto.Restaurant;
import com.restaurantProject.famousrestaurant.dto.Review;
import com.restaurantProject.famousrestaurant.dto.WishList;
import com.restaurantProject.famousrestaurant.entity.RestaurantEntity;
import com.restaurantProject.famousrestaurant.entity.ReviewEntity;
import com.restaurantProject.famousrestaurant.repository.RestaurantRepository;
import com.restaurantProject.famousrestaurant.service.RestaurantService;
import com.restaurantProject.famousrestaurant.service.ReviewService;
import com.restaurantProject.famousrestaurant.service.WishListService;
import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionContext;
import java.util.Enumeration;
import java.util.List;

@Controller
@RequestMapping("/restaurant")
@RequiredArgsConstructor
public class RestaurantController {

    private final RestaurantService restaurantService;
    private final ReviewService reviewService;
    private final WishListService wishListService;

    @GetMapping("/category/{category}/paging")
    public String restaurantCategory(
            @PageableDefault(page = 1) Pageable pageable,
            @PathVariable(name = "category") String category, Model model) {
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
        session.setAttribute("memberId" , "user1");
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
        model.addAttribute("image", restaurantService.search(query));
        model.addAttribute("restaurant", restaurant);
        return "detail";
    }

//    @GetMapping("/paging")
//    public String paging(@PageableDefault(page = 1) Pageable pageable, Model model) {
////        pageable.getPageNumber();
//
//        Page<Restaurant> restaurantsList = restaurantService.paging(pageable);
//        int blockLimit = 3;
//        int startPage = (((int)(Math.ceil((double)pageable.getPageNumber() / blockLimit))) - 1) * blockLimit + 1; // 1 4 7 10 ~~
//        int endPage = Math.min((startPage + blockLimit - 1), restaurantsList.getTotalPages());
//
//        model.addAttribute("list" , restaurantsList);
//        model.addAttribute("startPage", startPage);
//        model.addAttribute("endPage", endPage);
//
//        return "paging";
//
//
//    }


}

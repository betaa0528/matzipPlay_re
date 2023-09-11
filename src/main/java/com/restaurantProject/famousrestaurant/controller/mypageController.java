package com.restaurantProject.famousrestaurant.controller;

import com.restaurantProject.famousrestaurant.entity.MemberEntity;
import com.restaurantProject.famousrestaurant.entity.ReviewEntity;
import com.restaurantProject.famousrestaurant.entity.WishListEntity;
import com.restaurantProject.famousrestaurant.repository.MemberRepository;
import com.restaurantProject.famousrestaurant.repository.RestaurantRepository;
import com.restaurantProject.famousrestaurant.repository.ReviewRepository;
import com.restaurantProject.famousrestaurant.repository.WishListRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.net.ssl.HandshakeCompletedEvent;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@Slf4j
@RequiredArgsConstructor
public class mypageController {
    private final RestaurantRepository restaurantRepository;
    private final MemberRepository memberRepository;
    private final WishListRepository wishListRepository;
    private final ReviewRepository reviewRepository;
    private String memberId;
    HttpSession session;
    @GetMapping("/mypage")
    public String getMypage(Model model,HttpServletRequest session){
        //아이디 확인
        session.setAttribute("sessionId","hr1234");
        String sessionId = (String)session.getAttribute("sessionId");
        MemberEntity member = memberRepository.findByMemberId(sessionId);
        if(member.getMemberId()!=null){
            memberId = member.getMemberId();
            model.addAttribute("id",memberId);
        }else{
            return "error";
        }

        //리뷰목록 가져오기
        List<ReviewEntity> reviewList = (List<ReviewEntity>) reviewRepository.findByMemberId(memberId);
        model.addAttribute("reviewList",reviewList);

        return "mypage";
    }

    @GetMapping("/mypage/wishlist")
    public String getWishList(Model model, HttpServletRequest session){
        session.setAttribute("sessionId","hr1234");
        String sessionId = (String)session.getAttribute("sessionId");
        MemberEntity member = memberRepository.findByMemberId(sessionId);
        if(member.getMemberId()!=null){
            memberId = member.getMemberId();
            model.addAttribute("id",memberId);
        }else{
            return "error";
        }

        //찜목록 가져오기
        List<WishListEntity> wishList = (List<WishListEntity>) wishListRepository.findByMemberId(memberId);
        model.addAttribute("wishList",wishList);
        return "wishlist";
    }
}

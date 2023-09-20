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
import java.util.Optional;

@Controller
@Slf4j
@RequiredArgsConstructor
public class mypageController {

    private final MemberRepository memberRepository;
    private final WishListRepository wishListRepository;
    private final ReviewRepository reviewRepository;

    @GetMapping("mypage")
    public String getMypage(Model model,HttpSession session){

        //아이디 확인
        String sessionId = (String)session.getAttribute("memberId");
        System.out.println(sessionId);

        Optional<MemberEntity> member = memberRepository.findByMemberId(sessionId);

        if(member.get().getMemberId()!=null){
            String memberId = member.get().getMemberId();
            model.addAttribute("id",memberId);

            //리뷰목록 가져오기
            List<ReviewEntity> reviewList = reviewRepository.findByMemberId(memberId);
            model.addAttribute("reviewList",reviewList);
            return "mypage";
        }else{
            return "error";
        }

    }

    @GetMapping("/mypage/wishlist")
    public String getWishList(Model model, HttpSession session){
        String sessionId = (String)session.getAttribute("memberId");
        Optional<MemberEntity> member = memberRepository.findByMemberId(sessionId);
        if(member.get().getMemberId()!=null){
            String memberId = member.get().getMemberId();
            model.addAttribute("id",memberId);

            //찜목록 가져오기
            List<WishListEntity> wishList = wishListRepository.findByMemberWishId(memberId);
            model.addAttribute("wishList",wishList);
            return "wishlist";
        }else{
            return "error";
        }


    }
}

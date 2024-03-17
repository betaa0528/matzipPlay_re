package com.restaurantProject.famousrestaurant.controller;

import com.querydsl.core.types.Order;
import com.restaurantProject.famousrestaurant.dto.Member;
import com.restaurantProject.famousrestaurant.dto.Review;
import com.restaurantProject.famousrestaurant.dto.WishList;
import com.restaurantProject.famousrestaurant.dto.security.BoardPrincipal;
import com.restaurantProject.famousrestaurant.entity.MemberEntity;
import com.restaurantProject.famousrestaurant.entity.ReviewEntity;
import com.restaurantProject.famousrestaurant.entity.WishListEntity;
import com.restaurantProject.famousrestaurant.repository.MemberRepository;
import com.restaurantProject.famousrestaurant.service.MemberService;
import com.restaurantProject.famousrestaurant.service.MyPageService;
import com.restaurantProject.famousrestaurant.service.PaginationService;
import com.restaurantProject.famousrestaurant.service.ReviewService;
import lombok.RequiredArgsConstructor;
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
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/mypage")
@Controller
public class mypageController {

    private final MyPageService myPageService;
    private final MemberService memberService;
    private final MemberRepository memberRepository;
    private final PaginationService paginationService;
    private final ReviewService reviewService;


    @GetMapping
    public String getMypage(
            @AuthenticationPrincipal BoardPrincipal boardPrincipal,
            Model model) throws IOException {
        //아이디 확인
        String memberName = boardPrincipal.getUsername();
        MemberEntity member = memberRepository.getByMemberId(memberName);

        model.addAttribute("member", member);

        //리뷰목록 가져오기
        List<Review> reviewList = myPageService.getMyReview(memberName);
        model.addAttribute("reviews", reviewList);
        model.addAttribute("reviewCount", reviewList.size());

        //찜목록 가져오기
        List<WishList> wishList = myPageService.getMyWish(member.getMemberId());
        //찜목록 이미지 담을 리스트
        List<String> image = myPageService.getMyWishImage(wishList);

        model.addAttribute("wishList", wishList);
        model.addAttribute("image", image);
        model.addAttribute("wishCount", wishList.size());
        return "mypage";
    }


    @PostMapping("/review/{id}")
    public boolean reviewDelete(@PathVariable Long id, @AuthenticationPrincipal BoardPrincipal principal) {
        myPageService.reviewDelete(id);
        return true;
    }

    @PostMapping("/wish/{id}")
    public boolean wishListDelete(@PathVariable Long id, @AuthenticationPrincipal BoardPrincipal principal) {
        return myPageService.wishListDelete(id);
    }

    @PostMapping("/profile")
    @ResponseBody
    public boolean profile(@RequestPart("file") MultipartFile file, @AuthenticationPrincipal BoardPrincipal principal) throws IOException {
        return  myPageService.profileUpload(file, principal.getUsername());
    }

}

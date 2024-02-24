package com.restaurantProject.famousrestaurant.controller;

import com.restaurantProject.famousrestaurant.entity.MemberEntity;
import com.restaurantProject.famousrestaurant.entity.ReviewEntity;
import com.restaurantProject.famousrestaurant.entity.WishListEntity;
import com.restaurantProject.famousrestaurant.service.MemberService;
import com.restaurantProject.famousrestaurant.service.MyPageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/mypage")
@Controller
public class mypageController {

    private final MyPageService myPageService;
    private final MemberService memberService;



    @GetMapping
    public String getMypage(Model model, HttpSession session) throws IOException {
        //아이디 확인
        String sessionId = (String) session.getAttribute("memberId");
        MemberEntity member = memberService.getMember();

        if (member.getMemberId() != null) {
            model.addAttribute("member", member);

            //리뷰목록 가져오기
            List<ReviewEntity> reviewList = myPageService.getMyReview(member.getMemberId());
            model.addAttribute("reviewList", reviewList);
            model.addAttribute("reviewCount", reviewList.size());

            //찜목록 가져오기
            List<WishListEntity> wishList = myPageService.getMyWish(member.getMemberId());
            //찜목록 이미지 담을 리스트
            List<String> image = myPageService.getMyWishImage(wishList);

            model.addAttribute("wishList", wishList);
            model.addAttribute("image", image);
            model.addAttribute("wishCount", wishList.size());
        }
        return "mypage";
    }

    @DeleteMapping("/review/{id}")
    @ResponseBody
    public boolean reviewDelete(@PathVariable Long id) { return myPageService.reviewDelete(id); }

    @DeleteMapping("/wish/{id}")
    @ResponseBody
    public boolean wishListDelete(@PathVariable Long id) { return myPageService.wishListDelete(id); }

//    @PutMapping("/profile/{memberId}")
//    @ResponseBody
//    public boolean profile(@RequestPart("file") MultipartFile file, @PathVariable String memberId) throws IOException {
//        return myPageService.profileUpload(file, memberId);
//    }

}

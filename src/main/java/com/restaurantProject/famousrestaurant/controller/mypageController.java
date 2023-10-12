package com.restaurantProject.famousrestaurant.controller;

import com.restaurantProject.famousrestaurant.entity.MemberEntity;
import com.restaurantProject.famousrestaurant.entity.ReviewEntity;
import com.restaurantProject.famousrestaurant.entity.WishListEntity;
import com.restaurantProject.famousrestaurant.service.MyPageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Controller
public class mypageController {

    private final MyPageService myPageService;

    @GetMapping("mypage")
    public String getMypage(Model model, HttpSession session) {
        //아이디 확인
        String sessionId = (String) session.getAttribute("memberId");
        Optional<MemberEntity> member = myPageService.findByMemberId(sessionId);

        if (member.get().getMemberId() != null) {
            model.addAttribute("member", member.get());

            //리뷰목록 가져오기
            List<ReviewEntity> reviewList = myPageService.getMyRivew(member.get().getMemberId());
            model.addAttribute("reviewList", reviewList);
            model.addAttribute("reviewCount", reviewList.size());

            //찜목록 가져오기
            List<WishListEntity> wishList = myPageService.getMyWish(member.get().getMemberId());
            //찜목록 이미지 담을 리스트
            List<String> image = myPageService.getMyWishImage(wishList);

            model.addAttribute("wishList", wishList);
            model.addAttribute("image", image);
            model.addAttribute("wishCount", wishList.size());
        }
        return "mypage";
    }

    @DeleteMapping("mypage/review/{id}")
    @ResponseBody
    public boolean reviewDelete(@PathVariable Long id) { return myPageService.reviewDelete(id); }

    @DeleteMapping("mypage/wish/{id}")
    @ResponseBody
    public boolean wishListDelete(@PathVariable Long id) { return myPageService.wishListDelete(id); }

    @PutMapping("mypage/profile/{memberId}")
    @ResponseBody
    public boolean profile(@RequestPart("file") MultipartFile file, @PathVariable String memberId) { return myPageService.profileUpload(file, memberId); }

}

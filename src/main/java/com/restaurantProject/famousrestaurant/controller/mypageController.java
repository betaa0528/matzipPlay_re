package com.restaurantProject.famousrestaurant.controller;

import com.restaurantProject.famousrestaurant.dto.Member;
import com.restaurantProject.famousrestaurant.dto.Restaurant;
import com.restaurantProject.famousrestaurant.dto.Review;
import com.restaurantProject.famousrestaurant.dto.WishList;
import com.restaurantProject.famousrestaurant.entity.MemberEntity;
import com.restaurantProject.famousrestaurant.entity.ReviewEntity;
import com.restaurantProject.famousrestaurant.entity.WishListEntity;
import com.restaurantProject.famousrestaurant.repository.MemberRepository;
import com.restaurantProject.famousrestaurant.repository.ReviewRepository;
import com.restaurantProject.famousrestaurant.repository.WishListRepository;
import com.restaurantProject.famousrestaurant.service.MyPageService;
import com.restaurantProject.famousrestaurant.service.RestaurantService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@Slf4j
@RequiredArgsConstructor
public class mypageController {

    private final MemberRepository memberRepository;
    private final WishListRepository wishListRepository;
    private final ReviewRepository reviewRepository;
    private final RestaurantService restaurantService;
    private final MyPageService myPageService;

    @GetMapping("mypage")
    public String getMypage(Model model,HttpSession session){

        //찜목록 이미지 담을 리스트
        List<String> image = new ArrayList<>();

        //아이디 확인
        String sessionId = (String)session.getAttribute("memberId");
        Optional<MemberEntity> member = memberRepository.findByMemberId(sessionId);

        if(member.get().getMemberId()!=null) {
            MemberEntity m = member.get();
            model.addAttribute("member", m);

            //리뷰목록 가져오기
            List<ReviewEntity> reviewList = reviewRepository.findByMemberId(m.getMemberId());
            model.addAttribute("reviewList",reviewList);
            model.addAttribute("reviewCount",reviewList.size());

            //찜목록 가져오기
            List<WishListEntity> wishList = wishListRepository.findByMemberWishId(m.getMemberId());

            for(WishListEntity w : wishList){
                Restaurant restaurant = restaurantService.findById(w.getRestaurantId());
                String query = restaurant.getRestaurantName();
                image.add(restaurantService.imgSearch(query));
            }

            model.addAttribute("wishList",wishList);
            model.addAttribute("image",image);
            model.addAttribute("wishCount",wishList.size());

        }
        return "mypage";
    }

    @PostMapping("mypage/reviewDelete")
    public String reviewDelete(Review review){
        Optional<ReviewEntity> r = reviewRepository.findById(review.getId());
        if (r.isPresent()) { // Optional에서 엔티티 추출
            ReviewEntity reviewEntity = r.get();
            reviewRepository.delete(reviewEntity); // 엔티티를 사용하여 삭제
        }
        return "redirect:/mypage";
    }

    @PostMapping("mypage/wishListDelete")
    public String wishListDelete(WishList wishList){
        Optional<WishListEntity> w = wishListRepository.findById(wishList.getId());
        if (w.isPresent()) { // Optional에서 엔티티 추출
            WishListEntity wishListEntity = w.get();
            wishListRepository.delete(wishListEntity); // 엔티티를 사용하여 삭제
        }
        return "redirect:/mypage";
    }

    @PostMapping("mypage/profile")
    public String profile(@RequestParam("file") MultipartFile file , Member member){
        Optional<MemberEntity> memOptional = memberRepository.findByMemberId(member.getMemberId());
        if(memOptional.isPresent()){
            MemberEntity mem = memOptional.get();
            System.out.println(mem.getMemberProfile());
            if(mem.getMemberProfile().equals("default.jpeg")){
                String profile = myPageService.upload(file,"/Users/yun/Desktop/profile/");
                mem.setMemberProfile(profile);
            }else{
                myPageService.delete("/Users/yun/Desktop/profile/",mem.getMemberProfile());
                String profile = myPageService.upload(file,"/Users/yun/Desktop/profile/");
                mem.setMemberProfile(profile);
            }
            memberRepository.save(mem);
        }
        return "redirect:/mypage";
    }
}

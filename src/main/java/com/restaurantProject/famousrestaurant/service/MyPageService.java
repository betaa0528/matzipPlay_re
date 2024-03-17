package com.restaurantProject.famousrestaurant.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.restaurantProject.famousrestaurant.dto.Restaurant;
import com.restaurantProject.famousrestaurant.dto.Review;
import com.restaurantProject.famousrestaurant.dto.WishList;
import com.restaurantProject.famousrestaurant.entity.MemberEntity;
import com.restaurantProject.famousrestaurant.entity.ReviewEntity;
import com.restaurantProject.famousrestaurant.entity.WishListEntity;
import com.restaurantProject.famousrestaurant.repository.MemberRepository;
import com.restaurantProject.famousrestaurant.repository.RestaurantRepository;
import com.restaurantProject.famousrestaurant.repository.ReviewRepository;
import com.restaurantProject.famousrestaurant.repository.WishListRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class MyPageService {

    private final MemberRepository memberRepository;
    private final WishListRepository wishListRepository;
    private final ReviewRepository reviewRepository;
    private final RestaurantService restaurantService;
    private final ReviewService reviewService;
    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    private final String profilePath = "C:/profile/";
    private final RestaurantRepository restaurantRepository;

    private String upload(MultipartFile file) throws IOException {
        if (!file.isEmpty()) {
            String fileRealName = file.getOriginalFilename(); // 파일명을 얻어낼 수 있는 메서드
            String fileExtension = Objects.requireNonNull(fileRealName).substring(fileRealName.lastIndexOf("."));
            UUID uuid = UUID.randomUUID();
            String[] uuids = uuid.toString().split("-");
            String uniqueName = uuids[0];
            String fileName = uniqueName + fileExtension;
            String savePath = profilePath + fileName;
            file.transferTo(new File(savePath));


//            ObjectMetadata metadata = new ObjectMetadata();
//            metadata.setContentLength(file.getSize());
//            metadata.setContentType(file.getContentType());
//            amazonS3.putObject(bucket + "/profile", fileName, file.getInputStream(), metadata);
            return fileName;

        } else {
            return null;
        }
    }

    public void delete(String fileName) {
//        amazonS3.deleteObject(bucket, fileName);
        System.out.println("삭제 완");
    }

    public boolean profileUpload(MultipartFile file, String memberId) throws IOException {
        MemberEntity member = memberRepository.getByMemberId(memberId);
        if (!member.getMemberProfile().equals("default.jpeg")) {
            delete(member.getMemberProfile());
        }
        String profile = upload(file);
        member.setMemberProfile(profile);
        memberRepository.save(member);
        return true;
    }

    public void reviewDelete(Long id) {
        reviewRepository.deleteById(id);
    }

    public boolean wishListDelete(Long id) {
        Optional<WishListEntity> w = wishListRepository.findById(id);
        if (w.isPresent()) { // Optional에서 엔티티 추출
            WishListEntity wishListEntity = w.get();
            wishListRepository.delete(wishListEntity); // 엔티티를 사용하여 삭제
            return true;
        }
        return false;
    }

    public List<Review> getMyReview(String memberId) {
        List<ReviewEntity> reviewEntities = reviewRepository.findAllByMemberId(memberId);
        return reviewEntities.stream().map(Review::from).collect(Collectors.toList());
    }

    public List<WishList> getMyWish(String memberId) {
        List<WishListEntity> entityList = wishListRepository.findByMemberWishId(memberId);
        for(WishListEntity entity : entityList) {
            entity.setRestaurantEntity(restaurantRepository.findById(entity.getRestaurantId()).get());
        }
        return entityList.stream().map(WishList::from).collect(Collectors.toList());
    }

    public List<String> getMyWishImage(List<WishList> wishList) {
        List<String> image = new ArrayList<>();
        for (WishList w : wishList) {
            Restaurant restaurant = restaurantService.findById(w.getRestaurantId());
            String query = restaurant.getRestaurantName();
            image.add(restaurantService.imgSearch(query));
        }
        return image;
    }

}

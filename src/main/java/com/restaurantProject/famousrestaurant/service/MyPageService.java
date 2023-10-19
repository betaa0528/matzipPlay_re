package com.restaurantProject.famousrestaurant.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.restaurantProject.famousrestaurant.dto.Restaurant;
import com.restaurantProject.famousrestaurant.entity.MemberEntity;
import com.restaurantProject.famousrestaurant.entity.ReviewEntity;
import com.restaurantProject.famousrestaurant.entity.WishListEntity;
import com.restaurantProject.famousrestaurant.repository.MemberRepository;
import com.restaurantProject.famousrestaurant.repository.ReviewRepository;
import com.restaurantProject.famousrestaurant.repository.WishListRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@Service
@RequiredArgsConstructor
public class MyPageService {

    private final MemberRepository memberRepository;
    private final WishListRepository wishListRepository;
    private final ReviewRepository reviewRepository;
    private final RestaurantService restaurantService;
    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    private String upload(MultipartFile file) throws IOException {
        if (!file.isEmpty()) {
            String fileRealName = file.getOriginalFilename(); // 파일명을 얻어낼 수 있는 메서드
            String fileExtension = Objects.requireNonNull(fileRealName).substring(fileRealName.lastIndexOf("."));
            UUID uuid = UUID.randomUUID();
            String[] uuids = uuid.toString().split("-");
            String uniqueName = uuids[0];
            String fileName = uniqueName + fileExtension;

            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(file.getSize());
            metadata.setContentType(file.getContentType());
            amazonS3.putObject(bucket+"/profile",fileName,file.getInputStream(),metadata);
            return fileName;

        } else {
            return null;
        }
    }

    public void delete(String fileName) {
        amazonS3.deleteObject(bucket, fileName);
    }

    public boolean profileUpload(MultipartFile file, String memberId) throws IOException {
        Optional<MemberEntity> memOptional = memberRepository.findByMemberId(memberId);
        if (memOptional.isPresent()) {
            MemberEntity mem = memOptional.get();
            if (mem.getMemberProfile().equals("default.jpeg")) {
                String profile = upload(file);
                mem.setMemberProfile(profile);
            } else {
                delete(mem.getMemberProfile());
                String profile = upload(file);
                mem.setMemberProfile(profile);
            }
            memberRepository.save(mem);
            return true;
        }else {
            return false;
        }
    }

    public boolean reviewDelete(Long id) {
        Optional<ReviewEntity> r = reviewRepository.findById(id);
        if (r.isPresent()) { // Optional에서 엔티티 추출
            ReviewEntity reviewEntity = r.get();
            reviewRepository.delete(reviewEntity); // 엔티티를 사용하여 삭제
            return true;
        } else return false;
    }

    public boolean wishListDelete(Long id) {
        Optional<WishListEntity> w = wishListRepository.findById(id);
        if (w.isPresent()) { // Optional에서 엔티티 추출
            WishListEntity wishListEntity = w.get();
            wishListRepository.delete(wishListEntity); // 엔티티를 사용하여 삭제
            return true;
        }return false;
    }

    public List<ReviewEntity> getMyRivew(String memberId) { return reviewRepository.findByMemberId(memberId);}

    public List<WishListEntity> getMyWish(String memberId){ return wishListRepository.findByMemberWishId(memberId); }

    public List<String> getMyWishImage(List<WishListEntity> wishList){
        List<String> image = new ArrayList<>();
        for (WishListEntity w : wishList) {
            Restaurant restaurant = restaurantService.findById(w.getRestaurantId());
            String query = restaurant.getRestaurantName();
            image.add(restaurantService.imgSearch(query));
        }
        return image;
    }

    public Optional<MemberEntity> findByMemberId(String id){
       return memberRepository.findByMemberId(id);
    }
}

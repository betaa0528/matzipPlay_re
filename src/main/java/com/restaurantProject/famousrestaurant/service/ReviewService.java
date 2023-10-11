package com.restaurantProject.famousrestaurant.service;

import com.restaurantProject.famousrestaurant.dto.Review;
import com.restaurantProject.famousrestaurant.entity.RestaurantEntity;
import com.restaurantProject.famousrestaurant.entity.ReviewEntity;
import com.restaurantProject.famousrestaurant.entity.ReviewFileEntity;
import com.restaurantProject.famousrestaurant.repository.RestaurantRepository;
import com.restaurantProject.famousrestaurant.repository.ReviewFileRepository;
import com.restaurantProject.famousrestaurant.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final RestaurantRepository restaurantRepository;
    private final ReviewRepository reviewRepository;
    private final ReviewFileRepository reviewFileRepository;

    public void save(Review review) throws IOException {
        Optional<RestaurantEntity> optionalRestaurantEntity = restaurantRepository.findById(review.getRestaurantId());
        RestaurantEntity restaurantEntity = optionalRestaurantEntity.get();


        if (review.getFileList().get(0).getSize() == 0) {
            ReviewEntity reviewEntity = ReviewEntity.toSaveEntity(review, restaurantEntity);
            reviewRepository.save(reviewEntity);
        } else {
            ReviewEntity reviewEntity = ReviewEntity.toSaveFileEntity(review, restaurantEntity);
            Long savedId = reviewRepository.save(reviewEntity).getId();
            ReviewEntity reviewEntityGetId = reviewRepository.findById(savedId).get();
            for (MultipartFile reviewFile : review.getFileList()) {
                String originalFileName = reviewFile.getOriginalFilename();
                String storedFileName = System.currentTimeMillis() + "_" + originalFileName;
//                String savePath = "/Users/yun/Desktop/review_img/" + storedFileName;
                String savePath = "C:/review_img/" + storedFileName;
                reviewFile.transferTo(new File(savePath));
                ReviewFileEntity reviewFileEntity = ReviewFileEntity.toReviewFileEntity(reviewEntityGetId, originalFileName, storedFileName);
                reviewFileRepository.save(reviewFileEntity);
            }
        }
    }

    public List<Review> findByRestaurantId(Long restaurant_id) {
        RestaurantEntity restaurantEntity = restaurantRepository.findById(restaurant_id).get();
        List<ReviewEntity> reviewEntities = reviewRepository.findAllByRestaurantEntityOrderByIdDesc(restaurantEntity);
        List<Review> reviews = new ArrayList<>();
        for (ReviewEntity reviewEntity : reviewEntities) {
            reviews.add(Review.toReview(reviewEntity, restaurant_id));
        }

        return reviews;
    }

    public HashMap<Long, List<String>> changeRecommend(List<Review> reviews) {
        HashMap<Long, List<String>> recommend = new HashMap<>();
        if (!reviews.isEmpty()) {
            for (Review review : reviews) {
                Long id = review.getId();
                List<String> recValues = new ArrayList<>();
                String[] recommendValues = review.getRecommendValues();
                for (String value : recommendValues) {
                    switch (value) {
                        case "1":
                            recValues.add("음식이 맛있어요");
                            break;
                        case "2":
                            recValues.add("인테리어가 멋져요");
                            break;
                        case "3":
                            recValues.add("친절해요");
                            break;
                        case "4":
                            recValues.add("매장이 청결해요");
                            break;
                        case "5":
                            recValues.add("음악이 좋아요");
                            break;
                        case "6":
                            recValues.add("술이 다양해요");
                            break;
                        case "7":
                            recValues.add("특별한 메뉴가 있어요");
                            break;
                        case "8":
                            recValues.add("가성비가 좋아요");
                            break;
                        case "9":
                            recValues.add("단체모임 하기 좋아요");
                            break;
                        case "10":
                            recValues.add("대화하기 좋아요");
                            break;
                        case "11":
                            recValues.add("화장실이 깨끗해요");
                            break;
                        case "12":
                            recValues.add("혼밥하기 좋아요");
                            break;
                        case "13":
                            recValues.add("양이 많아요");
                            break;
                    }
                }
                recommend.put(id, recValues);
            }
        }
        return recommend;
    }
}

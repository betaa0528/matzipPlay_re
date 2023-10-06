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


        if(review.getFileList().get(0).getSize() == 0) {
            ReviewEntity reviewEntity = ReviewEntity.toSaveEntity(review, restaurantEntity);
            reviewRepository.save(reviewEntity);
        } else {
            ReviewEntity reviewEntity = ReviewEntity.toSaveFileEntity(review, restaurantEntity);
            Long savedId = reviewRepository.save(reviewEntity).getId();
            ReviewEntity reviewEntityGetId = reviewRepository.findById(savedId).get();
            for(MultipartFile reviewFile : review.getFileList()) {
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
        for(ReviewEntity reviewEntity : reviewEntities){
            reviews.add(Review.toReview(reviewEntity , restaurant_id));
        }

        return reviews;
    }
}

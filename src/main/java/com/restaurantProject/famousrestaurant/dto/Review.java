package com.restaurantProject.famousrestaurant.dto;

import com.restaurantProject.famousrestaurant.entity.ReviewEntity;
import com.restaurantProject.famousrestaurant.entity.ReviewFileEntity;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Review {
    private Long id;
    private String memberId;
    private String reviewText;
    private String createdAt;
    private String updatedAt;
    private Long restaurantId;
    private String restaurantName;
    private List<MultipartFile> fileList; // 파일 담는 용도
    private List<String> originalName; // 원본 파일 이름
    private List<String> storedName; // 서버 저장 파일이름
    private int fileAttached; // 파일 첨부 여부
    private String[] recommendValues;

    public Review(String memberId, String reviewText, String createdAt, Long restaurantId) {
        this.memberId = memberId;
        this.reviewText = reviewText;
        this.createdAt = createdAt;
        this.restaurantId = restaurantId;
    }

    public static Review toReview(ReviewEntity reviewEntity, Long restaurantId) {
        Review review = new Review();
        review.setId(reviewEntity.getId());
        review.setMemberId(reviewEntity.getMemberId());
        review.setReviewText(reviewEntity.getReviewText());
        review.setCreatedAt(String.valueOf(reviewEntity.getCreatedAt().toLocalDate()));
        if(reviewEntity.getUpdatedAt() != null){
            review.setUpdatedAt(String.valueOf(reviewEntity.getUpdatedAt().toLocalDate()));
        }
//        review.setCreatedAt(reviewEntity.getCreatedAt());
        review.setRestaurantId(restaurantId);
        review.setRestaurantName(reviewEntity.getRestaurantEntity().getRestaurantName());
        review.setRecommendValues(reviewEntity.getRecommendValues().split(","));
        if(reviewEntity.getFileAttached() == 0){
            review.setFileAttached(reviewEntity.getFileAttached());
        } else {
            List<String> originalFileNameList = new ArrayList<>();
            List<String> storedFileNameList = new ArrayList<>();

            review.setFileAttached(reviewEntity.getFileAttached());

            for(ReviewFileEntity reviewFileEntity : reviewEntity.getReviewFileEntity()) {
                originalFileNameList.add(reviewFileEntity.getOriginalName());
                storedFileNameList.add(reviewFileEntity.getStoredName());
            }

            review.setOriginalName(originalFileNameList);
            review.setStoredName(storedFileNameList);
        }
        return review;
    }

    public static Review from(ReviewEntity entity) {
        return Review.builder()
                .id(entity.getId())
                .memberId(entity.getMemberId())
                .reviewText(entity.getReviewText())
                .fileAttached(entity.getFileAttached())
                .createdAt(String.valueOf(entity.getCreatedAt()))
                .storedName(entity.getReviewFileEntity().stream().map(ReviewFileEntity::getStoredName).collect(Collectors.toList()))
                .restaurantId(entity.getRestaurantEntity().getId())
                .restaurantName(entity.getRestaurantEntity().getRestaurantName())
                .recommendValues(entity.getRecommendValues().split(","))
                .build();

    }
}

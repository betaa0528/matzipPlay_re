package com.restaurantProject.famousrestaurant.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.restaurantProject.famousrestaurant.dto.Restaurant;
import com.restaurantProject.famousrestaurant.dto.Review;
import com.restaurantProject.famousrestaurant.dto.ReviewSummaryDto;
import com.restaurantProject.famousrestaurant.dto.ReviewUpdate;
import com.restaurantProject.famousrestaurant.entity.RestaurantEntity;
import com.restaurantProject.famousrestaurant.entity.ReviewEntity;
import com.restaurantProject.famousrestaurant.entity.ReviewFileEntity;
import com.restaurantProject.famousrestaurant.repository.RestaurantRepository;
import com.restaurantProject.famousrestaurant.repository.ReviewFileRepository;
import com.restaurantProject.famousrestaurant.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final RestaurantRepository restaurantRepository;
    private final ReviewRepository reviewRepository;
    private final ReviewFileRepository reviewFileRepository;
    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    private final String uploadPath = "C:/review_img/";

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
//                String savePath = realPath.realPath()+"review_img/" + storedFileName;
                String savePath = uploadPath + storedFileName;
                reviewFile.transferTo(new File(savePath));

                /* amazon S3 bucket 저장 */
//                ObjectMetadata metadata = new ObjectMetadata();
//                metadata.setContentLength(reviewFile.getSize());
//                metadata.setContentType(reviewFile.getContentType());
//
//                amazonS3.putObject(bucket+"/review_img",storedFileName,reviewFile.getInputStream(),metadata);
//
//
                ReviewFileEntity reviewFileEntity = ReviewFileEntity.toReviewFileEntity(reviewEntityGetId, originalFileName, storedFileName);
                reviewFileRepository.save(reviewFileEntity);
            }
        }
    }

    public Page<Review> findByRestaurantId(Long restaurantId, Pageable pageable) {
//        int page = pageable.getPageNumber() - 1;
//        int pageLimit = 4;
        RestaurantEntity restaurantEntity = restaurantRepository.findById(restaurantId).get();
        Page<ReviewEntity> reviewEntities = reviewRepository.findAllByRestaurantEntityOrderByIdDesc(restaurantEntity, pageable);

        return reviewEntities.map(entity -> Review.toReview(entity, restaurantId));
    }

    public ReviewSummaryDto getOverallRecommend(Long id) {
        List<Review> reviews = findByRestaurantIdList(id);
        HashMap<String, Integer> hashMap = new HashMap<>();
        for (Review review : reviews) {
            String[] recommends = review.getRecommendValues();
            for (String recommend : recommends) {
                hashMap.put(recommend, hashMap.getOrDefault(recommend, 0) + 1);
            }
        }

        Comparator<? super Map.Entry<String, Integer>> Comparator = new Comparator<Map.Entry<String, Integer>>() {
            @Override
            public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
                return o1.getValue().compareTo(o2.getValue());
            }
        };

        Map.Entry<String, Integer> max = Collections.max(hashMap.entrySet(), Comparator);
        HashMap<String, String> codeAndStr = new HashMap<>();
        List<String> list = new ArrayList<>(hashMap.keySet());
        list.sort(new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return hashMap.get(o2).compareTo(hashMap.get(o1));
            }
        });

        for(String str : list) {
            switch (str) {
                case "1":
                    codeAndStr.put(str, "음식이 맛있어요");
                    break;
                case "2":
                    codeAndStr.put(str, "인테리어가 멋져요");
                    break;
                case "3":
                    codeAndStr.put(str, "친절해요");
                    break;
                case "4":
                    codeAndStr.put(str, "매장이 청결해요");
                    break;
                case "5":
                    codeAndStr.put(str, "음악이 좋아요");
                    break;
                case "6":
                    codeAndStr.put(str, "술이 다양해요");
                    break;
                case "7":
                    codeAndStr.put(str, "특별한 메뉴가 있어요");
                    break;
                case "8":
                    codeAndStr.put(str, "가성비가 좋아요");
                    break;
                case "9":
                    codeAndStr.put(str, "단체모임 하기 좋아요");
                    break;
                case "10":
                    codeAndStr.put(str, "대화하기 좋아요");
                    break;
                case "11":
                    codeAndStr.put(str, "화장실이 깨끗해요");
                    break;
                case "12":
                    codeAndStr.put(str, "혼밥하기 좋아요");
                    break;
                case "13":
                    codeAndStr.put(str, "양이 많아요");
                    break;
            }
        }
        return new ReviewSummaryDto(
                max.getKey(),
                max.getValue(),
                hashMap,
                codeAndStr,
                list);
    }

    public HashMap<Long, List<String>> changeRecommend(Long restId) {
        List<Review> reviews = findByRestaurantIdList(restId);
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

    public Review findById(Long id) {
        ReviewEntity reviewEntity = reviewRepository.findById(id).get();
        return Review.toReview(reviewEntity, reviewEntity.getRestaurantEntity().getId());
    }


    @Transactional
    public void update(ReviewUpdate reviewUpdate) throws IOException {
        ReviewEntity reviewEntity = reviewRepository.findById(reviewUpdate.getId()).get();
        String[] deleteFiles = reviewUpdate.getDeleteFiles();
        // 삭제한 이미지들 Review File Entity에서 제거
        for (String deleteFile : deleteFiles) {
            for (ReviewFileEntity reviewFileEntity : reviewEntity.getReviewFileEntity()) {
                if(deleteFile.contains(";")){
                    deleteFile = deleteFile.replace(';', ',');
                }

                if (reviewFileEntity.getStoredName().equals(deleteFile)) {
                    reviewFileRepository.delete(reviewFileEntity);
                }
            }
        }
        Review review = Review.from(reviewEntity);

        review.setReviewText(reviewUpdate.getReviewText());
        review.setRecommendValues(reviewUpdate.getRecommendValues());

        if (reviewUpdate.getFileList().get(0).getSize() == 0) {
            if (review.getStoredName() != null) {
                if (reviewUpdate.getDeleteFiles().length == review.getStoredName().size()) {
                    reviewEntity.setFileAttached(0);
                } else {
                    reviewEntity.setFileAttached(1);
                }
            }
            reviewRepository.save(ReviewEntity.toSaveEntity(review, reviewEntity));
        } else {
            ReviewEntity.toSaveFileEntity(review, reviewEntity);
            Long savedId = reviewRepository.save(reviewEntity).getId();
            ReviewEntity reviewEntityGetId = reviewRepository.findById(savedId).get();
            for (MultipartFile reviewFile : reviewUpdate.getFileList()) {
                String originalFileName = reviewFile.getOriginalFilename();
                String storedFileName = System.currentTimeMillis() + "_" + originalFileName;
                String savePath = uploadPath + storedFileName;
                reviewFile.transferTo(new File(savePath));

                /* amazon S3 bucket 저장 */
//                ObjectMetadata metadata = new ObjectMetadata();
//                metadata.setContentLength(reviewFile.getSize());
//                metadata.setContentType(reviewFile.getContentType());
//
//                amazonS3.putObject(bucket+"/review_img",storedFileName,reviewFile.getInputStream(),metadata);
//
//
                ReviewFileEntity reviewFileEntity = ReviewFileEntity.toReviewFileEntity(reviewEntityGetId, originalFileName, storedFileName);
                reviewFileRepository.save(reviewFileEntity);
            }
        }
    }

    public Page<Review> findAll(Pageable pageable) {
        int page = pageable.getPageNumber() - 1;
        int limit = pageable.getPageSize();
        return reviewRepository.findAll(PageRequest.of(page, limit, Sort.by(Sort.Order.desc("id")))).map(Review::from);
    }

    public Page<Review> findAllMove(Pageable pageable, int move) {
        int page = pageable.getPageNumber() + move - 1;
        int limit = 10;
        return reviewRepository.findAll(PageRequest.of(page, limit)).map(Review::from);
    }

    List<Review> findByRestaurantIdList(Long id) {
        RestaurantEntity restaurant = restaurantRepository.findById(id).get();
        List<ReviewEntity> reviewEntities = reviewRepository.findAllByRestaurantEntity(restaurant);
        List<Review> list = new ArrayList<>();
        for(ReviewEntity reviewEntity : reviewEntities) {
            list.add(Review.from(reviewEntity));
        }

        return list;
    }
}

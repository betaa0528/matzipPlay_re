package com.restaurantProject.famousrestaurant.repository;

import com.restaurantProject.famousrestaurant.dto.Review;
import com.restaurantProject.famousrestaurant.entity.RestaurantEntity;
import com.restaurantProject.famousrestaurant.entity.ReviewEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class ReviewRepositoryTest {

    @Autowired
    private ReviewRepository reviewRepository;
    @Autowired
    private RestaurantRepository restaurantRepository;

    @Test
    public void repositoryTest() {

//        List<RestaurantEntity> restaurantEntities = restaurantRepository.findAll();
//
//        ReviewEntity review1 = new ReviewEntity();
//        review1.setId(1L);
//        review1.setMemberId("user1");
//        review1.setRestaurantEntity(restaurantEntities.get(0));
//        System.out.println(review1);
        LocalDateTime now = LocalDateTime.now();
        String format = now.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
//        LocalDateTime nwer = now.toLocalDate().;
//        System.out.println(nwer);

    }

    @DisplayName("리뷰에 버튼들의 숫자의 갯수를 확인한다")
    @Test
    void givenTestData_whenCountRecommend_thenWorkFine () {
        Optional<RestaurantEntity> restaurantEntity = restaurantRepository.findById(17L);
        System.out.println(restaurantEntity);
//
//        Page<ReviewEntity> reviewEntities = reviewRepository.findAllByRestaurantEntityOrderByIdDesc(restaurantEntity.get());
//        System.out.println(reviewEntities.getTotalElements());

//        HashMap<String, Integer> hashMap = new HashMap<>();
//        for(ReviewEntity review : reviewEntities) {
////            System.out.println("review_id : " + review.getId() + ", member_id" + review.getMemberId() +
////                    ", recommend " + review.getRecommendValues());
//            String[] split = review.getRecommendValues().split(",");
//            for(String sp : split) {
//                hashMap.put(sp, hashMap.getOrDefault(sp, 0) + 1);
//            }
//        }
//
//        System.out.println(hashMap);
//        Comparator<? super Map.Entry<String, Integer>> Comparator = new Comparator<Map.Entry<String, Integer>>() {
//            @Override
//            public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
//                return o1.getValue().compareTo(o2.getValue());
//            }
//        };
//        Map.Entry<String , Integer> maxEntry = Collections.max(hashMap.entrySet(), Comparator);
//        System.out.println(maxEntry.getKey() + ", " +maxEntry.getValue());
    }

}

package com.restaurantProject.famousrestaurant.service;

import com.restaurantProject.famousrestaurant.dto.Review;
import com.restaurantProject.famousrestaurant.entity.ReviewEntity;
import com.restaurantProject.famousrestaurant.repository.ReviewRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class ReviewServiceTest {

    @Autowired
    private ReviewRepository repository;
    @Autowired
    private ReviewService reviewService;
    @Test
    void recommendTest() {
        ReviewEntity all = repository.findById(1L).get();
        System.out.println(all);
    }

    @DisplayName("findByRestaurantId method test")
    @Test
    void givenRestaurantId_whenCountRecommend_thenFineWork() {
//        List<Review> list = reviewService.findByRestaurantId(17L);
//        System.out.println(Arrays.toString(list.get(0).getRecommendValues()));
//        HashMap<String, Integer> hashMap = new HashMap<>();
//        for(Review review : list) {
//            String[] split = review.getRecommendValues();
//            for(String sp : split) {
//                hashMap.put(sp, hashMap.getOrDefault(sp, 0) + 1);
//            }
//        }
//
//        Comparator<? super Map.Entry<String, Integer>> Comparator = new Comparator<Map.Entry<String, Integer>>() {
//            @Override
//            public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
//                return o1.getValue().compareTo(o2.getValue());
//            }
//        };
//        Map.Entry<String , Integer> maxEntry = Collections.max(hashMap.entrySet(), Comparator);
////        System.out.println(maxEntry.getKey() + ", " +maxEntry.getValue());
//
//        assertEquals("11", maxEntry.getKey());
    }
}

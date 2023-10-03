package com.restaurantProject.famousrestaurant.repository;

import com.restaurantProject.famousrestaurant.dto.Review;
import com.restaurantProject.famousrestaurant.entity.RestaurantEntity;
import com.restaurantProject.famousrestaurant.entity.ReviewEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@SpringBootTest
public class ReviewRespositoryTest {

    @Autowired
    private ReviewRepository repository;
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
}

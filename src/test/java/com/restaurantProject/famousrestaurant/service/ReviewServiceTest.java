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

}

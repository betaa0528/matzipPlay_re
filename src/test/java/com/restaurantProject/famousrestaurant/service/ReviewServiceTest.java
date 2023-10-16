package com.restaurantProject.famousrestaurant.service;

import com.restaurantProject.famousrestaurant.entity.ReviewEntity;
import com.restaurantProject.famousrestaurant.repository.ReviewRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

@SpringBootTest
public class ReviewServiceTest {

    @Autowired
    private ReviewRepository repository;
    @Test
    void recommendTest() {
        ReviewEntity all = repository.findById(1L).get();
        System.out.println(all);
    }
}

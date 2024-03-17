package com.restaurantProject.famousrestaurant.service;

import com.querydsl.core.types.Order;
import com.restaurantProject.famousrestaurant.dto.Review;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MyPageServiceTest {

    @Autowired
    private MyPageService myPageService;


}
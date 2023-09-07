package com.restaurantProject.famousrestaurant.service;

import com.restaurantProject.famousrestaurant.naver.NaverClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class NaverServiceTest {

    @Autowired
    private RestaurantService restaurantService;

    @Test
    public void NaverClientTest() {
        String link = restaurantService.search("대치동그집1981");
        System.out.println(link);

    }
}

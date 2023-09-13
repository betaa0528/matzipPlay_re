package com.restaurantProject.famousrestaurant.service;

import com.restaurantProject.famousrestaurant.naver.NaverClient;
import com.restaurantProject.famousrestaurant.naver.dto.SearchImageReq;
import com.restaurantProject.famousrestaurant.naver.dto.SearchImageRes;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class NaverServiceTest {

    @Autowired
    private RestaurantService restaurantService;
    @Autowired
    private NaverClient naverClient;

    @Test
    public void NaverClientTest() {
//        String link = restaurantService.search("호랭이분식");
//        System.out.println(link);

        SearchImageReq searchImageReq = new SearchImageReq();
        searchImageReq.setQuery("호랭이분식");
        SearchImageRes searchImageRes = naverClient.searchImage(searchImageReq);

        System.out.println(searchImageRes);
    }
}

package com.restaurantProject.famousrestaurant.service;

import com.restaurantProject.famousrestaurant.dto.Restaurant;
import com.restaurantProject.famousrestaurant.entity.RestaurantEntity;
import com.restaurantProject.famousrestaurant.naver.NaverClient;
import com.restaurantProject.famousrestaurant.naver.dto.SearchImageReq;
import com.restaurantProject.famousrestaurant.naver.dto.SearchImageRes;
import com.restaurantProject.famousrestaurant.naver.dto.SearchLocalReq;
import com.restaurantProject.famousrestaurant.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;
    private final NaverClient naverClient;

    public List<Restaurant> findAll() {
        List<RestaurantEntity> restaurantEntityList = restaurantRepository.findAll();
        List<Restaurant> restaurants = new ArrayList<>();
        for (RestaurantEntity restaurantEntity : restaurantEntityList) {
            restaurants.add(Restaurant.toRestaurant(restaurantEntity));
        }

        return restaurants;
    }

    public Restaurant findById(Long id) {
        RestaurantEntity restaurantEntity = restaurantRepository.findById(id).get();
        return Restaurant.toRestaurant(restaurantEntity);
    }

    public String search(String query){
        SearchImageReq searchImageReq = new SearchImageReq();
        searchImageReq.setQuery(query);
        SearchImageRes searchImageRes = naverClient.searchImage(searchImageReq);

        SearchImageRes.SearchImageItem searchImageItem = searchImageRes.getItems().stream().findFirst().get();
        String link = searchImageItem.getLink();

        return link;
    }
}

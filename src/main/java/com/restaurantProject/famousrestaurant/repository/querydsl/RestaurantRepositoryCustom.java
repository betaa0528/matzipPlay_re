package com.restaurantProject.famousrestaurant.repository.querydsl;

import com.restaurantProject.famousrestaurant.entity.RestaurantEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface RestaurantRepositoryCustom {

    List<RestaurantEntity> findByRestaurantNameOrAddress(String keyword);

    PageImpl<RestaurantEntity> SearchRestaurantOrderByReview(String keyword, Pageable pageable);
    PageImpl<RestaurantEntity> SearchRestaurantOrderByWish(String keyword, Pageable pageable);
    PageImpl<RestaurantEntity> SearchAddressOrderByReview(String keyword, Pageable pageable);
    PageImpl<RestaurantEntity> SearchAddressOrderByWish(String keyword, Pageable pageable);
}

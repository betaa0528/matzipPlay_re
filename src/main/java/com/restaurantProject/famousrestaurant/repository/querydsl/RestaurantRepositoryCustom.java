package com.restaurantProject.famousrestaurant.repository.querydsl;

import com.restaurantProject.famousrestaurant.entity.RestaurantEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface RestaurantRepositoryCustom {

    List<RestaurantEntity> findByRestaurantNameOrAddress(String keyword);
}

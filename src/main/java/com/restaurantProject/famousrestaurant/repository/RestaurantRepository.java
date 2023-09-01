package com.restaurantProject.famousrestaurant.repository;

import com.restaurantProject.famousrestaurant.entity.RestaurantEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RestaurantRepository extends JpaRepository<RestaurantEntity, Long> {
}

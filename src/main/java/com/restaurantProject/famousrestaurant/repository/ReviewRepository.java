package com.restaurantProject.famousrestaurant.repository;

import com.restaurantProject.famousrestaurant.entity.RestaurantEntity;
import com.restaurantProject.famousrestaurant.entity.ReviewEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<ReviewEntity, Long> {

    List<ReviewEntity> findByMemberId(String memberId);
    List<ReviewEntity> findAllByRestaurantEntityOrderByIdDesc(RestaurantEntity restaurantId);

}

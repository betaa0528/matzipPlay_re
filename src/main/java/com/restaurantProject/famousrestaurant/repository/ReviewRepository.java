package com.restaurantProject.famousrestaurant.repository;

import com.restaurantProject.famousrestaurant.dto.Restaurant;
import com.restaurantProject.famousrestaurant.entity.RestaurantEntity;
import com.restaurantProject.famousrestaurant.entity.ReviewEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<ReviewEntity, Long> {

    List<ReviewEntity> findByMemberId(String memberId);
    Page<ReviewEntity> findAllByRestaurantEntityOrderByIdDesc(RestaurantEntity entity, Pageable pageable);
    List<ReviewEntity> findAllByMemberId(String memberId);
    List<ReviewEntity> findAllByRestaurantEntity(RestaurantEntity restaurantEntity);
}

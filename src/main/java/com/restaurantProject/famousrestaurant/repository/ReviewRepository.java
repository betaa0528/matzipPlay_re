package com.restaurantProject.famousrestaurant.repository;

import com.restaurantProject.famousrestaurant.entity.ReviewEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<ReviewEntity, Long> {
}

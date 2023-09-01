package com.restaurantProject.famousrestaurant.repository;

import com.restaurantProject.famousrestaurant.entity.WishListEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WishListRepository extends JpaRepository<WishListEntity, Long> {
}

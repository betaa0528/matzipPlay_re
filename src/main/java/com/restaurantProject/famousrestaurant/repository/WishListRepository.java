package com.restaurantProject.famousrestaurant.repository;

import com.restaurantProject.famousrestaurant.entity.MemberEntity;
import com.restaurantProject.famousrestaurant.entity.WishListEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WishListRepository extends JpaRepository<WishListEntity, Long> {
    List<WishListEntity> findByMemberId(String memberId);
}

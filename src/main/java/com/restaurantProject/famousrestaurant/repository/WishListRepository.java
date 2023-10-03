package com.restaurantProject.famousrestaurant.repository;

import com.restaurantProject.famousrestaurant.entity.WishListEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WishListRepository extends JpaRepository<WishListEntity, Long> {
    //List<WishListEntity> findByMemberWishId(String user1);

    List<WishListEntity> findByMemberWishId(String memberId);

    WishListEntity findByMemberWishIdAndRestaurantId(String memberWishId, long l);

    List<WishListEntity> findByRestaurantId(long l);
}

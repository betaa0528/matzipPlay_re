package com.restaurantProject.famousrestaurant.repository;

import com.restaurantProject.famousrestaurant.dto.WishList;
import com.restaurantProject.famousrestaurant.entity.WishListEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class WishListRepositoryTest {

    @Autowired
    private WishListRepository repository;

    @Test
    public void getWishListTest(){
        WishListEntity entity1 = new WishListEntity();
        entity1.setId(1L);
        entity1.setMemberWishId("user1");
        entity1.setRestaurantId(1L);
        WishListEntity entity2 = new WishListEntity();
        entity1.setId(2L);
        entity2.setMemberWishId("user1");
        entity2.setRestaurantId(2L);
        repository.save(entity1);
        repository.save(entity2);

//        System.out.println(repository.findAll());
        System.out.println(repository.findByMemberWishIdAndRestaurantId(entity1.getMemberWishId(), 1L)!=null);

    }
}

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
        entity2.setId(2L);
        entity2.setMemberWishId("user2");
        entity2.setRestaurantId(1L);
        WishListEntity entity3 = new WishListEntity();
        entity3.setId(3L);
        entity3.setMemberWishId("user2");
        entity3.setRestaurantId(2L);

        repository.save(entity1);
        repository.save(entity2);
        repository.save(entity3);

        System.out.println(repository.findAll());
//        System.out.println(repository.findByMemberWishIdAndRestaurantId(entity1.getMemberWishId(), 1L)!=null);


        System.out.println(repository.findByRestaurantId(1L).stream().count());

    }
}

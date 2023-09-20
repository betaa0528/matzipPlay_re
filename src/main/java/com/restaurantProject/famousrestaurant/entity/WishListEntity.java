package com.restaurantProject.famousrestaurant.entity;

import com.restaurantProject.famousrestaurant.dto.WishList;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@Entity
@ToString
@Table(name = "wishlist_table")
@AllArgsConstructor
@NoArgsConstructor
public class WishListEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private Long restaurantId;
    @Column
    private String memberWishId;

    public static WishListEntity toWishListEntity(WishList wishList) {
        WishListEntity wishListEntity = new WishListEntity();
        wishListEntity.setRestaurantId(wishList.getRestaurantId());
        wishListEntity.setMemberWishId(wishList.getMemberWishId());
        return wishListEntity;
    }
}

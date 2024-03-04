package com.restaurantProject.famousrestaurant.entity;

import com.restaurantProject.famousrestaurant.dto.WishList;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@Entity
@ToString(exclude = "restaurantEntity")
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "join_id")
    @ToString.Exclude
    private RestaurantEntity restaurantEntity;

    public static WishListEntity toWishListEntity(WishList wishList, RestaurantEntity restaurantEntity) {
        WishListEntity wishListEntity = new WishListEntity();
        wishListEntity.setRestaurantId(wishList.getRestaurantId());
        wishListEntity.setMemberWishId(wishList.getMemberWishId());
        wishListEntity.setRestaurantEntity(restaurantEntity);
        return wishListEntity;
    }

}

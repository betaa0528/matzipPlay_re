package com.restaurantProject.famousrestaurant.dto;

import com.restaurantProject.famousrestaurant.entity.WishListEntity;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WishList {
    private Long id;
    private Long restaurantId;
    private String memberWishId;
    private String restaurantName;
    private String category;
    private String address;

    public WishList(Long restaurantId, String memberWishId) {
        this.restaurantId = restaurantId;
        this.memberWishId = memberWishId;
    }

    public static WishList from(WishListEntity entity) {
        return WishList.builder()
                .id(entity.getId())
                .restaurantId(entity.getRestaurantId())
                .memberWishId(entity.getMemberWishId())
                .restaurantName(entity.getRestaurantEntity().getRestaurantName())
                .category(entity.getRestaurantEntity().getCategory())
                .address(entity.getRestaurantEntity().getRestaurantAddress())
                .build();
    }
}

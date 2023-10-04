package com.restaurantProject.famousrestaurant.dto;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class WishList {
    private Long id;
    private Long restaurantId;
    private String memberWishId;
}
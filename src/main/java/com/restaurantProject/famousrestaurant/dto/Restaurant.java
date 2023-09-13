package com.restaurantProject.famousrestaurant.dto;

import com.restaurantProject.famousrestaurant.entity.RestaurantEntity;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Restaurant {
    private Long id;
    private String restaurantName;
    private String restaurantAddress;
    private String restaurantRoadAddress;
    private String category;
    private double mapX;
    private double mapY;

    public Restaurant(Long id, String restaurantName, String restaurantAddress, String restaurantRoadAddress, String category) {
        this.id = id;
        this.restaurantName = restaurantName;
        this.restaurantAddress = restaurantAddress;
        this.restaurantRoadAddress = restaurantRoadAddress;
        this.category = category;
    }

    public static Restaurant toRestaurant(RestaurantEntity restaurantEntity){
        Restaurant restaurant = new Restaurant();
        restaurant.setId(restaurantEntity.getId());
        restaurant.setRestaurantName(restaurantEntity.getRestaurantName());
        restaurant.setRestaurantAddress(restaurantEntity.getRestaurantAddress());
        restaurant.setRestaurantRoadAddress(restaurantEntity.getRestaurantRoadAddress());
        restaurant.setMapX(restaurantEntity.getMapX());
        restaurant.setMapY(restaurantEntity.getMapY());
        restaurant.setCategory(restaurantEntity.getCategory());
        return restaurant;

    }

}

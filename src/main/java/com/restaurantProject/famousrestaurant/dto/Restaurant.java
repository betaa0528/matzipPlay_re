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
    private String imgLink;
    private String category;
    private int reviewCnt;
    private int wishCnt;
    private double mapX;
    private double mapY;
    private double distance;

    public Restaurant(Long id, String restaurantName, String restaurantAddress, String restaurantRoadAddress, String imgLink, String category, double mapX, double mapY) {
        this.id = id;
        this.restaurantName = restaurantName;
        this.restaurantAddress = restaurantAddress;
        this.restaurantRoadAddress = restaurantRoadAddress;
        this.imgLink = imgLink;
        this.category = category;
        this.mapX = mapX;
        this.mapY = mapY;
    }

    public Restaurant(Long id, String restaurantName, String restaurantAddress, String restaurantRoadAddress, String category) {
        this.id = id;
        this.restaurantName = restaurantName;
        this.restaurantAddress = restaurantAddress;
        this.restaurantRoadAddress = restaurantRoadAddress;
        this.category = category;
    }

    public Restaurant(Long id, String restaurantName, String restaurantAddress, String restaurantRoadAddress, String imgLink, String category) {
        this.id = id;
        this.restaurantName = restaurantName;
        this.restaurantAddress = restaurantAddress;
        this.restaurantRoadAddress = restaurantRoadAddress;
        this.imgLink = imgLink;
        this.category = category;
    }

    public Restaurant(String restaurantName, String restaurantAddress, String restaurantRoadAddress, String category, double mapX, double mapY) {
        this.restaurantName = restaurantName;
        this.restaurantAddress = restaurantAddress;
        this.restaurantRoadAddress = restaurantRoadAddress;
        this.category = category;
        this.mapX = mapX;
        this.mapY = mapY;
    }

    public static Restaurant from(RestaurantEntity restaurantEntity){
        Restaurant restaurant = new Restaurant();
        restaurant.setId(restaurantEntity.getId());
        restaurant.setRestaurantName(restaurantEntity.getRestaurantName());
        restaurant.setRestaurantAddress(restaurantEntity.getRestaurantAddress());
        restaurant.setRestaurantRoadAddress(restaurantEntity.getRestaurantRoadAddress());
        restaurant.setImgLink(restaurantEntity.getImgLink());
//        restaurant.setDistance(restaurantEntity.getDistance());
        restaurant.setDistance(1);
        restaurant.setReviewCnt(restaurantEntity.getReviewEntity().size());
        restaurant.setWishCnt(restaurantEntity.getWishListEntity().size());
        restaurant.setMapX(restaurantEntity.getMapX());
        restaurant.setMapY(restaurantEntity.getMapY());
        restaurant.setCategory(restaurantEntity.getCategory());
        return restaurant;
    }



}

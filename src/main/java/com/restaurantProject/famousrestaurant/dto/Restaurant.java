package com.restaurantProject.famousrestaurant.dto;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Restaurant {
    private String restaurantName;
    private String restaurantAddress;
    private String category;
    private double xCoordinate;
    private double yCoordinate;

}

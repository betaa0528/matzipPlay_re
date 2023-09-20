package com.restaurantProject.famousrestaurant.dto;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Member {
    private String memberId;
    private String memberPass;
    private String memberPhoneNumber;
    private String memberAddress;
    private double mapX;
    private double mapY;

}

package com.restaurantProject.famousrestaurant.dto;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Reservation {
    private String restaurantName;
    private String memberId;
    private int ticket;
}

package com.restaurantProject.famousrestaurant.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Review {
    private String memberId;
    private String restaurantName;
    private String reviewImage;
    private String reviewText;
    private LocalDateTime createdAt;

}

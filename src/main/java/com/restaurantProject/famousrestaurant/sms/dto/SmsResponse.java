package com.restaurantProject.famousrestaurant.sms.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class SmsResponse {
    String requestId;
    LocalDateTime requestTime;
    String statusCode;
    String statusName;
}

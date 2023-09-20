package com.restaurantProject.famousrestaurant.sms.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class smsResponse {
    String requestId;
    LocalDateTime requestTime;
    String statusCode;
    String statusName;
}

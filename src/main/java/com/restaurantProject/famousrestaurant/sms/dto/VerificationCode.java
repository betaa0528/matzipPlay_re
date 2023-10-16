package com.restaurantProject.famousrestaurant.sms.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class VerificationCode {

    private String code;
    private long timestamp;

    public VerificationCode(String code) {
        this.code = code;
        this.timestamp = System.currentTimeMillis();
    }
}

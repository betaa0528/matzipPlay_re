package com.restaurantProject.famousrestaurant.sms.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class SmsRequest {
    String type;
    String contentType;
    String countryCode;
    String from;
    String content;
    List<Message> messages;
}

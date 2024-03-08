package com.restaurantProject.famousrestaurant.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewSummaryDto {
    private String key;
    private Integer value;
    private HashMap<String, Integer> hashMap;
    private HashMap<String, String> codeAndStr;
    private List<String> list;
}

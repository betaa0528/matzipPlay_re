package com.restaurantProject.famousrestaurant.entity.constant;

import lombok.Getter;

@Getter
public enum ArticleType {
    FREE("자유게시판"),
    REQUEST("맛집신청게시판");

    private final String description;

    ArticleType(String description) {
        this.description = description;
    }
}

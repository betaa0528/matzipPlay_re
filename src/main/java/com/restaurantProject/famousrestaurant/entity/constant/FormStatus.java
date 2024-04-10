package com.restaurantProject.famousrestaurant.entity.constant;

import lombok.Getter;

@Getter
public enum FormStatus {
    CREATE("게시글 생성", false),
    UPDATE("게시글 수정", true);

    private final String description;
    private final boolean update;


    FormStatus(String description, boolean update) {
        this.description = description;
        this.update = update;
    }
}

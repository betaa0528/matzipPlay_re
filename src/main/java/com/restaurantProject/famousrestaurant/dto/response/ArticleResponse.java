package com.restaurantProject.famousrestaurant.dto.response;

import com.restaurantProject.famousrestaurant.dto.ArticleDto;
import com.restaurantProject.famousrestaurant.entity.constant.ArticleType;
import lombok.*;

import java.time.LocalDateTime;


@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ArticleResponse {
    private Long id;
    private String title;
    private String content;
    private ArticleType articleType;
    private LocalDateTime createdAt;

    public static ArticleResponse from(ArticleDto dto) {
        return new ArticleResponse(dto.getId(), dto.getTitle(), dto.getContent(), dto.getArticleType(), dto.getCreatedAt());
    }
}

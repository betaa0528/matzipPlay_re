package com.restaurantProject.famousrestaurant.dto.request;

import com.restaurantProject.famousrestaurant.dto.ArticleDto;
import com.restaurantProject.famousrestaurant.dto.Member;
import com.restaurantProject.famousrestaurant.entity.constant.ArticleType;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ArticleRequest {
    private String title;
    private String content;
    private ArticleType articleType;

    public ArticleRequest of(String title, String content, ArticleType articleType) {
        return new ArticleRequest(title, content, articleType);
    }

    public ArticleDto toDto(Member member) {
        return ArticleDto.of(member, title, content, articleType);
    }

}

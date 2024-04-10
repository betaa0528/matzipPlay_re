package com.restaurantProject.famousrestaurant.dto;

import com.restaurantProject.famousrestaurant.entity.Article;
import com.restaurantProject.famousrestaurant.entity.MemberEntity;
import com.restaurantProject.famousrestaurant.entity.constant.ArticleType;
import lombok.*;

import java.time.LocalDateTime;

/**
 * DTO for {@link com.restaurantProject.famousrestaurant.entity.Article}
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ArticleDto {
    Long id;
    String title;
    String content;
    Member member;
    ArticleType articleType;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
    int views;

    public static ArticleDto of(Member member, String title, String content, ArticleType articleType) {
        return new ArticleDto(null, title, content, member, articleType, null, null, 0);
    }

    public static ArticleDto of(Long id, String title, String content, Member member, ArticleType articleType, LocalDateTime createdAt, LocalDateTime updatedAt, int views) {
        return new ArticleDto(id, title, content, member, articleType, createdAt, updatedAt, views);
    }

    public ArticleDto(String title, String content, Member member, ArticleType articleType) {
        this.title = title;
        this.content = content;
        this.member = member;
        this.articleType = articleType;
    }

    public static ArticleDto from(Article entity) {
        return new ArticleDto(
                entity.getId(),
                entity.getTitle(),
                entity.getContent(),
                Member.from(entity.getMember()),
                entity.getArticleType(),
                entity.getCreatedAt(),
                entity.getUpdatedAt(),
                entity.getViews()
        );
    }

    public Article toEntity(MemberEntity member) {
        switch (articleType) {
            case FREE:
                return Article.of(member, title, content, ArticleType.FREE);
            case REQUEST:
                return Article.of(member, title, content, ArticleType.REQUEST);
            default:
                return null;
        }
    }
}
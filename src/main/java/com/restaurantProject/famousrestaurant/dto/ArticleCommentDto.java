package com.restaurantProject.famousrestaurant.dto;

import com.restaurantProject.famousrestaurant.entity.ArticleComment;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * DTO for {@link com.restaurantProject.famousrestaurant.entity.ArticleComment}
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class ArticleCommentDto {
    Long id;
    Long articleId;
    String content;
    String memberId;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;

    public static ArticleCommentDto from(ArticleComment articleComment) {
        return ArticleCommentDto.
                builder()
                .id(articleComment.getId())
                .articleId(articleComment.getArticle().getId())
                .content(articleComment.getContent())
                .memberId(articleComment.getMember().getMemberId())
                .createdAt(articleComment.getCreatedAt())
                .updatedAt(articleComment.getUpdatedAt())
                .build();
    }
}
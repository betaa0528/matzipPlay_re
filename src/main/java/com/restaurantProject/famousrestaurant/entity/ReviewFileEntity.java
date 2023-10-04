package com.restaurantProject.famousrestaurant.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "review_file_table")
public class ReviewFileEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String originalName;
    @Column
    private String storedName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_id")
    private ReviewEntity reviewEntity;

    public static ReviewFileEntity toReviewFileEntity(ReviewEntity reviewEntity, String originalFileName, String storedFileName) {
        ReviewFileEntity reviewFileEntity = new ReviewFileEntity();
        reviewFileEntity.setOriginalName(originalFileName);
        reviewFileEntity.setStoredName(storedFileName);
        reviewFileEntity.setReviewEntity(reviewEntity);
        return reviewFileEntity;
    }
}

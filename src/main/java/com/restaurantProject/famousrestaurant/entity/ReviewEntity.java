package com.restaurantProject.famousrestaurant.entity;

import com.restaurantProject.famousrestaurant.dto.Review;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@ToString
@Table(name = "review_table")
public class ReviewEntity extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String memberId;
    @Column(length = 255)
    private String reviewText;
    @Column
    private int fileAttached; // 1 or 0

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id")
    private RestaurantEntity restaurantEntity;

    @OneToMany(mappedBy = "reviewEntity" , cascade = CascadeType.REMOVE, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<ReviewFileEntity> reviewFileEntity;


    public static ReviewEntity toSaveEntity(Review review, RestaurantEntity restaurantEntity) {
        ReviewEntity reviewEntity = new ReviewEntity();
        reviewEntity.setMemberId(review.getMemberId());
        reviewEntity.setReviewText(review.getReviewText());
        reviewEntity.setRestaurantEntity(restaurantEntity);
        reviewEntity.setFileAttached(0);
        return reviewEntity;
    }

    public static ReviewEntity toSaveFileEntity(Review review, RestaurantEntity restaurantEntity) {
        ReviewEntity reviewEntity = new ReviewEntity();
        reviewEntity.setMemberId(review.getMemberId());
        reviewEntity.setReviewText(review.getReviewText());
        reviewEntity.setRestaurantEntity(restaurantEntity);
        reviewEntity.setFileAttached(1);
        return reviewEntity;
    }
}

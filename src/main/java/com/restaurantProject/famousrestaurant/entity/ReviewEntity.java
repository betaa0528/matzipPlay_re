package com.restaurantProject.famousrestaurant.entity;

import com.restaurantProject.famousrestaurant.dto.Review;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "review_table")
@ToString
public class ReviewEntity extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String memberId;
    @Column(length = 500)
    private String reviewText;
    @Column
    private int fileAttached; // 1 or 0
    @Column
    private String recommendValues;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id")
    @ToString.Exclude
    private RestaurantEntity restaurantEntity;

    @OneToMany(mappedBy = "reviewEntity" , cascade = CascadeType.REMOVE, orphanRemoval = true, fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<ReviewFileEntity> reviewFileEntity;


    public static ReviewEntity toSaveEntity(Review review, RestaurantEntity restaurantEntity) {
        ReviewEntity reviewEntity = new ReviewEntity();
        reviewEntity.setMemberId(review.getMemberId());
        reviewEntity.setReviewText(review.getReviewText());
        reviewEntity.setRestaurantEntity(restaurantEntity);
        reviewEntity.setFileAttached(0);
        reviewEntity.setRecommendValues(RecommendTrans(review.getRecommendValues()));
        return reviewEntity;
    }

    public static ReviewEntity toSaveFileEntity(Review review, RestaurantEntity restaurantEntity) {
        ReviewEntity reviewEntity = new ReviewEntity();
        reviewEntity.setMemberId(review.getMemberId());
        reviewEntity.setReviewText(review.getReviewText());
        reviewEntity.setRestaurantEntity(restaurantEntity);
        reviewEntity.setFileAttached(1);
        reviewEntity.setRecommendValues(RecommendTrans(review.getRecommendValues()));
        return reviewEntity;
    }

    public static String RecommendTrans(String[] recommendValues) {
        StringBuilder stringBuilder = new StringBuilder();
        if(recommendValues != null){
            for(int i = 0 ; i<recommendValues.length; i++) {
                stringBuilder.append(recommendValues[i]);
                if(i != recommendValues.length-1){
                    stringBuilder.append(",");
                }
            }
        }
        return stringBuilder.toString();
//        List<String> recommends = new ArrayList<>();
//        if(recommendValues != null) {
//            recommends.addAll(Arrays.asList(recommendValues));
//        }
//        return recommends;
    }

    public static ReviewEntity toSaveEntity(Review review, ReviewEntity reviewEntity) {
        reviewEntity.setReviewText(review.getReviewText());
        reviewEntity.setRecommendValues(RecommendTrans(review.getRecommendValues()));
        return reviewEntity;
    }

    public static ReviewEntity toSaveFileEntity(Review review, ReviewEntity reviewEntity) {
        reviewEntity.setReviewText(review.getReviewText());
        reviewEntity.setRecommendValues(RecommendTrans(review.getRecommendValues()));
        reviewEntity.setFileAttached(1);
        return reviewEntity;
    }

}

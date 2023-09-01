package com.restaurantProject.famousrestaurant.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@ToString
@Table(name = "review_table")
public class ReviewEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String memberId;
    @Column
    private String restaurantName;
    @Column
    private String reviewImage;
    @Column
    private String reviewText;
    @Column
    private LocalDateTime createdAt;
}

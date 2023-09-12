package com.restaurantProject.famousrestaurant.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@ToString(exclude = "reviewEntity")
@Table(name = "restaurant_table")
public class RestaurantEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String restaurantName;
    @Column
    private String restaurantAddress;
    @Column
    private String restaurantRoadAddress;
    @Column
    private double mapX;
    @Column
    private double mapY;
    @Column
    private String category;

    @OneToMany(mappedBy = "restaurantEntity", cascade = CascadeType.REMOVE, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<ReviewEntity> reviewEntity;


}

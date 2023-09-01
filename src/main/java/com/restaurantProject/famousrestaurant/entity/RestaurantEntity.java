package com.restaurantProject.famousrestaurant.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GeneratorType;

import javax.persistence.*;

@Entity
@Getter
@Setter
@ToString
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
    private double xCoordinate;
    @Column
    private double yCoordinate;
    @Column
    private String category;


}

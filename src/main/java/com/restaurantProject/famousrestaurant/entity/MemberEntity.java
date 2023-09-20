package com.restaurantProject.famousrestaurant.entity;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@ToString
@Table(name = "member_table")
public class MemberEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String memberId;
    @Column
    private String memberPass;
    @Column
    private String memberPhoneNumber;
    @Column
    private String memberAddress;
    @Column
    private double mapX;
    @Column
    private double mapY;

}

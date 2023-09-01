package com.restaurantProject.famousrestaurant.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

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

}

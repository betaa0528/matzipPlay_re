package com.restaurantProject.famousrestaurant.entity;

import jdk.jshell.Snippet;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "member_table")
public class MemberEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String memberId;
    @Column
    private String memberNaverId;
    @Column
    private String memberPass;
    @Column
    private String memberPhoneNumber;
    @Column
    private String memberAddress;
    @Column
    private String memberProfile;
    @Column
    private String mapX;
    @Column
    private String mapY;

}

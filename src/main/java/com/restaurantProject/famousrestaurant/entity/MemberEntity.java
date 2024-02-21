package com.restaurantProject.famousrestaurant.entity;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@ToString(callSuper = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "member_table")
public class MemberEntity extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String memberId;
    @Column
    private String memberNaverId;
    @Column
    private String memberPass;
    @Column(unique = true)
    private String memberPhoneNumber;
    @Column
    private String memberAddress;
    @Column
    private String memberProfile;
    @Column
    private String mapX;
    @Column
    private String mapY;
    private String role;

}

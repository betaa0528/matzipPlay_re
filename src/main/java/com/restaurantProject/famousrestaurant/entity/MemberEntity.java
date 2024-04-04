package com.restaurantProject.famousrestaurant.entity;

import lombok.*;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "member_table")
public class MemberEntity extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "m_id")
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
    @OneToMany(mappedBy = "member", fetch = FetchType.EAGER)
    private Set<Authority> authorities;

    private MemberEntity(String memberId, String memberPass) {
        this.memberId = memberId;
        this.memberPass = memberPass;
        this.role = "USER";
    }

    public static MemberEntity of(String memberId, String memberPass) {
        return new MemberEntity(memberId, memberPass);
    }
}

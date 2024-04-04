package com.restaurantProject.famousrestaurant.dto;

import com.restaurantProject.famousrestaurant.entity.MemberEntity;
import lombok.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Member {

    @NotBlank
    @Size(min = 4, max = 10)
    private String memberId;

    private String memberNaverId;

    @NotBlank
    @Size(min = 8, max = 16)
    private String memberPass;

    @NotBlank
    private String memberPhoneNumber;

    @NotBlank
    private String memberAddress;

    @NotBlank
    private String memberDetailAddr;

    private String memberProfile;
    private String mapX;
    private String mapY;

    public static Member from(MemberEntity entity) {
        Member member = new Member();
        member.setMemberId(entity.getMemberId());
//        member.setMemberNaverId(entity.getMemberNaverId());
        member.setMemberPass(entity.getMemberPass());
//        member.setMemberPhoneNumber(entity.getMemberPhoneNumber());
//        member.setMemberAddress(entity.getMemberAddress());
//        member.setMemberDetailAddr("");
        member.setMemberProfile(entity.getMemberProfile());
//        member.setMapX(entity.getMapX());
//        member.setMapY(entity.getMapY());

        return member;
    }

    public static MemberEntity toEntity(Member member) {
        return MemberEntity.of(member.getMemberId(), member.getMemberPass());
    }
}

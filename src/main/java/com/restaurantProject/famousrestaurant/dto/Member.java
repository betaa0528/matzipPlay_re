package com.restaurantProject.famousrestaurant.dto;

import com.restaurantProject.famousrestaurant.entity.MemberEntity;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Member {

    private String memberId;
    private String memberNaverId;
    private String memberPass;
    private String memberPhoneNumber;
    private String memberAddress;
    private String memberDetailAddr;
    private String memberProfile;
    private String mapX;
    private String mapY;

    public static Member toMember(MemberEntity entity) {
        Member member = new Member();
        member.setMemberId(entity.getMemberId());
        member.setMemberNaverId(entity.getMemberNaverId());
        member.setMemberPass(entity.getMemberPass());
        member.setMemberPhoneNumber(entity.getMemberPhoneNumber());
        member.setMemberAddress(entity.getMemberAddress());
        member.setMemberDetailAddr("");
        member.setMapX(entity.getMapX());
        member.setMapY(entity.getMapY());

        return member;
    }
}

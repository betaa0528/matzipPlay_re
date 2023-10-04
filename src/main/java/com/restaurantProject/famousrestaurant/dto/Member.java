package com.restaurantProject.famousrestaurant.dto;

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

}

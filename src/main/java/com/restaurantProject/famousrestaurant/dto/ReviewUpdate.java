package com.restaurantProject.famousrestaurant.dto;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ReviewUpdate {
    private Long id;
    private String memberId;
    private String reviewText;
    private String createdAt;
    private String updatedAt;
    private Long restaurantId;
    private List<MultipartFile> fileList; // 파일 담는 용도
    private List<String> originalName; // 원본 파일 이름
    private List<String> storedName; // 서버 저장 파일이름
    private int fileAttached; // 파일 첨부 여부
    private String[] recommendValues;
    private String[] deleteFiles;
}

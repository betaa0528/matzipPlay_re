package com.restaurantProject.famousrestaurant.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Service
public class MyPageService {

    public String upload(MultipartFile file, String realPath) {
        if (!file.isEmpty()) {
            String fileRealName = file.getOriginalFilename(); // 파일명을 얻어낼 수 있는 메서드
            long size = file.getSize(); // 파일 사이즈
            String fileExtension = fileRealName.substring(fileRealName.lastIndexOf("."), fileRealName.length());
            String uploadFolder = realPath;
            UUID uuid = UUID.randomUUID();
            String[] uuids = uuid.toString().split("-");
            String uniqueName = uuids[0];
            String filePath = uploadFolder + "/" + uniqueName + fileExtension;

            File saveFile = new File(filePath); // 적용 후

            try {
                file.transferTo(saveFile); // 실제 파일 저장메서드(filewriter 작업을 손쉽게 한방에 처리해준다.)
            } catch (IllegalStateException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            String fileName = uniqueName + fileExtension;

            return fileName;

        } else {
            return null;
        }
    }

    public void delete(String realPath, String fileName) {
        String filePath = realPath + fileName;
        File file = new File(filePath);
        file.delete();
    }

}

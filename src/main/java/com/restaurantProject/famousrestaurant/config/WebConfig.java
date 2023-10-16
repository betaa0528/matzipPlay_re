package com.restaurantProject.famousrestaurant.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.File;
import java.io.IOException;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private String reviewImgResourcePath = "/upload/**";

    private String ProfileResourcePath = "/profile/**";


    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        ClassPathResource classPathResourceProfile = new ClassPathResource("static/profile/");
        ClassPathResource classPathResourceReviewImg = new ClassPathResource("static/review_img/");

        File reviewImg = null;
        File profile = null;
        try {
            reviewImg = classPathResourceReviewImg.getFile();
            profile = classPathResourceProfile.getFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        registry.addResourceHandler(reviewImgResourcePath)
                .addResourceLocations("file://"+reviewImg.getAbsolutePath()+"/");
        registry.addResourceHandler(ProfileResourcePath)
                .addResourceLocations("file://"+profile.getAbsolutePath()+"/");
    }
}
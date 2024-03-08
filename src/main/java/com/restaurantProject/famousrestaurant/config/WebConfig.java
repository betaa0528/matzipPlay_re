package com.restaurantProject.famousrestaurant.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    private String reviewResourcePath = "/upload/**";
    private String profileResourcePath = "/profile/**";
    private String reviewSavePath = "file:////C:/review_img/";
    private String profileSavePath = "";


    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler(reviewResourcePath)
                .addResourceLocations(reviewSavePath);
        registry.addResourceHandler(profileResourcePath)
                .addResourceLocations(profileSavePath);
    }
}

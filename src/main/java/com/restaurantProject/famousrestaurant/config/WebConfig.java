package com.restaurantProject.famousrestaurant.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    private String resourcePath = "/upload/**";
    private String savePath = "file:///Users/yun/Desktop/review_img/";

    private String resourcePath2 = "/profile/**";
    private String savePath2 = "file:///Users/yun/Desktop/profile/";

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler(resourcePath)
                .addResourceLocations(savePath);
        registry.addResourceHandler(resourcePath2)
                .addResourceLocations(savePath2);
    }
}
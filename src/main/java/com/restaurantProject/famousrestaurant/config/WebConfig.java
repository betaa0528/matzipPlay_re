package com.restaurantProject.famousrestaurant.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    private String reviewResourcePath = "/review_img/**";
    private String profileResourcePath = "/profile/**";
    private String reviewSavePath = "https://elasticbeanstalk-ap-northeast-2-028662638933.s3.ap-northeast-2.amazonaws.com/review_img/";
    private String profileSavePath = "https://elasticbeanstalk-ap-northeast-2-028662638933.s3.ap-northeast-2.amazonaws.com/profile/";


    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler(reviewResourcePath)
                .addResourceLocations(reviewSavePath);
        registry.addResourceHandler(profileResourcePath)
                .addResourceLocations(profileSavePath);
    }
}

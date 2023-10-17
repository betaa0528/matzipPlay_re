package com.restaurantProject.famousrestaurant.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class RealPath {

    @Autowired
    private ResourceLoader resourceLoader;

    public String realPath() throws IOException {
        Resource resource = resourceLoader.getResource("classpath:/static/");
        return resource.getURL().getPath();
    }
}

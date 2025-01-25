package com.example.main.Configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {



    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/HMSImages/**")
        //                   .addResourceLocations("file:/Users/girjeshbaghel/Documents/Images/");
        .addResourceLocations("file:/mnt/vol1/HMS/HMSImages/");

    }
}


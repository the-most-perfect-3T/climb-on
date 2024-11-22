package com.ohgiraffers.climbon.crew.crewHome.controller;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/img/multi/**")
                .addResourceLocations("file:///C:/uploads/single/");
        registry.addResourceHandler("/img/crewPic/**")
                .addResourceLocations("file:///C:/climbon/crew/profile/");
    }
}

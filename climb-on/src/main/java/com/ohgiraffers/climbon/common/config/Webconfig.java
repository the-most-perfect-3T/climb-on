package com.ohgiraffers.climbon.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class Webconfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        String commonPath = "file:///C:/climbon/";

        registry.addResourceHandler("/img/profile/**")
                .addResourceLocations(commonPath + "profile/");

        registry.addResourceHandler("/img/business/**")
                .addResourceLocations(commonPath + "business/");

        registry.addResourceHandler("/images/crewPic/**")
                .addResourceLocations(commonPath + "crew/profile/");

        registry.addResourceHandler("/images/crewPost/**")
                .addResourceLocations(commonPath + "crew/posts/");
    }
}

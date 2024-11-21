package com.ohgiraffers.climbon.user.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class Webconfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/img/profile/**")
                .addResourceLocations("file:///C:/uploads/profile/");

        registry.addResourceHandler("/img/business/**")
                .addResourceLocations("file:///C:/uploads/business/");
    }
}

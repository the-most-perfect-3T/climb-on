package com.ohgiraffers.climbon;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
public class ClimbOnApplication
{

    public static void main(String[] args)
    {
        SpringApplication.run(ClimbOnApplication.class, args);
    }

}

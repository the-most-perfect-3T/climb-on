package com.ohgiraffers.climbon.user.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserController {

    @GetMapping("mypage")
    public String mypage() {
        return "mypage/mypage";
    }
}

package com.ohgiraffers.climbon.auth.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/auth/*")
public class AuthController {

    @GetMapping("login")
    public ModelAndView login(ModelAndView mv) {
        mv.setViewName("auth/login");
        return mv;
    }
}

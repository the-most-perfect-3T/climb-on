package com.ohgiraffers.climbon.auth.controller;

import com.ohgiraffers.climbon.auth.service.AuthService;
import com.ohgiraffers.climbon.auth.model.dto.SignupDTO;
import com.ohgiraffers.climbon.auth.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;


@Controller
@RequestMapping("/auth/*")
public class AuthController {

    @Autowired
    private UserService userService;

    @GetMapping("login")
    public ModelAndView login(ModelAndView mv) {
        mv.setViewName("auth/login");
        return mv;
    }

    @GetMapping("signup")
    public ModelAndView signup(ModelAndView mv) {
        mv.setViewName("auth/signup");
        return mv;
    }

    @PostMapping("signup")
    public ModelAndView signup(ModelAndView mv, SignupDTO signupDTO) {
        System.out.println("post");

        int result = userService.regist(signupDTO);
        if(result > 0){
            // 가입 성공
            System.out.println("가입 성공 !!");
        }else {
            // 가입 실패
            System.out.println("가입 실패 !!");
        }
        return mv;
    }

}

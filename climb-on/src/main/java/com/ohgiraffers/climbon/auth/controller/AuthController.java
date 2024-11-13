package com.ohgiraffers.climbon.auth.controller;

import com.ohgiraffers.climbon.auth.service.AuthService;
import com.ohgiraffers.climbon.auth.model.dto.SignupDTO;
import com.ohgiraffers.climbon.auth.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


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
    public String signup(SignupDTO signupDTO, RedirectAttributes redirectAttributes) {

        String message;
        int result = userService.regist(signupDTO);
        if(result > 0){
            message = "회원 가입이 완료 되었습니다.";
            redirectAttributes.addFlashAttribute("message", message);
            return "redirect:/auth/login";
        }else {
            message = "회원 가입이 실패 하였습니다.";
            redirectAttributes.addFlashAttribute("message", message);
            return "redirect:/user/signup";
        }
    }


   /* @GetMapping("checkUserId")
    public String checkUserId(@RequestParam String userId, RedirectAttributes redirectAttributes){

        if(userService.isUserIdExists(userId)){
            redirectAttributes.addFlashAttribute("message", "중복된 아이디 입니다. \n다시 입력해주세요.");
            return "redirect:/user/signup";
        }else {
            redirectAttributes.addFlashAttribute("message", "가입 가능한 아이디입니다.");
            redirectAttributes.addFlashAttribute("id", userId);
            return "redirect:/user/signup";
        }
    }*/


    @GetMapping("fail")
    public ModelAndView fail(ModelAndView mv, @RequestParam("message") String message){
        mv.addObject("message", message);
        mv.setViewName("auth/fail");
        return mv;
    }

}

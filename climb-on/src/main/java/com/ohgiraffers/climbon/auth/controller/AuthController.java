/*
 *   2024-11-12 최초 작성
 *   작성자: 최정민
 * */
package com.ohgiraffers.climbon.auth.controller;

import com.ohgiraffers.climbon.auth.common.EmailValidator;
import com.ohgiraffers.climbon.auth.common.NameValidator;
import com.ohgiraffers.climbon.auth.model.dto.SignupDTO;
import com.ohgiraffers.climbon.auth.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Map;

import static com.ohgiraffers.climbon.auth.common.HashUtil.sha256Hex;


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
    public ModelAndView signupForm(ModelAndView mv) {
        mv.setViewName("auth/signup");
        return mv;
    }

    @PostMapping("signup")
    public String signup(SignupDTO signupDTO, RedirectAttributes redirectAttributes) {

        String message;

        // 프로필 이미지 생성
        String userId = signupDTO.getUserId().toLowerCase();
        String hash = sha256Hex(userId);
        String gravatarURL = "https://gravatar.com/avatar/" + hash + "?&d=identicon";

        signupDTO.setProfilePic(gravatarURL);

        int result = userService.regist(signupDTO);

        if(result > 0){
            message = "회원 가입이 완료 되었습니다.";

            redirectAttributes.addFlashAttribute("message", message);

            return "redirect:/auth/login";
        }else {
            message = "회원 가입이 실패 하였습니다.";
            redirectAttributes.addFlashAttribute("message", message);

            return "redirect:/auth/signup";
        }

    }

    /**아이디 중복검사*/
    @GetMapping("checkUserId")
    public ResponseEntity<String> checkUserId(@RequestParam Map<String, Object> parameters){

        String userId = (String) parameters.get("userId");
        // 유효성 검사
        if(!EmailValidator.isValidEmail(userId)){
            return ResponseEntity.ok("유효하지않은 형식입니다. \n다시 입력해주세요.");
        }

        // 중복 여부
        if(userService.isUserIdExists(userId)){
            return ResponseEntity.ok("중복된 아이디 입니다. \n다시 입력해주세요.");
        }

        // 성공
        return ResponseEntity.ok("가입 가능한 아이디입니다.");
    }

    /**닉네임 중복검사*/
    @GetMapping("checkName")
    public ResponseEntity<String> checkName(@RequestParam("nickname") String nickname){

        // 유효성 검사
        if(!NameValidator.isValidName(nickname)){
            return ResponseEntity.ok("유효하지않은 형식입니다. \n다시 입력해주세요.");
        }

        // 중복 여부
        if(userService.isUserNameExists(nickname)){
            return ResponseEntity.ok("중복된 닉네임입니다. \n다시 입력해주세요.");
        }

        // 성공
        return ResponseEntity.ok("가입 가능한 닉네임입니다.");
    }



    @GetMapping("fail")
    public ModelAndView fail(ModelAndView mv, @RequestParam("message") String message){
        mv.addObject("message", message);
        mv.setViewName("auth/login");
        return mv;
    }

}

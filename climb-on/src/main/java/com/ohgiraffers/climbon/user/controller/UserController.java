/*
 *   2024-11-15 최초 작성
 *   작성자: 최정민
 * */
package com.ohgiraffers.climbon.user.controller;

import com.ohgiraffers.climbon.auth.model.AuthDetail;
import com.ohgiraffers.climbon.user.dto.UserDTO;
import com.ohgiraffers.climbon.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/mypage/*")
public class UserController {

    @Autowired
    private UserService userService;

    private void populateUserData(ModelAndView mv, Integer userId) {
        UserDTO user = userService.findByKey(userId);
        String crewName = userService.findCrewName(userId);
        String homeName = userService.findHomeName(userId);

        mv.addObject("user", user);
        mv.addObject("crewName", crewName);
        mv.addObject("homeName", homeName);
    }

    @GetMapping("home")
    public ModelAndView mypage(ModelAndView mv, @AuthenticationPrincipal AuthDetail userDetails) {
        if (userDetails == null || userDetails.getLoginUserDTO() == null) {
            mv.addObject("message", "로그인 정보가 유효하지 않습니다. 다시 로그인해주세요.");
            mv.setViewName("/auth/login");
            return mv;
        }

        Integer key = userDetails.getLoginUserDTO().getId();
        UserDTO user = userService.findByKey(key);

        if (user == null) {
            mv.addObject("message", "사용자 정보를 찾을 수 없습니다.");
            mv.setViewName("/auth/login");
            return mv;
        }

        populateUserData(mv, key);
        mv.setViewName("mypage/mypage");
        return mv;
    }

    @PostMapping("updateUser")
    public ModelAndView updateUser(
            ModelAndView mv,
            @AuthenticationPrincipal AuthDetail userDetails,
            UserDTO user,
            RedirectAttributes redirectAttributes) {

        if (userDetails == null || userDetails.getLoginUserDTO() == null) {
            redirectAttributes.addFlashAttribute("message", "로그인 정보가 유효하지 않습니다.");
            return new ModelAndView("redirect:/auth/login");
        }

        Integer key = userDetails.getLoginUserDTO().getId();
        int result = userService.updateUser(user, key);

        if (result > 0) {
            redirectAttributes.addFlashAttribute("message", "회원정보를 수정했습니다.");
            return new ModelAndView("redirect:/mypage/home");
        } else {
            populateUserData(mv, key);
            mv.addObject("message", "회원정보 수정에 실패했습니다.");
            mv.setViewName("mypage/mypage");
            return mv;
        }
    }



    @PostMapping("profileModify")
    public ModelAndView updateProfile(
            @AuthenticationPrincipal AuthDetail userDetails,
            UserDTO user,
            RedirectAttributes redirectAttributes,
            ModelAndView mv) {

        if (userDetails == null || userDetails.getLoginUserDTO() == null) {
            redirectAttributes.addFlashAttribute("message", "로그인이 필요합니다.");
            return new ModelAndView("redirect:/auth/login");
        }


        Integer key = userDetails.getLoginUserDTO().getId();

        int result = userService.updateProfile(user, key);



        if (result > 0) {
            mv.addObject("message", "프로필 이미지를 수정했습니다.");
            mv.setViewName("redirect:/mypage/home");
        } else {
            mv.addObject("message", "프로필 이미지 수정에 실패했습니다.");
            mv.setViewName("mypage/mypage");
        }

        return mv;
    }
}

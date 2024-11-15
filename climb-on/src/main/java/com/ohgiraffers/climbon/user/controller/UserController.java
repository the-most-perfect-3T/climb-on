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

@Controller
@RequestMapping("/mypage")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("home")
    public ModelAndView mypage(ModelAndView mv, @AuthenticationPrincipal AuthDetail userDetails) {

        Integer key = userDetails.getLoginUserDTO().getId();
        UserDTO user = userService.findByKey(key);
        String crewName = userService.findCrewName(key);
        String homeName = userService.findHomeName(key);

        if(user == null) {
            // 찾아온 결과가 없을 때...
        }

        mv.addObject("user", user);
        mv.addObject("crewName", crewName);
        mv.addObject("homeName", homeName);

        mv.setViewName("mypage/mypage");
        return mv;
    }

    @PostMapping("updateUser")
    public ModelAndView updateUser(ModelAndView mv, @AuthenticationPrincipal AuthDetail userDetails, UserDTO user) {

        Integer key = userDetails.getLoginUserDTO().getId();
        user.setId(key);
        System.out.println(user.getNickname());
        System.out.println(user.getPassword());
        System.out.println(user.getOneLiner());
        int result = userService.updateUser(user);
        System.out.println(result);

        if(result > 0){

            mv.addObject("message", "회원정보를 수정했습니다.");
            mv.setViewName("mypage/mypage");
        }else {

            mv.addObject("message", "회원정보 수정에 실패했습니다.");
            mv.setViewName("mypage/mypage");
        }

        return mv;
    }
}

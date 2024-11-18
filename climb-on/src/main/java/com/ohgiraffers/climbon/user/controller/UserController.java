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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import static com.ohgiraffers.climbon.auth.common.HashUtil.sha256Hex;

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
            RedirectAttributes redirectAttributes,
            ModelAndView mv,
            @RequestParam("profilePic") MultipartFile profilePic) {

        if (userDetails == null || userDetails.getLoginUserDTO() == null) {
            redirectAttributes.addFlashAttribute("message", "로그인 정보가 유효하지 않습니다.");
            return new ModelAndView("redirect:/auth/login");
        }

        if (profilePic.isEmpty()) {
            mv.addObject("message", "파일을 선택해주세요.");
            mv.setViewName("mypage/mypage");
            return mv;
        }

        String filePath = "C:/uploads/profile";
        File fileDir = new File(filePath);

        if(!fileDir.exists()){
            fileDir.mkdirs();
        }

        String originFileName = profilePic.getOriginalFilename();
        String ext = originFileName.substring(originFileName.lastIndexOf("."));
        String savedName = UUID.randomUUID().toString().replace("-", "") + ext;

        try {
            profilePic.transferTo(new File(filePath + "/" + savedName));
            String newFileName = "/img/profile/" + savedName;

            Integer key = userDetails.getLoginUserDTO().getId();
            int result = userService.updateProfile(newFileName, key);

            if (result > 0) {
                redirectAttributes.addFlashAttribute("message", "프로필 이미지를 수정했습니다.");
                mv.setViewName("redirect:/mypage/home");
            } else {
                populateUserData(mv, key);
                mv.addObject("message", "프로필 이미지 수정에 실패했습니다.");
                mv.setViewName("mypage/mypage");
            }


        } catch (IOException e) {
            e.printStackTrace();
            mv.addObject("message", "파일 업로드에 실패했습니다.");
            mv.setViewName("mypage/mypage");
        }



        return mv;
    }



    @PostMapping("profileDelete")
    public ModelAndView deleteProfile(
            @AuthenticationPrincipal AuthDetail userDetails,
            RedirectAttributes redirectAttributes,
            ModelAndView mv) {

        if (userDetails == null || userDetails.getLoginUserDTO() == null) {
            redirectAttributes.addFlashAttribute("message", "로그인 정보가 유효하지 않습니다.");
            return new ModelAndView("redirect:/auth/login");
        }

        Integer key = userDetails.getLoginUserDTO().getId();

        String userId = userDetails.getLoginUserDTO().getUserId();
        String hash = sha256Hex(userId);
        String gravatarURL = "https://gravatar.com/avatar/" + hash + "?&s=200&d=identicon";

        int result = userService.updateProfile(gravatarURL, key);


        if (result > 0) {
            redirectAttributes.addFlashAttribute("message", "프로필 이미지를 삭제했습니다.");
            mv.setViewName("redirect:/mypage/home");

        } else {
            populateUserData(mv, key);
            mv.addObject("message", "프로필 이미지 삭제를 실패했습니다.");
            mv.setViewName("mypage/mypage");
        }


        return mv;
    }


    @PostMapping("updateStatus")
    public ModelAndView updateStatus(ModelAndView mv, @AuthenticationPrincipal AuthDetail userDetails, RedirectAttributes redirectAttributes){
        System.out.println("실행됨.");

        if (userDetails == null || userDetails.getLoginUserDTO() == null) {
            redirectAttributes.addFlashAttribute("message", "로그인 정보가 유효하지 않습니다.");
            return new ModelAndView("redirect:/auth/login");
        }
        Integer key = userDetails.getLoginUserDTO().getId();

        int result = userService.updateStatus(key);
        if (result > 0) {
            redirectAttributes.addFlashAttribute("message", "회원 탈퇴되었습니다. \n그동안 이용해주셔서 감사합니다.");
            mv.setViewName("redirect:/");
        }else {
            populateUserData(mv, key);
            mv.addObject("message", "회원 탈퇴에 실패했습니다.");
            mv.setViewName("mypage/mypage");
        }
        return mv;
    }

}

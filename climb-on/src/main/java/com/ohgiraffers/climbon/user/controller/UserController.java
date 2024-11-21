/*
 *   2024-11-15 최초 작성
 *   작성자: 최정민
 * */
package com.ohgiraffers.climbon.user.controller;

import com.ohgiraffers.climbon.auth.model.AuthDetail;
import com.ohgiraffers.climbon.facilities.service.FacilitiesService;
import com.ohgiraffers.climbon.user.dto.BusinessDTO;
import com.ohgiraffers.climbon.user.dto.NoticeDTO;
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
import java.util.*;

import static com.ohgiraffers.climbon.auth.common.HashUtil.sha256Hex;

@Controller
@RequestMapping("/mypage/*")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private FacilitiesService facilitiesService;

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

        // 로그인 정보 없으면
        if (userDetails == null || userDetails.getLoginUserDTO() == null) {
            mv.addObject("message", "로그인 정보가 유효하지 않습니다. 다시 로그인해주세요.");
            mv.setViewName("/auth/login");
            return mv;
        }

        // 유저 pk
        Integer key = userDetails.getLoginUserDTO().getId();

        // 유저 role
        String role = userDetails.getLoginUserDTO().getUserRole().getRole();
        System.out.println("role = " + role);


        if(role.equals("USER")){
            // user 알림 테이블 가져오기

        }else if(role.equals("ADMIN")){

            // admin 알림 테이블 가져오기
            List<NoticeDTO> noticeList = userService.selectAdminNotice();
            System.out.println("noticeList = " + noticeList);

            // 알림 데이터를 저장할 리스트
            List<Map<String, Object>> noticeMapList = new ArrayList<>();

            // 알림 수
            mv.addObject("noticeCount", noticeList.size());



            for(NoticeDTO notice : noticeList){
                Map<String, Object> noticeMap = new HashMap<>();

                Integer userCode = (Integer) notice.getUserCode();
                String nickname = userService.findById(userCode); // userCode 로 닉네임 찾아오기
                System.out.println("nickname = " + nickname);
                /*mv.addObject("name", nickname);*/

                noticeMap.put("userCode", notice.getUserCode());
                noticeMap.put("category", notice.getCategory());
                noticeMap.put("facilityCode", notice.getFacilityCode());
                noticeMap.put("attachFile", notice.getAttachFile());
                noticeMap.put("isApproval", notice.getIsApproval());
                noticeMap.put("nickname", nickname);

                noticeMapList.add(noticeMap);
            }


            mv.addObject("noticeMapList", noticeMapList);


        }else if(role.equals("BUSINESS")){

        }


        // 유저 pk 로 정보 전체 가져오기
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

        // 이미지 파일 형식인지 재확인
        String contentType = profilePic.getContentType();
        Set<String> allowedMimeTypes = new HashSet<>(Arrays.asList("image/jpeg", "image/png", "image/gif"));

        if (!allowedMimeTypes.contains(contentType)) {
            mv.addObject("message", "지원하지 않는 파일 형식입니다.");
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

        if (userDetails == null || userDetails.getLoginUserDTO() == null) {
            redirectAttributes.addFlashAttribute("message", "로그인 정보가 유효하지 않습니다.");
            return new ModelAndView("redirect:/auth/login");
        }
        Integer key = userDetails.getLoginUserDTO().getId();

        int result = userService.updateStatus(key);
        if (result > 0) {
            mv.setViewName("/common/userWithdraw");

        }else {
            populateUserData(mv, key);
            mv.addObject("message", "회원 탈퇴에 실패했습니다.");
            mv.setViewName("mypage/mypage");
        }
        return mv;
    }




    @PostMapping("applyBusiness")
    public ModelAndView applyBusiness(
            @AuthenticationPrincipal AuthDetail userDetails,
            RedirectAttributes redirectAttributes,
            ModelAndView mv,
            BusinessDTO businessDTO) {

        if (userDetails == null || userDetails.getLoginUserDTO() == null) {
            redirectAttributes.addFlashAttribute("message", "로그인 정보가 유효하지 않습니다.");
            return new ModelAndView("redirect:/auth/login");
        }
        

        // 신청했었던 사람인지 확인
        Integer key = userDetails.getLoginUserDTO().getId();
        int isApproval = userService.findByIdIsApproval(key);
        if(isApproval == 0){
            redirectAttributes.addFlashAttribute("message", "관리자 승인 대기중입니다. 잠시만 기다려주세요.");
            return new ModelAndView("redirect:/mypage/home");
        }


        String facilityName = businessDTO.getFacilityName();
        MultipartFile businessFile = businessDTO.getBusinessFile();


        if (businessFile.isEmpty() || facilityName.isEmpty()) {
            mv.addObject("message", "파일을 선택해주세요.");
            mv.setViewName("mypage/mypage");
            return mv;
        }


        // 이미지 파일 형식인지 재확인
        String contentType = businessFile.getContentType();
        Set<String> allowedMimeTypes = new HashSet<>(Arrays.asList("image/jpeg", "image/png", "image/gif"));

        if (!allowedMimeTypes.contains(contentType)) {
            mv.addObject("message", "지원하지 않는 파일 형식입니다.");
            mv.setViewName("mypage/mypage");
            return mv;
        }


        // facilityName 으로 facility id 찾아오기
        int facilityCode = facilitiesService.getFacilityIdByName(facilityName);


        // 첨부파일 저장
        String filePath = "C:/uploads/business";
        File fileDir = new File(filePath);

        if(!fileDir.exists()){
            fileDir.mkdirs();
        }

        String originFileName = businessFile.getOriginalFilename();
        String ext = originFileName.substring(originFileName.lastIndexOf("."));
        String savedName = UUID.randomUUID().toString().replace("-", "") + ext;

        try {
            businessFile.transferTo(new File(filePath + "/" + savedName));
            String newFileName = "/img/business/" + savedName;

            
            // 비즈니스 전환신청 테이블에 추가
            int result = userService.registBusiness(newFileName, key, facilityCode); // 첨부파일, 유저코드, 시설코드
            // 관리자 알림 테이블에 추가
            int result2 = userService.registAdminNotice(key);


            if (result > 0 && result2 > 0) {
                redirectAttributes.addFlashAttribute("message", "비즈니스계정 전환 신청이 완료되었습니다. \n관리자 승인 후 전환 됩니다.");
                mv.setViewName("redirect:/mypage/home");
            } else {
                populateUserData(mv, key);
                mv.addObject("message", "비즈니스계정 전환에 실패했습니다.");
                mv.setViewName("mypage/mypage");
            }


        } catch (IOException e) {
            e.printStackTrace();
            mv.addObject("message", "파일 업로드에 실패했습니다.");
            mv.setViewName("mypage/mypage");
        }


        return mv;
    }


    @PostMapping("updateNotice")
    public String updateNotice(NoticeDTO notice, RedirectAttributes redirectAttributes) {
        int isApproval = notice.getIsApproval();
        switch (isApproval) {
            case 1:
                // 승인 로직
                notice.setIsApproval(1);
                // user role / nickname-facilityName 으로 변경
                int userCode = notice.getUserCode();

                UserDTO userDTO = new UserDTO();
                userDTO.setId(userCode);
                int result0 = userService.updateRole(userDTO, userCode);
                // 비즈니스전환테이블에 상태변경(승인)
                int result = userService.updateNotice(notice);
                // 비즈니스알림 테이블에 추가
                int result1 = userService.registBusinessNotice(userCode);

                if(result > 0 && result1 > 0 && result0 > 0){
                    redirectAttributes.addFlashAttribute("message", "요청을 승인했습니다.");
                    return "redirect:/mypage/home";
                }else {
                    redirectAttributes.addFlashAttribute("message", "요청 승인을 실패했습니다.");
                    return "redirect:/mypage/home";
                }

            case -1:
                System.out.println("isApproval 세팅 전" + notice.getIsApproval());
                // 거절 로직
                notice.setIsApproval(-1);
                // 비즈니스 전환 테이블에 상태변경(거절)
                int result2 = userService.updateNotice(notice);
                // 유저알림 테이블에 추가
                int userCode2 = notice.getUserCode();
                int result3 = userService.registUserNotice(userCode2);

                if(result2 > 0 && result3 > 0){
                    redirectAttributes.addFlashAttribute("message", "요청을 거절했습니다.");
                    return "redirect:/mypage/home";
                }else {
                    redirectAttributes.addFlashAttribute("message", "요청 거절을 실패했습니다.");
                    return "redirect:/mypage/home";
                }

        }

        return "redirect:/mypage/home";
    }
}

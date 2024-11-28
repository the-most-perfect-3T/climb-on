package com.ohgiraffers.climbon.crew.mycrew.controller;

import com.ohgiraffers.climbon.auth.model.AuthDetail;
import com.ohgiraffers.climbon.crew.crewHome.dto.CrewDTO;
import com.ohgiraffers.climbon.crew.mycrew.Enum.CrewRole;
import com.ohgiraffers.climbon.crew.mycrew.dto.CrewApplyDTO;
import com.ohgiraffers.climbon.crew.mycrew.dto.CrewMembersDTO;
import com.ohgiraffers.climbon.crew.mycrew.dto.UserCrewDTO;
import com.ohgiraffers.climbon.crew.mycrew.service.MyCrewService;
import com.ohgiraffers.climbon.user.dto.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Controller
@RequestMapping(("/crew/myCrew"))
public class MyCrewController
{
    @Autowired
    private MyCrewService myCrewService;

    @GetMapping
    public ModelAndView getMyCrewPage(ModelAndView mv, @AuthenticationPrincipal AuthDetail userDetails, RedirectAttributes redirectAttributes)
    {
        try {// 비로그인 user
            if (userDetails == null || userDetails.getLoginUserDTO() == null) {
                mv.addObject("message", "크루 페이지는 로그인 후 볼수 있습니다.  \n로그인해주세요.");
                mv.setViewName("/auth/login");
                return mv;
            }
            // 로그인 user
            else{
                int myId = userDetails.getLoginUserDTO().getId();
                UserCrewDTO userCrewDTO = myCrewService.getMyCrewCodeAndRole(myId);
                // 크루원 아닐시
                if(Objects.isNull(userCrewDTO)){
                    redirectAttributes.addFlashAttribute("alertMessage", "크루에 가입되어 있지 않습니다.");
                    mv.setViewName("redirect:/crew/crewlist");
                    return mv;

                }else { // 크루원 일시
                    CrewDTO myCrew = myCrewService.getMyCrewById(myId);
                    int memberCount = myCrewService.getMemberCount(myCrew.getId());
                    boolean isMyCrew = true;
                    boolean haveCrew = true;

                    //role이 CAPTAIN일시 db 조회추가

                    /////////////////////

                    mv.addObject("isMyCrew", isMyCrew);
                    mv.addObject("haveCrew", haveCrew);
                    mv.addObject("memberCount", memberCount);
                    mv.addObject("myCrew", myCrew);
                    mv.setViewName("crew/myCrew/myCrew");
                    return mv;
                }

            }
        } catch (Exception e) {
            e.printStackTrace(); // Log error
            // Redirect to an error page if needed
            mv.setViewName("redirect:/eror/500");
            return mv;
        }
    }


    @GetMapping("/{crewCode}")
    public ModelAndView getOtherCrewPage(@PathVariable("crewCode") Integer crewCode, ModelAndView mv, @AuthenticationPrincipal AuthDetail userDetails, RedirectAttributes redirectAttributes, @ModelAttribute("alertMessage") String alertMessage){
        try {// 비로그인 user
            if (userDetails == null || userDetails.getLoginUserDTO() == null) {
                mv.addObject("message", "크루 페이지는 로그인 후 볼수 있습니다.  \n로그인해주세요.");
                mv.setViewName("/auth/login");
            }
            // 로그인 user
            else {
                int myId = userDetails.getLoginUserDTO().getId();
                UserCrewDTO userCrewDTO = myCrewService.getMyCrewCodeAndRole(myId);

                // user가 가입되지 않은 크루일시(노크루 유저, 타크루 유저 포함)
                if (Objects.isNull(userCrewDTO) || userCrewDTO.getCrewCode() != crewCode) {
                    boolean isMyCrew = false;
                    boolean haveCrew = true;
                    if (Objects.isNull(userCrewDTO)) {
                        haveCrew = false;
                    }
                    CrewDTO crewInfo = myCrewService.getCrewInfoByCrewCode(crewCode);
                    int memberCount = myCrewService.getMemberCount(crewInfo.getId());

                    mv.addObject("memberCount", memberCount);
                    mv.addObject("myCrew", crewInfo);
                    mv.addObject("isMyCrew", isMyCrew);
                    mv.addObject("haveCrew", haveCrew);
                    mv.setViewName("crew/myCrew/myCrew");
                }
                // user가 가입된 크루일시
                else if (userCrewDTO.getCrewCode() == crewCode) {
                    if (alertMessage != null && !alertMessage.isEmpty()){
                        redirectAttributes.addFlashAttribute("alertMessage", "크루 가입 완료.");
                    }
                    mv.setViewName("redirect:/crew/myCrew");
                }
            }
            return mv;

        } catch (Exception e) {
            e.printStackTrace(); // Log error
            mv.setViewName("redirect:/eror/500");
            return mv;
        }
    }

    @PostMapping("/crewApply/{crewCode}")
    public ModelAndView applyForCrew(@PathVariable("crewCode") int crewCode, String content, ModelAndView mv, @AuthenticationPrincipal AuthDetail userDetails, RedirectAttributes redirectAttributes){
        int myId = userDetails.getLoginUserDTO().getId();
        System.out.println(content);
        // 크루 가입조건 확인
        boolean isPermission = myCrewService.getHowToJoinCrew(crewCode);

        // 크루 가입신청
        CrewApplyDTO crewApplication = new CrewApplyDTO();
        crewApplication.setCrewCode(crewCode);
        crewApplication.setContent(content);
        crewApplication.setUserCode(myId);
        crewApplication.setIsApproval(0);
        crewApplication.setIsPermission(isPermission);

        int resultForCrewMemberUpdate = 1;
        // 신청 즉시 가입
        if(!isPermission){
            UserCrewDTO newMember = new UserCrewDTO();
            newMember.setCrewCode(crewCode);
            newMember.setUserCode(myId);
            newMember.setRole(CrewRole.MEMBER);
            resultForCrewMemberUpdate = myCrewService.crewMemberInsert(newMember);
        }

        // 가입 신청서(신청즉시 가입, 승인후 가입 모두 crew CAPTAIN 이 확인 할수 있게 한다)
        int resultForCrewApplyInsert = myCrewService.applyForCrew(crewApplication);
        if(resultForCrewApplyInsert == 0 || resultForCrewMemberUpdate == 0){
            redirectAttributes.addFlashAttribute("alertMessage", "크루 가입 신청중에 문제가 발생했습니다.");
            mv.setViewName("redirect:/crew/myCrew/" + crewCode);
        }else{
            if(isPermission){
                redirectAttributes.addFlashAttribute("alertMessage", "크루 가입 신청이 완료 되었습니다. \n크루장 승인/거절 결과는 마이 페이지 알림으로 확인 가능합니다.");
            }else if(!isPermission){
                redirectAttributes.addFlashAttribute("alertMessage", "크루 가입 완료.");
            }

            mv.setViewName("redirect:/crew/myCrew/" + crewCode);
        }


        return mv;
    }
}

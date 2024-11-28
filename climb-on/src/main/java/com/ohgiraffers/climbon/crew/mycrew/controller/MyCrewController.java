package com.ohgiraffers.climbon.crew.mycrew.controller;

import com.ohgiraffers.climbon.auth.model.AuthDetail;
import com.ohgiraffers.climbon.crew.crewHome.dto.CrewDTO;
import com.ohgiraffers.climbon.crew.mycrew.dto.CrewMembersDTO;
import com.ohgiraffers.climbon.crew.mycrew.dto.UserCrewDTO;
import com.ohgiraffers.climbon.crew.mycrew.service.MyCrewService;
import com.ohgiraffers.climbon.user.dto.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
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
    public ModelAndView getOtherCrewPage(@PathVariable("crewCode") Integer crewCode, ModelAndView mv, @AuthenticationPrincipal AuthDetail userDetails, RedirectAttributes redirectAttributes){
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
}

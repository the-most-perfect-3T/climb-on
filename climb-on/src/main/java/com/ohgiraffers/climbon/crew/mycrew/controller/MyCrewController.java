package com.ohgiraffers.climbon.crew.mycrew.controller;

import com.ohgiraffers.climbon.auth.model.AuthDetail;
import com.ohgiraffers.climbon.crew.crewHome.dto.CrewDTO;
import com.ohgiraffers.climbon.crew.mycrew.dto.UserCrewDTO;
import com.ohgiraffers.climbon.crew.mycrew.service.MyCrewService;
import com.ohgiraffers.climbon.user.dto.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Objects;

@Controller
@RequestMapping(("/crew/myCrew"))
public class MyCrewController
{
    @Autowired
    private MyCrewService myCrewService;

    @GetMapping
    public ModelAndView getMyCrew(ModelAndView mv, @AuthenticationPrincipal AuthDetail userDetails, RedirectAttributes redirectAttributes)
    {
        try {
            // Fetch data from service and add to model
            if(Objects.isNull(userDetails)){
                mv.setViewName("auth/login");
                return mv;
            }else{
                int myId = userDetails.getLoginUserDTO().getId();
                UserCrewDTO userCrewDTO = myCrewService.getMyCrewCodeAndRole(myId);
                if(Objects.isNull(userCrewDTO)){
                    redirectAttributes.addFlashAttribute("alertMessage", "크루에 가입되어 있지 않습니다.");
                    mv.setViewName("redirect:/crew/crewlist");
                    return mv;
//                    return "crew/crewHome/crewList";
                }else {
                    CrewDTO myCrew = myCrewService.getMyCrewById(myId);
                    List<UserDTO> memberList = myCrewService.getCrewMemeberList(myCrew.getId());
                    System.out.println(myCrew);
                    System.out.println(userCrewDTO);
                    for(UserDTO member : memberList){
                        System.out.println(member);
                    }


                    mv.addObject("memberList", memberList);
                    mv.addObject("memberCount", memberList.size());
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


    /*@GetMapping(/{id})*/
}

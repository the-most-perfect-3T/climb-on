package com.ohgiraffers.climbon.crew.mycrew.controller;

import com.ohgiraffers.climbon.auth.model.AuthDetail;
import com.ohgiraffers.climbon.crew.crewHome.dto.CrewBoardDTO;
import com.ohgiraffers.climbon.crew.mycrew.service.MyCrewService;
import com.ohgiraffers.climbon.facilities.dto.ReviewDTO;
import com.ohgiraffers.climbon.user.dto.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/mycrew")
public class MyCrewRestController {

    @Autowired
    private MyCrewService myCrewService;


    @GetMapping("/member")
    public ResponseEntity<Object> showMemberList(@AuthenticationPrincipal AuthDetail userDetails) {
        // 유저 pk
        Integer key = userDetails.getLoginUserDTO().getId();

        List<UserDTO> memberList = myCrewService.getCrewMemeberList(key);



        /*if (reviewList == null || reviewList.isEmpty()) {
            return ResponseEntity.ok(Map.of("message", "작성한 리뷰가 없습니다."));
        }*/

        return ResponseEntity.ok(memberList);
    }



}

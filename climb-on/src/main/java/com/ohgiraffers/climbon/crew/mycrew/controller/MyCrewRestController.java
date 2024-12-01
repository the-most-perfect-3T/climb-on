package com.ohgiraffers.climbon.crew.mycrew.controller;

import com.ohgiraffers.climbon.auth.model.AuthDetail;
import com.ohgiraffers.climbon.crew.crewHome.dto.CrewBoardDTO;
import com.ohgiraffers.climbon.crew.crewHome.dto.CrewDTO;
import com.ohgiraffers.climbon.crew.mycrew.Enum.CrewRole;
import com.ohgiraffers.climbon.crew.mycrew.dto.CrewApplyWithUserInfoDTO;
import com.ohgiraffers.climbon.crew.mycrew.dto.CrewMemberListWithCrewApplyList;
import com.ohgiraffers.climbon.crew.crewHome.dto.CrewPostDTO;
import com.ohgiraffers.climbon.crew.mycrew.dto.CrewMembersDTO;
import com.ohgiraffers.climbon.crew.mycrew.dto.UserCrewDTO;
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

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/mycrew")
public class MyCrewRestController {

    @Autowired
    private MyCrewService myCrewService;


    @GetMapping("/member/{crewCode}")
    public ResponseEntity<Object> showMemberList(@PathVariable("crewCode") int crewCode, @AuthenticationPrincipal AuthDetail userDetails) {
        int myId = userDetails.getLoginUserDTO().getId();
        UserCrewDTO userCrewDTO = myCrewService.getMyCrewCodeAndRole(myId);
        List<CrewMembersDTO> memberList = myCrewService.getCrewMemberList(crewCode);
        // 해당 크루의 크루 캡틴이면 크루신청을 챙겨서 돌아간다
        if(!Objects.isNull(userCrewDTO) && userCrewDTO.getCrewCode() == crewCode && userCrewDTO.getRole().equals(CrewRole.CAPTAIN)) {
            List<CrewApplyWithUserInfoDTO> crewApplys = myCrewService.getNewCrewApplyContentByCrewCode(userCrewDTO.getCrewCode());
            CrewMemberListWithCrewApplyList crewMemberListWithCrewApplyList = new CrewMemberListWithCrewApplyList(crewApplys, memberList);
            return ResponseEntity.ok(crewMemberListWithCrewApplyList);
        }
        else{
            return ResponseEntity.ok(memberList);
        }
    }

    @GetMapping("/member/newApply/{userId}")
    public ResponseEntity<Object> getInfoForModal(@PathVariable("userId") int userId){
        CrewApplyWithUserInfoDTO crewApply = myCrewService.getCrewApplyWithUserInfo(userId);
        return ResponseEntity.ok(crewApply);
    }

    @GetMapping("/album/{crewCode}")
    public ResponseEntity<Object> showAlbum(@PathVariable("crewCode") int crewCode, @AuthenticationPrincipal AuthDetail userDetails) {
        // 크루멤버들이 쓴 게시글의 imgUrl 컬럼(null 포합)
        List<String> imgUrlList = myCrewService.getImgUrlList(crewCode);

        // list 정리를 쉽게 해주는 stream API
        // null 값 필터, split, 빈 값("") 제거, 다시 리스트로 반환
        List<String> imgList = imgUrlList.stream()
                .filter(Objects::nonNull)
                .flatMap(url -> Arrays.stream(url.split(",")))
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toList());

        return ResponseEntity.ok(imgList);
    }

    @GetMapping("/posts/{crewCode}")
    public ResponseEntity<Object> showPosts(@PathVariable("crewCode") int crewCode)
    {
        List<CrewPostDTO> postList = myCrewService.getCrewPostsList(crewCode);
        for(CrewPostDTO post : postList){
            System.out.println(post);
            System.out.println(post.getUserProfilePic());
        }
        return ResponseEntity.ok(postList);
    }
}

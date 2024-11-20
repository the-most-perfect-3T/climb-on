package com.ohgiraffers.climbon.crew.crewHome.controller;

import com.ohgiraffers.climbon.auth.model.AuthDetail;
import com.ohgiraffers.climbon.crew.crewHome.dto.CrewBoardDTO;
import com.ohgiraffers.climbon.crew.crewHome.service.CrewBoardService;
import com.ohgiraffers.climbon.crew.crewHome.service.CrewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;

@Controller
@RequestMapping("/crew")
public class CrewController {

    @Autowired
    private CrewBoardService crewBoardService;

    @Autowired
    CrewService crewService;


    @GetMapping("/writepost")
    public String writePost( Model model) {
//        model.addAttribute("nickname", userDetails.getLoginUserDTO().getNickname());

        // crew 인지 확인하는 메소드 추가
        // 소속 crew 가 있어야만 게시글 작성 가능
        return "/crew/crewBoardWritePost";
    }



    @PostMapping("/writepost")
    public String registPost(CrewBoardDTO crewBoardDTO, @AuthenticationPrincipal AuthDetail userDetails) {
        int id = userDetails.getLoginUserDTO().getId();
        Integer crew_code = crewBoardService.getCrewCode(id);

        crewBoardDTO.setCrewCode(crew_code);
        crewBoardDTO.setUserId(id);
        System.out.println(id);
        System.out.println(crew_code);
        System.out.println(crewBoardDTO.getIsAnonymous());

        int result = crewBoardService.insertPost(crewBoardDTO);
        if (result == 0) {
            String message = "등록 실패";
        }

        return "redirect:/crew/crewBoardList";
    }

    @GetMapping("/crewBoardList")
    public ModelAndView crewBoardList(ModelAndView mv) {
        CrewBoardDTO boardDTO = crewBoardService.selectLastPost();
        mv.addObject("boardDTO", boardDTO);
        mv.setViewName("crew/crewBoardList");
        return mv;
    }

    @GetMapping("/updatepost/{id}")
    public ModelAndView updatePost(@PathVariable int id, ModelAndView mv) {
        CrewBoardDTO crewBoardDTO = crewBoardService.selectOnePostById(id);
        System.out.println(crewBoardDTO);
        mv.addObject("crewBoardDTO", crewBoardDTO);
        mv.setViewName("crew/crewBoardUpdatePost");
        return mv;
    }

    @PostMapping("/updatepost/{id}")
    public String updatePost(@PathVariable int id, CrewBoardDTO crewBoardDTO, ModelAndView mv) {
        crewBoardDTO.setId(id);
        int result = crewBoardService.updatePostById(crewBoardDTO);

        return "redirect:/crew/updatedpost?id=" + id;

    }

    @GetMapping("/updatedpost")
    public ModelAndView updatedpost(@RequestParam int id, ModelAndView mv) {
        CrewBoardDTO crewBoardDTO = crewBoardService.selectOnePostById(id);
        mv.addObject("boardDTO", crewBoardDTO);
        mv.setViewName("crew/crewBoardList");
        return mv;
    }

    @GetMapping("/crewlist")
    public ModelAndView crewList(ModelAndView mv) {
        mv.setViewName("crew/crewHome/crewList");
        return mv;
    }

    @GetMapping("/checkCrewName")
    public ResponseEntity<String> checkCrewName(@RequestParam Map<String, Object> parameters){
        String crewName = (String)parameters.get("crewName");
        System.out.println(crewName);
        if(crewService.isCrewNameExists(crewName)){
            return ResponseEntity.ok("중복된 크루 이름 입니다. \n다시 입력해주세요.");
        }
        return ResponseEntity.ok("사용 가능한 아이디입니다.");
    }


}

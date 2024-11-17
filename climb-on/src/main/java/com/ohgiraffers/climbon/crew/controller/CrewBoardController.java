package com.ohgiraffers.climbon.crew.controller;

import com.ohgiraffers.climbon.auth.model.AuthDetail;
import com.ohgiraffers.climbon.crew.dto.CrewBoardDTO;
import com.ohgiraffers.climbon.crew.service.CrewBoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;
import org.springframework.web.servlet.ModelAndView;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;

@Controller
@RequestMapping("/crew")
public class CrewBoardController {

    @Autowired
    private CrewBoardService crewBoardService;

    @GetMapping("/writepost")
    public String writePost() {
        return "/crew/crewBoardWritePost";
    }
    // crew 인지 확인하는 메소드 추가
    // 소속 crew 가 있어야만 게시글 작성 가능


    @PostMapping("/writePost")
    public String registPost(CrewBoardDTO crewBoardDTO, @AuthenticationPrincipal AuthDetail userDetails) {
        int id = userDetails.getLoginUserDTO().getId();
        Integer crew_code = crewBoardService.getCrewCode(id);

        crewBoardDTO.setCrewCode(crew_code);
        crewBoardDTO.setUserId(id);
        System.out.println(id);
        System.out.println(crew_code);
        System.out.println(crewBoardDTO.getIsAnonymous());

        int result = crewBoardService.insertPost(crewBoardDTO);
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
        mv.addObject("crewBoardDTO", crewBoardDTO);
        mv.setViewName("crew/crewBoardUpdatePost");
        return mv;
    }

    /*@PostMapping("/file/upload")
    public String upload(MultipartFile[] uploadFile) {
        String savePath;
        String uploadFileName = "";

        // OS 따라 구분자 분리
//        String os = System.getProperty("os.name").toLowerCase();
//        if (os.contains("win")){
//            savePath = System.getProperty("user.dir") + "\\files\\image";
//        }
//        else{
//            savePath = System.getProperty("user.dir") + "/files/image";
//        }
        savePath = "C:/uploads/single";
        System.out.println(savePath);

        File uploadPath = new File(savePath);

        // 파일 저장 경로가 없으면 신규 생성
        if (!uploadPath.exists()) {
            uploadPath.mkdirs();
        }

        for (MultipartFile multipartFile : uploadFile) {

            uploadFileName = multipartFile.getOriginalFilename();

            String uuid = UUID.randomUUID().toString();

            // 파일명 저장
            uploadFileName = uuid + "_" + uploadFileName;

            java.io.File saveFile = new java.io.File(uploadPath, uploadFileName);


            try {
                multipartFile.transferTo(saveFile);
                return uploadFileName;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        System.out.println(uploadPath);
        System.out.println(savePath);
        System.out.println(uploadFileName);
        return savePath;
    }*/


}

package com.ohgiraffers.climbon.crew.controller;

import com.ohgiraffers.climbon.crew.dto.CrewBoardDTO;
import com.ohgiraffers.climbon.crew.service.CrewBoardService;
import org.springframework.beans.factory.annotation.Autowired;
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

    private String imgUrl;

    @ResponseBody
    @PostMapping("/imgUpload")
    public Map<String, Object> imgUpload(@RequestParam Map<String,Object> paramMap, MultipartRequest request) {

        MultipartFile uploadImg = request.getFile("upload");

        // 업로드 파일 저장 경로 설정
        String uploadDir = "C:/uploads/temp";
        File fileDir = new File(uploadDir);
        if (!fileDir.exists()) {
            fileDir.mkdirs();
        }

        // 원본 파일 이름
        String originalFileName = uploadImg.getOriginalFilename();
        // 확장자명 추출
        String ext = originalFileName.substring(originalFileName.lastIndexOf(".")); // .뒤의 확장자명만 담기
        // UUID.randomUUID() 고유 식별자 생성
        String savedName = UUID.randomUUID().toString().replace("-","") + ext;

        try {
            uploadImg.transferTo(new File(fileDir + "/" + savedName));
            // DB에 Img url 추가

            paramMap.put("url", "/img/temp/" + savedName);
            imgUrl = fileDir + "/" + savedName;
        } catch (IOException e) {
            e.printStackTrace();
            paramMap.put("error", e.getMessage());
        }
        return paramMap;
    }

    @PostMapping("/writePost")
    public String registPost(CrewBoardDTO crewBoardDTO) {
        crewBoardDTO.setImgUrl(imgUrl);
        System.out.println(crewBoardDTO);
        System.out.println(crewBoardDTO.getCategoryCode());
        int result = crewBoardService.insertPost(crewBoardDTO);
        return "redirect:/crew/crewBoardList";
    }

    @GetMapping("/crewBoardList")
    public ModelAndView crewBoardList(ModelAndView mv) {
        mv.setViewName("crew/crewBoardList");
        return mv;

    }

}

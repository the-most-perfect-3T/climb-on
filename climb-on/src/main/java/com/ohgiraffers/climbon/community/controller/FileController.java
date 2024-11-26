package com.ohgiraffers.climbon.community.controller;

import com.ohgiraffers.climbon.crew.crewHome.dto.CrewBoardDTO;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.util.*;

@RestController("communityFileController")
@RequestMapping("/community/file")
public class FileController {

    /**
     * 에디터 내 사진 파일 업로드
     *
     * @param uploadFile
     * @return savePath - 저장경로
     */

    @PostMapping(value = "/upload", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<String> upload(MultipartFile[] uploadFile) {
        String savePath = "C:/climbon/community/posts";
        List<String> savedNames = new ArrayList<>();
        File uploadPath = new File(savePath);

        // 파일 저장 경로가 없으면 신규 생성
        if (!uploadPath.exists()) {
            uploadPath.mkdirs();
        }

        for (MultipartFile multipartFile : uploadFile) {
            String uploadFileName = multipartFile.getOriginalFilename();
            String ext = uploadFileName.substring(uploadFileName.lastIndexOf("."));
            String uuid = UUID.randomUUID().toString().replace("-", "");
            // 파일명 저장
            String savedName = uuid + ext;
            String img = "/images/communityPost/" + savedName;
            savedNames.add(img);

            File saveFile = new File(uploadPath, savedName);

            try {
                multipartFile.transferTo(saveFile);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return savedNames;
    }
}
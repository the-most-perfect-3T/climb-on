package com.ohgiraffers.climbon.crew.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.util.*;

@RestController
@RequestMapping("/file")
public class FileController {

    /**
     * 에디터 내 사진 파일 업로드
     * @param uploadFile
     * @return savePath - 저장경로
     */
    @PostMapping(value = "/upload", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<String> upload(MultipartFile[] uploadFile) {
        String savePath = "C:/uploads/single";
        List<String> savedNames = new ArrayList<>();

        /*// OS 따라 구분자 분리
        String os = System.getProperty("os.name").toLowerCase();
        if (os.contains("win")){
            savePath = System.getProperty("user.dir") + "\\files\\image";
        }
        else{
            savePath = System.getProperty("user.dir") + "/files/image";
        }*/

        File uploadPath = new File(savePath);

        // 파일 저장 경로가 없으면 신규 생성
        if (!uploadPath.exists()) {
            uploadPath.mkdirs();
        }

        for (MultipartFile multipartFile : uploadFile) {
            String uploadFileName = multipartFile.getOriginalFilename();
            String uuid = UUID.randomUUID().toString().replace("-","");
            // 파일명 저장
            String savedName = uuid + "_" + uploadFileName;
            String img = "/img/multi/" + savedName;
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

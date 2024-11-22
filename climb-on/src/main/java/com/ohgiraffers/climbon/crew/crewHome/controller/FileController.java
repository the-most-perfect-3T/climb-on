package com.ohgiraffers.climbon.crew.crewHome.controller;

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

    @PostMapping(value = "/uploadCrewPic", produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> uploadCrewPic(@RequestParam MultipartFile imageFile) {
        // 파일을 저장할 경로 설정
        String filePath = "C:/climbon/crew/profile";
        System.out.println(imageFile);
        File fileDir = new File(filePath);
        if (!fileDir.exists()) {
            fileDir.mkdirs();   // 경로가 없으면 생성한다.
        }
        String originalFileName = imageFile.getOriginalFilename();
        String ext = originalFileName.substring(originalFileName.lastIndexOf("."));
        String savedName = UUID.randomUUID().toString().replace("-", "") + ext;
        String img = "/img/crewPic/" + savedName;

        try {
            imageFile.transferTo(new File(filePath + "/" + savedName));
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("크루 이미지 업로드 중 문제가 발생했습니다.111111");
        }
        return ResponseEntity.ok(img);
    }
}

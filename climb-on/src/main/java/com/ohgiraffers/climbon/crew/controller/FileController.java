package com.ohgiraffers.climbon.crew.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.util.UUID;

@RestController
@RequestMapping("/file")
public class FileController {

    /**
     * 에디터 내 사진 파일 업로드
     * @param uploadFile
     * @return savePath - 저장경로
     */
    @PostMapping("/upload")
    public String upload(MultipartFile[] uploadFile) {
        String savePath;

        // OS 따라 구분자 분리
        String os = System.getProperty("os.name").toLowerCase();
        if (os.contains("win")){
            savePath = System.getProperty("user.dir") + "\\files\\image";
        }
        else{
            savePath = System.getProperty("user.dir") + "/files/image";
        }

        File uploadPath = new File(savePath);

        // 파일 저장 경로가 없으면 신규 생성
        if (!uploadPath.exists()) {
            uploadPath.mkdirs();
        }

        for (MultipartFile multipartFile : uploadFile) {

            String uploadFileName = multipartFile.getOriginalFilename();

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
        return savePath;
    }




    /**
     * 에디터 내 사진 파일 첨부
     * @param fileName
     * @return
     */
    @ResponseBody
    @GetMapping(value = "/display")
    public ResponseEntity<byte[]> showImageGET(@RequestParam("fileName") String fileName) {

        String savePath;

        // OS 따라 구분자 분리
        String os = System.getProperty("os.name").toLowerCase();
        if (os.contains("win")){
            savePath = System.getProperty("user.dir") + "\\files\\image\\";
        }
        else{
            savePath = System.getProperty("user.dir") + "/files/image/";
        }

        // 설정한 경로로 파일 다운로드
        java.io.File file = new java.io.File(savePath + fileName);

        ResponseEntity<byte[]> result = null;

        try {

            HttpHeaders header = new HttpHeaders();
            header.add("Content-type", Files.probeContentType(file.toPath()));

            result = new ResponseEntity<>(FileCopyUtils.copyToByteArray(file), header, HttpStatus.OK);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        /*catch (NoSuchFileException e){
            log.error("No Such FileException {}", e.getFile());
        } catch (IOException e) {
            log.error(e.getMessage());
        }*/

        return result;
    }


}

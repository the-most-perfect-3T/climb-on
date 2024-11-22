package com.ohgiraffers.climbon.facilities.controller;

import com.ohgiraffers.climbon.auth.model.AuthDetail;
import com.ohgiraffers.climbon.facilities.dto.FacilityImgDTO;
import com.ohgiraffers.climbon.facilities.service.FacilitiesService;
import com.ohgiraffers.climbon.facilities.service.FacilitiesServiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/facilityImg")
public class FacilityImgController {

    @Autowired
    private FacilitiesService facilitiesService; //퍼실리티서비스

    @GetMapping("getImage")
    public ResponseEntity<List<FacilityImgDTO>> getImageById(@RequestParam int facilityId) {

        List<FacilityImgDTO> result = facilitiesService.getImageById(facilityId);
        System.out.println("여기맞지? = " + result);
        return ResponseEntity.ok(result);
    }
}

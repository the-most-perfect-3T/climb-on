/*
*   2024-11-22 최초 작성
*   작성자: 최정민
* */

package com.ohgiraffers.climbon.user.controller;

import com.ohgiraffers.climbon.auth.model.AuthDetail;
import com.ohgiraffers.climbon.facilities.service.FacilitiesService;
import com.ohgiraffers.climbon.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@RestController
@RequestMapping("/user")
public class UserRestController {

    @Autowired
    private UserService userService;
    @Autowired
    private FacilitiesService facilitiesService;

    @PostMapping("/registFacility")
    public ResponseEntity<Object> registFacility(@AuthenticationPrincipal AuthDetail userDetails,
                                                 @RequestBody Map<String, String> requestBody){

        System.out.println("요청오나?");

        // 로그인 정보 없으면
        if (userDetails == null || userDetails.getLoginUserDTO() == null) {
            return ResponseEntity.status(401).body(Map.of("message", "로그인 정보가 없습니다."));
        }

        // 유저 pk
        Integer key = userDetails.getLoginUserDTO().getId();

        String facilityName = requestBody.get("facilityName");
        System.out.println(key + " " + facilityName);
        int facilityId = facilitiesService.getFacilityIdByName(facilityName);
        System.out.println("facilityId = " + facilityId);
        int result = userService.updateFacility(key, facilityId);
        if(result > 0){
            // 성공
            return ResponseEntity.ok(Map.of("message", "홈짐이 등록되었습니다."));
        }else {
            return ResponseEntity.status(500).body(Map.of("message", "홈짐 등록에 실패했습니다."));
        }

    }
}

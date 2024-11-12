/*
 작성자: 이승환 - 2024-11-07
    설명 :
 */

package com.ohgiraffers.climbon.facilities.controller;

import com.ohgiraffers.climbon.facilities.dto.FacilitiesDTO;
import com.ohgiraffers.climbon.facilities.service.FacilitiesServiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@RestController
@RequestMapping("/facilitiesService")
public class FacilitiesServiceController {

    @Autowired
    private FacilitiesServiceService facilitiesServiceService; //퍼실리티서비스

    @GetMapping("select")
   public ModelAndView selectCategory(ModelAndView mv) {
        // 서비스 리스트 가져오기
        List<FacilitiesDTO> facilitiesList = facilitiesServiceService.facilitiesList();

        // ModelAndView 생성 (뷰 이름: select, 모델에 시설 목록 추가)
        mv.setViewName("facilities/facilities") ; // 'select'는 view 이름
        mv.addObject("facilitiesList", facilitiesList); // 모델에 데이터 추가


        return mv; // ModelAndView 반환
    }

























}

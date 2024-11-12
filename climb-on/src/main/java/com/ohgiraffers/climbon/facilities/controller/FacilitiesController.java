/*
 작성자: 이승환 - 2024-11-07
    설명 :
 */

package com.ohgiraffers.climbon.facilities.controller;

import com.ohgiraffers.climbon.facilities.dto.FacilitiesDTO;
import com.ohgiraffers.climbon.facilities.service.FacilitiesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/facilities")
public class FacilitiesController {

    @Autowired
    private FacilitiesService facilitiesService; //퍼실리티서비스

    @GetMapping("select")
    public ModelAndView selectCategory(ModelAndView mv) {
        // 시설 리스트 가져오기
        List<FacilitiesDTO> facilitiesList = facilitiesService.facilitiesList();

        // ModelAndView 생성 (뷰 이름: select, 모델에 시설 목록 추가)
        mv.setViewName("facilities/facilities") ; // 'select'는 view 이름
        mv.addObject("facilitiesList", facilitiesList); // 모델에 데이터 추가


        return mv; // ModelAndView 반환
    }
    //카테고리코드를 이용한 조회
    // 카테고리 코드에 해당하는 시설 목록을 조회하는 메서드
    @PostMapping("/categoryselect")
    public ModelAndView categorySelect(@RequestParam int categoryId ,String facilityName, ModelAndView mv) {


        // 시설 목록 조회 (서비스 계층에서 처리)
         List<FacilitiesDTO> facilitiesList = facilitiesService.categorySelect(categoryId,facilityName);

        mv.addObject("facilitiesList", facilitiesList);
        mv.setViewName("facilities/facilities");
        return mv;
    }


    //검색기능
    @PostMapping("search")
    public ModelAndView search(@RequestParam String code, ModelAndView mv) {

        List<FacilitiesDTO> facilitiesList = facilitiesService.searchOneFacility(code);
        if (Objects.isNull(facilitiesList)) {
            throw new NullPointerException();
        }
        mv.addObject("facilitiesList", facilitiesList);
        mv.setViewName("facilities/facilities");
        return mv;

    }
    //검색시 실시간
    @GetMapping("/suggestions")
    public ResponseEntity<List<FacilitiesDTO>> getSuggestions(@RequestParam String code) {

        List<FacilitiesDTO> suggestions = facilitiesService.getFacilitySuggestions(code);
        return ResponseEntity.ok(suggestions);


    }

























}

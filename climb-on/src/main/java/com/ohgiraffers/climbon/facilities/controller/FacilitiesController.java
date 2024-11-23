/*
 작성자: 이승환 - 2024-11-07
    설명 :
 */

package com.ohgiraffers.climbon.facilities.controller;

import com.ohgiraffers.climbon.auth.model.AuthDetail;
import com.ohgiraffers.climbon.facilities.dto.FacilitiesDTO;
import com.ohgiraffers.climbon.facilities.dto.FacilityFavoriteDTO;
import com.ohgiraffers.climbon.facilities.service.FacilitiesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
        System.out.println(facilitiesList);
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
    @GetMapping("/selectList")
    public ResponseEntity<List<FacilitiesDTO>> categoryList() {
        List<FacilitiesDTO> facilitiesList = facilitiesService.facilitiesList();
        return ResponseEntity.ok(facilitiesList);
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
    public ResponseEntity<List<FacilitiesDTO>> getSuggestions(@RequestParam String code) { // 이건 쿼리파라미터

        List<FacilitiesDTO> suggestions = facilitiesService.getFacilitySuggestions(code);
        return ResponseEntity.ok(suggestions);


    }

    //하나만 가져오기
    @GetMapping("facility/{id}")
    public ResponseEntity<FacilitiesDTO> getFacility(@PathVariable("id") int facilityId) { //이건 그냥 단순값

        FacilitiesDTO facilityDTO = facilitiesService.getFacility(facilityId);
        System.out.println("facilityDTO = " + facilityDTO);
        return ResponseEntity.ok(facilityDTO);
    }

    @PostMapping("/update-favorite")
    public String updateFavorite(@RequestBody Map<String, Object> requestData, @AuthenticationPrincipal AuthDetail userDetails) {
        Integer facilityId = (Integer) requestData.get("facilityId");  // JSON에서 facilityId 추출
        boolean favorite = (boolean) requestData.get("isFavorite");
        Integer userId = userDetails.getLoginUserDTO().getId();
        int result;

        if (favorite) {
            result =  facilitiesService.addFavorite(userId, facilityId);
            System.out.println("성공 추가되면1 아니면 0 = " + result);
            return "즐겨찾기 추가되었습니다.";
        } else {
            result =facilitiesService.removeFavorite(userId, facilityId);
            System.out.println("삭제 추가되면1 아니면 0 = " + result);
            return "즐겨찾기 삭제되었습니다.";
        }
    }
    @PostMapping("/getIsFavorite")
    public ResponseEntity<Integer> getIsFavorite(@RequestParam int id, @AuthenticationPrincipal AuthDetail userDetails) {
        Integer userId = userDetails.getLoginUserDTO().getId();
        Integer result = facilitiesService.getIsFavorite(id,userId);
        System.out.println("여기맞지? = " + result);
        return ResponseEntity.ok(result);
    }











}

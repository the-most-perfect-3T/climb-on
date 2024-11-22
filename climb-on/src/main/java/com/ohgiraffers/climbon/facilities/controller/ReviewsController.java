package com.ohgiraffers.climbon.facilities.controller;

import com.ohgiraffers.climbon.auth.model.AuthDetail;
import com.ohgiraffers.climbon.facilities.dto.FacilitiesDTO;
import com.ohgiraffers.climbon.facilities.dto.ReviewDTO;
import com.ohgiraffers.climbon.facilities.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/Review")
public class ReviewsController {

    @Autowired
    private ReviewService reviewService;

    @PostMapping("/Reviews")
    public ResponseEntity<List<ReviewDTO>> getReview(@RequestBody Map<String, Object> requestBody) {
        Integer facilityId = (Integer) requestBody.get("code");
        System.out.println("facilityId = " + facilityId);
        List<ReviewDTO> reviewList = reviewService.getReview(String.valueOf(facilityId));

        return ResponseEntity.ok(reviewList);

    }

    @PostMapping("/getIsFavorite")
    public ResponseEntity<Integer> getIsFavorite(@RequestParam int id, @AuthenticationPrincipal AuthDetail userDetails) {
        Integer userId = userDetails.getLoginUserDTO().getId();
        Integer result = reviewService.getIsFavorite(id,userId);
        System.out.println("여기맞지? = " + result);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/update-favorite")
    public String updateFavorite(@RequestBody Map<String, Object> requestData, @AuthenticationPrincipal AuthDetail userDetails) {
        Integer facilityId = (Integer) requestData.get("facilityId");  // JSON에서 facilityId 추출
        boolean favorite = (boolean) requestData.get("isFavorite");
        Integer userId = userDetails.getLoginUserDTO().getId();
        int result;

        if (favorite) {
            result =  reviewService.addFavorite(userId, facilityId);
            System.out.println("성공 추가되면1 아니면 0 = " + result);
            return "즐겨찾기 추가되었습니다.";
        } else {
            result =reviewService.removeFavorite(userId, facilityId);
            System.out.println("삭제 추가되면1 아니면 0 = " + result);
            return "즐겨찾기 삭제되었습니다.";
        }
    }
}

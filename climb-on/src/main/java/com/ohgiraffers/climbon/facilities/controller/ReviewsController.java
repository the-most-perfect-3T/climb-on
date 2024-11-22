package com.ohgiraffers.climbon.facilities.controller;

import com.ohgiraffers.climbon.auth.model.AuthDetail;
import com.ohgiraffers.climbon.facilities.dto.ReviewDTO;
import com.ohgiraffers.climbon.facilities.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/Review")
public class ReviewsController {

    @Autowired
    private ReviewService reviewService;

    @GetMapping("/Reviews")
    public ResponseEntity<List<ReviewDTO>> getReview(@RequestParam Integer code) {
        System.out.println("facilityId = " + code);
        List<ReviewDTO> reviewList = reviewService.getReview(String.valueOf(code));

        return ResponseEntity.ok(reviewList);
    }

    @PostMapping("/getIsFavorite")
    public ResponseEntity<Integer> getIsFavorite(@RequestParam int id, @AuthenticationPrincipal AuthDetail userDetails) {
        Integer userId = userDetails.getLoginUserDTO().getId();
        Integer result = reviewService.getIsFavorite(id, userId);
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
            result = reviewService.addFavorite(userId, facilityId);
            System.out.println("성공 추가되면1 아니면 0 = " + result);
            return "즐겨찾기 추가되었습니다.";
        } else {
            result = reviewService.removeFavorite(userId, facilityId);
            System.out.println("삭제 추가되면1 아니면 0 = " + result);
            return "즐겨찾기 삭제되었습니다.";
        }
    }

    @PostMapping("/reviewInsert")
    public ResponseEntity<Integer> reviewInsert(@AuthenticationPrincipal AuthDetail userDetails,
                                     @ModelAttribute ReviewDTO reviewDTO) {

        Integer userId = userDetails.getLoginUserDTO().getId();
        // 리뷰를 DB에 저장하는 서비스 호출
        int result =  reviewService.reviewInsert(reviewDTO,userId);

        // 리뷰 저장 후, 리뷰 목록 페이지로 리다이렉트
        return ResponseEntity.ok(result);

    }
}
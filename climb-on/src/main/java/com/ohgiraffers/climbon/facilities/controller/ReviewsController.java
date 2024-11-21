package com.ohgiraffers.climbon.facilities.controller;

import com.ohgiraffers.climbon.facilities.dto.FacilitiesDTO;
import com.ohgiraffers.climbon.facilities.dto.ReviewDTO;
import com.ohgiraffers.climbon.facilities.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
}

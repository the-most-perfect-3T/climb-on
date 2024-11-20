package com.ohgiraffers.climbon.facilities.controller;

import com.ohgiraffers.climbon.facilities.dto.FacilitiesDTO;
import com.ohgiraffers.climbon.facilities.dto.ReviewDTO;
import com.ohgiraffers.climbon.facilities.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/Review")
public class ReviewsController {

    @Autowired
    private ReviewService reviewService;

    @GetMapping("/Reviews")
    public ResponseEntity<List<ReviewDTO>> getReview(@RequestParam String code) { // 이건 쿼리파라미터

        List<ReviewDTO> reviewList = reviewService.getReview(code);

        return ResponseEntity.ok(reviewList);



    }
}

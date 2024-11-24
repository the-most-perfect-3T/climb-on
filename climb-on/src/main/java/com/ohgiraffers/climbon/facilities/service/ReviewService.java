package com.ohgiraffers.climbon.facilities.service;

import com.ohgiraffers.climbon.facilities.dao.FacilitiesDAO;
import com.ohgiraffers.climbon.facilities.dao.ReviewDAO;
import com.ohgiraffers.climbon.facilities.dto.FacilitiesDTO;
import com.ohgiraffers.climbon.facilities.dto.ReviewDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class ReviewService {

    @Autowired
    private ReviewDAO reviewDAO;

    public List<ReviewDTO> getReview(String code) {
        List<ReviewDTO> ReviewList =reviewDAO.getReview(code);
        System.out.println("ReviewList = " + ReviewList);
        return ReviewList;
    }
    public List<ReviewDTO> getReviewByUserId(int userId) {
        List<ReviewDTO> ReviewList =reviewDAO.getReviewByUserId(userId);
        System.out.println("ReviewList = " + ReviewList);
        return ReviewList;

    }

    public Integer getIsFavorite(int id, Integer userId) {
        return reviewDAO.getIsFavorite(id,userId);
    }

    public int addFavorite(Integer userId, Integer facilityId) {
        Map<String, Integer> params = new HashMap<>();
        params.put("userId", userId);
        params.put("id", facilityId);

        int result = reviewDAO.addFavorite(params);
        System.out.println("result = " + result);
        return result;
    }

    public int removeFavorite(Integer userId, Integer facilityId) {
        Map<String, Integer> params = new HashMap<>();
        params.put("userId", userId);
        params.put("id", facilityId);

        int result = reviewDAO.removeFavorite(params);
        System.out.println("result = " + result);
        return result;
    }

    public int reviewInsert(ReviewDTO reviewDTO, int id) {
        reviewDTO.setReviewerId(id);
        return reviewDAO.reviewInsert(reviewDTO);
    }

    public int reviewUpdate(ReviewDTO reviewDTO, int id) {
        reviewDTO.setReviewerId(id);
        return reviewDAO.reviewUpdate(reviewDTO);
    }
}

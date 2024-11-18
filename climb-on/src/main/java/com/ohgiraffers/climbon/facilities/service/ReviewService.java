package com.ohgiraffers.climbon.facilities.service;

import com.ohgiraffers.climbon.facilities.dao.FacilitiesDAO;
import com.ohgiraffers.climbon.facilities.dao.ReviewDAO;
import com.ohgiraffers.climbon.facilities.dto.FacilitiesDTO;
import com.ohgiraffers.climbon.facilities.dto.ReviewDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;



@Service
public class ReviewService {

    @Autowired
    private ReviewDAO reviewDAO;

    public List<ReviewDTO> getReview(String code) {
        List<ReviewDTO> ReviewList =reviewDAO.getReview(code);
        return ReviewList;
    }
}

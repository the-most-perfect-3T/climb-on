package com.ohgiraffers.climbon.facilities.dao;

import com.ohgiraffers.climbon.facilities.dto.ReviewDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface ReviewDAO {
    List<ReviewDTO> getReview(String code);

    Integer getIsFavorite(int id, Integer userId);

    int addFavorite(Map<String, Integer> params);

    int removeFavorite(Map<String, Integer> params);

    int reviewInsert(ReviewDTO reviewDTO);

    int reviewUpdate(ReviewDTO reviewDTO);

    List<ReviewDTO> getReviewByUserId(int userId);
}

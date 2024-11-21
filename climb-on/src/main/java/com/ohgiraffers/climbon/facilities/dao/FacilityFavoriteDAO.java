package com.ohgiraffers.climbon.facilities.dao;

import org.apache.ibatis.annotations.Mapper;

import java.util.Map;

@Mapper
public interface FacilityFavoriteDAO {
    int addFavorite(Map<String, Integer> params);

    int getIsFavorite(int id, Integer userId);

    int removeFavorite(Map<String, Integer> params);
}

package com.ohgiraffers.climbon.calendar.dao;

import com.ohgiraffers.climbon.community.dto.PostDTO;
import com.ohgiraffers.climbon.facilities.dto.FacilitiesDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MainMapper
{
    List<PostDTO> getRecentPosts();
    List<PostDTO> getPopularPosts();

    List<PostDTO> getNotificationPosts();

    List<FacilitiesDTO> getFacilityInfo();

    List<PostDTO> getRecentPostsByCategory(String category);
}

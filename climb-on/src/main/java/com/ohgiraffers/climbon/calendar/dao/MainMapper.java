package com.ohgiraffers.climbon.calendar.dao;

import com.ohgiraffers.climbon.community.dto.PostDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MainMapper
{
    List<PostDTO> getRecentPosts();
    List<PostDTO> getPopularPosts();
}

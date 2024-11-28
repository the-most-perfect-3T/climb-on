package com.ohgiraffers.climbon.calendar.service;

import com.ohgiraffers.climbon.calendar.dao.MainMapper;
import com.ohgiraffers.climbon.community.dto.PostDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MainService
{
    @Autowired
    private MainMapper mainMapper;

    // 카테고리 전체 기준으로 최신, 가장 인기글
    public List<PostDTO> getRecentPosts()
    {
        return mainMapper.getRecentPosts();
    }

    public List<PostDTO> getPopularPosts()
    {
        return mainMapper.getPopularPosts();
    }

    public List<PostDTO> getNotificationPosts()
    {
        return mainMapper.getNotificationPosts();
    }
}

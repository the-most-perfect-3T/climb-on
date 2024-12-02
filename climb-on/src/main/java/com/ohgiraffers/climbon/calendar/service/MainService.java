package com.ohgiraffers.climbon.calendar.service;

import com.ohgiraffers.climbon.calendar.dao.MainMapper;
import com.ohgiraffers.climbon.community.dto.PostDTO;
import com.ohgiraffers.climbon.facilities.dto.FacilitiesDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    public List<FacilitiesDTO> getFacilityInfo()
    {
        List<FacilitiesDTO> allFacilities = mainMapper.getFacilityInfo();
        Set<Integer> set = new HashSet<>();
        while(set.size() <3)
        {
            int randNum = (int)(Math.random() * allFacilities.size());
            set.add(randNum);
        }

        List<FacilitiesDTO> recommendedFacilities = new ArrayList<>();
        for(int i : set)
        {
            recommendedFacilities.add(allFacilities.get(i));
        }
        return recommendedFacilities;
    }

    public List<PostDTO> getRecentPostsByCategory(String category)
    {
        return mainMapper.getRecentPostsByCategory(category);
    }

    public float getFacilityRate(Integer id)
    {
        return mainMapper.getFacilityRate(id);
    }
}

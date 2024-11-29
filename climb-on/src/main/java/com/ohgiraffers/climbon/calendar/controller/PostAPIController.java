package com.ohgiraffers.climbon.calendar.controller;

import com.ohgiraffers.climbon.calendar.service.MainService;
import com.ohgiraffers.climbon.community.dto.PostDTO;
import com.ohgiraffers.climbon.facilities.dto.FacilitiesDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
public class PostAPIController
{
    @Autowired
    private MainService mainService;

    @GetMapping("/recent/paginated")
    public List<PostDTO> getRecentPosts()
    {
        // 최근 6개 포스트 들고 와서 보여주기
        // 포스트 카테고리 별로 검사 ...
        return mainService.getRecentPosts();
    }

    @GetMapping("/popular")
    public List<PostDTO> getPopularPosts()
    {
        // 포스트 인기순으로 (likes 내림차순)
        return mainService.getPopularPosts();
    }

    @GetMapping("/notification")
    public List<PostDTO> getNotificationPosts()
    {
        return mainService.getNotificationPosts();
    }

    @GetMapping("/facilities")
    public List<FacilitiesDTO> getFacilityInfo(ModelAndView mv)
    {
        return mainService.getFacilityInfo();
    }
}

package com.ohgiraffers.climbon.calendar.controller;

import com.ohgiraffers.climbon.auth.model.AuthDetail;
import com.ohgiraffers.climbon.calendar.service.MainService;
import com.ohgiraffers.climbon.community.dto.PostDTO;
import com.ohgiraffers.climbon.facilities.dto.FacilitiesDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/posts")
public class PostAPIController
{
    @Autowired
    private MainService mainService;

    @GetMapping("/recent/paginated")
    public ResponseEntity<List<PostDTO>> getRecentPosts(@RequestParam(required = false, value = "category") String category)
    {
        List<PostDTO> recentPosts = new ArrayList<>();
        System.out.println("category 제발요 = " + category);

        if(category == null || category.isEmpty()){
            recentPosts = mainService.getRecentPosts();
        }
        if(category != null && !category.isEmpty())
        {
            recentPosts = mainService.getRecentPostsByCategory(category);
        }
        return ResponseEntity.ok(recentPosts);
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
    public List<FacilitiesDTO> getFacilityInfo()
    {
        return mainService.getFacilityInfo();
    }

    @GetMapping("/community/posts")
    public ModelAndView setPostsUrl(ModelAndView mv, @AuthenticationPrincipal AuthDetail userDetail)
    {
        try{
            if(userDetail == null || userDetail.getLoginUserDTO() == null){
                mv.addObject("message", "해당 게시글은 로그인 후 볼 수 있습니다.  \n로그인해주세요.");
                mv.setViewName("community");
            }
            return mv;
        }
        catch (Exception e){
            e.printStackTrace();
            mv.addObject("message", e.getMessage());
            mv.setViewName("redirect:/error/500");
            return mv;
        }
    }

    @GetMapping("/rate/{id}")
    public ResponseEntity<Object> getFacilityRate(@PathVariable(name = "id") Integer id){
        float result = mainService.getFacilityRate(id);
        return ResponseEntity.ok(result);
    }
}

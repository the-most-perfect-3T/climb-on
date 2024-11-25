package com.ohgiraffers.climbon.search.controller;

import com.ohgiraffers.climbon.search.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/search")
public class SearchController {

    @Autowired
    private SearchService searchService;

    @GetMapping
    public String search(@RequestParam(value = "keyword", required = false) String keyword, Model model){
        // 검색어가 없으면 기본값 설정
        if (keyword == null || keyword.isEmpty()) {
            model.addAttribute("keyword", ""); // 빈 검색어로 기본 페이지 표시
            model.addAttribute("communityPosts", List.of()); // 빈 리스트 전달
            model.addAttribute("facilities", List.of());
            model.addAttribute("crewNames", List.of());
            return "search/searchForm"; // 검색 폼과 빈 결과 페이지
        }

        Map<String, Object> searchResults = searchService.searchAll(keyword);

        model.addAttribute("keyword", keyword);
        model.addAttribute("communityPosts", searchResults.get("communityPosts"));
        model.addAttribute("facilities", searchResults.get("facilities"));
        model.addAttribute("crewNames", searchResults.get("crewNames"));

        return "search/searchForm";
    }
}

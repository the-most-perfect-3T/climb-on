package com.ohgiraffers.climbon.search.controller;

import com.ohgiraffers.climbon.community.dto.PostDTO;
import com.ohgiraffers.climbon.crew.crewHome.dto.CrewDTO;
import com.ohgiraffers.climbon.facilities.dto.FacilitiesDTO;
import com.ohgiraffers.climbon.search.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;
import java.util.Objects;

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
            model.addAttribute("crewCount", 0);
            return "search/searchForm"; // 검색 폼과 빈 결과 페이지
        }

        Map<String, Object> searchResults = searchService.searchAll(keyword);
        // 전체 게시글 리스트
        List<CrewDTO> crewNames = (List<CrewDTO>) searchResults.get("crewNames");
        List<FacilitiesDTO> facilities = (List<FacilitiesDTO>) searchResults.get("facilities");
        List<PostDTO> communityPosts = (List<PostDTO>) searchResults.get("communityPosts");
        // 제한된 게시글 리스트
        List<CrewDTO> limitedCrewNames = (List<CrewDTO>) searchResults.get("limitedCrewNames");
        List<PostDTO> limitedCommunityPosts = (List<PostDTO>) searchResults.get("limitedCommunityPosts");
        List<FacilitiesDTO> limitedfacilities = (List<FacilitiesDTO>) searchResults.get("limitedfacilities");

        for (PostDTO post : communityPosts) {
            // 각 게시글의 userId를 사용해 닉네임 조회 후 설정
            String userNickname =  searchService.getUserNicknameById(post.getUserId());
            post.setUserNickname(userNickname);
            String htmlContent = post.getContent();
            String plainText = htmlContent.replaceAll("<[^>]*>", "");
            post.setContent(plainText);
        }

        model.addAttribute("keyword", keyword);
        model.addAttribute("communityPosts",communityPosts);
        model.addAttribute("facilities", facilities);
        model.addAttribute("crewNames", crewNames);
        model.addAttribute("crewCount", crewNames.size());
        model.addAttribute("limitedCrewNames", limitedCrewNames);
        model.addAttribute("limitedfacilities", limitedfacilities);
        model.addAttribute("limitedCommunityPosts", limitedCommunityPosts);
        model.addAttribute("facilitiesCount", facilities.size());
        model.addAttribute("communityPostCount", communityPosts.size());

        return "search/searchForm";
    }

    @GetMapping("/loadMorePosts")
    @ResponseBody
    public ResponseEntity<?> loadMorePosts(
            @RequestParam("currentCount") int currentCount,
            @RequestParam("limit") int limit,
            @RequestParam(value = "keyword", required = false) String keyword) {
        // 추가 데이터를 가져오는 로직
        List<PostDTO> additionalPosts = searchService.loadMoreCommunityPosts(keyword, currentCount, limit);
        if (additionalPosts.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("검색 결과를 모두 불러왔습니다.");
        }
        return ResponseEntity.ok(additionalPosts);
    }

    @GetMapping("/loadMoreFacilities")
    @ResponseBody
    public ResponseEntity<?> loadMoreFacilities(
            @RequestParam("currentCount") int currentCount,
            @RequestParam("limit") int limit,
            @RequestParam(value = "keyword", required = false) String keyword) {
        // 추가 데이터를 가져오는 로직
        List<FacilitiesDTO> additionalFacilities = searchService.loadMoreFacilities(keyword, currentCount, limit);
        if (additionalFacilities.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("검색 결과를 모두 불러왔습니다.");
        }
        return ResponseEntity.ok(additionalFacilities);
    }

}

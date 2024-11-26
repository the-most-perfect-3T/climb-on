package com.ohgiraffers.climbon.search.service;

import com.ohgiraffers.climbon.community.dao.PostDAO;
import com.ohgiraffers.climbon.community.dto.PostDTO;
import com.ohgiraffers.climbon.crew.crewHome.dto.CrewDTO;
import com.ohgiraffers.climbon.facilities.dto.FacilitiesDTO;
import com.ohgiraffers.climbon.search.dao.SearchDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SearchService {

    @Autowired
    private SearchDAO searchDAO;


    public Map<String, Object> searchAll(String keyword){
        // 커뮤니티 게시글 검색
        List<PostDTO> communityPosts = searchDAO.searchCommunityPosts(keyword);

        //시설 검색
        List<FacilitiesDTO> facilities = searchDAO.searchFacilities(keyword);

        // 크루 검색
        List<CrewDTO> crewNames = searchDAO.searchCrewNames(keyword);

        // 크루 제한 설정 (최대 5개만 반환)
        int crewLimit = Math.min(5, crewNames.size());
        List<CrewDTO> crewLimited = crewNames.subList(0, crewLimit);

        // 시설 제한 설정 (최대 3개만 반환)
        int facilitiesLimit = Math.min(3, facilities.size());
        List<FacilitiesDTO> facilitiesLimited = facilities.subList(0, facilitiesLimit);

        // 게시글 제한 설정 (최대 2개만 반환)
        int postLimit = Math.min(2, communityPosts.size());
        List<PostDTO> limitedPosts = communityPosts.subList(0, postLimit);


        // 결과를 Map으로 묶어서 반환
        Map<String, Object> result = new HashMap<>();
        result.put("communityPosts", communityPosts); // 전체 게시글
        result.put("facilities", facilities);
        result.put("crewNames", crewNames);
        // 제한된 게시글
        result.put("limitedCrewNames", crewLimited);
        result.put("limitedCommunityPosts", limitedPosts);
        result.put("limitedfacilities", facilitiesLimited);

        return result;
    }

}

package com.ohgiraffers.climbon.search.service;

import com.ohgiraffers.climbon.community.dao.PostDAO;
import com.ohgiraffers.climbon.community.dto.PostDTO;
import com.ohgiraffers.climbon.crew.crewHome.dto.CrewDTO;
import com.ohgiraffers.climbon.facilities.dto.FacilitiesDTO;
import com.ohgiraffers.climbon.search.dao.SearchDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

        // 결과를 Map으로 묶어서 반환
        Map<String, Object> result = new HashMap<>();
        result.put("communityPosts", communityPosts);
        result.put("facilities", facilities);
        result.put("crewNames", crewNames);

        return result;
    }

}

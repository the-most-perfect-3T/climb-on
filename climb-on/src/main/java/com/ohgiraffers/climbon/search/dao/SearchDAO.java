package com.ohgiraffers.climbon.search.dao;

import com.ohgiraffers.climbon.community.dto.PostDTO;
import com.ohgiraffers.climbon.crew.crewHome.dto.CrewDTO;
import com.ohgiraffers.climbon.facilities.dto.FacilitiesDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SearchDAO {
    // 커뮤니티 게시글 검색
    @Select("SELECT * FROM community_posts WHERE title LIKE CONCAT ('%', #{keyword}, '%') OR content LIKE CONCAT('%', #{keyword}, '%')")
    List<PostDTO> searchCommunityPosts(@Param("keyword") String keyword);

    // 시설 검색
    @Select("SELECT * FROM facilities WHERE facility_name LIKE CONCAT('%', #{keyword}, '%') OR address LIKE CONCAT('%', #{keyword}, '%')")
    List<FacilitiesDTO> searchFacilities(@Param("keyword") String keyword);

    // 크루 검색
    @Select("SELECT * FROM crews WHERE crew_name LIKE CONCAT('%', #{keyword}, '%')")
    List<CrewDTO> searchCrewNames(@Param("keyword") String keyword);
}

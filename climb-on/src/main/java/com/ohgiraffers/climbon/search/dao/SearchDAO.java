package com.ohgiraffers.climbon.search.dao;

import com.ohgiraffers.climbon.community.dto.PostDTO;
import com.ohgiraffers.climbon.crew.crewHome.dto.CrewDTO;
import com.ohgiraffers.climbon.facilities.dto.FacilitiesDTO;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface SearchDAO {

    // 다른 매퍼에서 인라인으로 쓸때 인라인에서 Results로 매핑시켜주는게 중요하다!

    // 커뮤니티 게시글 검색
    @Select("SELECT * FROM community_posts WHERE title LIKE CONCAT ('%', #{keyword}, '%') OR content LIKE CONCAT('%', #{keyword}, '%')")
    @Results(id= "PostResultMap", value= {
            @Result(column = "user_id", property = "userId"), @Result(column = "created_at", property="createdAt"), @Result(column = "updated_at", property="updatedAt")
            , @Result(column = "view_count", property = "viewCount"), @Result(column = "img_url", property = "imageUrl"), @Result(column = "is_anonymous", property = "isAnonymous")
    })
    List<PostDTO> searchCommunityPosts(@Param("keyword") String keyword);

    // 시설 검색
    @Select("SELECT * FROM facilities WHERE facility_name LIKE CONCAT('%', #{keyword}, '%') OR address LIKE CONCAT('%', #{keyword}, '%')")
    @Results(id= "FacilitiesResultMap", value= {
            @Result(column = "facility_name", property = "facilityName")
    })
    List<FacilitiesDTO> searchFacilities(@Param("keyword") String keyword);

    // 크루 검색
    @Select("SELECT * FROM crews WHERE crew_name LIKE CONCAT('%', #{keyword}, '%')")
    @Results(id = "CrewResultMap", value= {
            @Result(column = "crew_name", property = "crewName"), @Result(column = "img_url", property = "imgUrl")
    })
    List<CrewDTO> searchCrewNames(@Param("keyword") String keyword);

    // 더보기(페이징처리)를 위한 쿼리
    @Select("SELECT * FROM community_posts " +
            "WHERE title LIKE CONCAT('%', #{keyword}, '%') " +
            "OR content LIKE CONCAT('%', #{keyword}, '%') " +
            "LIMIT #{currentCount}, #{limit}")
    @Results(id = "postResultMap", value = {
            @Result(column = "id", property = "id"),
            @Result(column = "user_id", property = "userId"),
            @Result(column = "title", property = "title"),
            @Result(column = "content", property = "content"),
            @Result(column = "category", property = "category"),
            @Result(column = "created_at", property = "createdAt", javaType = java.time.LocalDateTime.class),
            @Result(column = "updated_at", property = "updatedAt", javaType = java.time.LocalDateTime.class),
            @Result(column = "view_count", property = "viewCount"),
            @Result(column = "img_url", property = "imageUrl"),
            @Result(column = "is_anonymous", property = "isAnonymous"),
            @Result(column = "like_count", property = "heartsCount"),
            @Result(column = "status", property = "status"),
            @Result(column = "event_start_date", property = "eventStartDate"),
            @Result(column = "event_end_date", property = "eventEndDate")
    })

    List<PostDTO> searchCommunityPostsPaged(@Param("keyword") String keyword, @Param("currentCount") int currentCount, @Param("limit") int limit);

}

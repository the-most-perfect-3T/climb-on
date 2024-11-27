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

}

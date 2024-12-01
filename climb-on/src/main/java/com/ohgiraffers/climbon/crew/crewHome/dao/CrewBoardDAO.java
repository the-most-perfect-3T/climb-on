package com.ohgiraffers.climbon.crew.crewHome.dao;

import com.ohgiraffers.climbon.community.dto.PostDTO;
import com.ohgiraffers.climbon.crew.crewHome.dto.CrewBoardDTO;
import com.ohgiraffers.climbon.crew.crewHome.dto.CrewCommentDTO;
import com.ohgiraffers.climbon.crew.crewHome.dto.CrewPostDTO;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CrewBoardDAO {

    int insertPost(CrewBoardDTO crewBoardDTO);

    CrewBoardDTO selectLastPost();

    Integer getCrewCode(int id);

    CrewBoardDTO selectOnePostById(int id);

    int updatePostById(CrewBoardDTO crewBoardDTO);



    List<CrewPostDTO> getFixedPostsByCategory(@Param("category") String category, @Param("limit") int limit);

    // @Param 어노테이션을 사용해 MyBatis에서 offset, limit(pageSize로 설정), category 파라미터를 전달받는다.
    // LIMIT과 OFFSET을 사용하여 특정 페이지에 해당하는 게시글만 가져온다. //검색어 추가 //정렬조건 추가
    // 검색어와 카테고리를 기반으로 페이징 처리 및 정렬 조건에 따라 게시글 목록을 가져오는 메소드
    List<CrewPostDTO> getPostsByPageAndCategoryAndSearch(@Param("offset") int offset, @Param("limit") int pageSize,
                                                     @Param("category") String category, @Param("searchKeyword") String searchKeyword,
                                                     @Param("sort") String sort, /*@Param("dday") String dday,*/ @Param("status") Boolean status);

    @Select("SELECT nickname FROM users WHERE id = #{userId}")
    String getUserNicknameById(@Param("userId") Integer userId);

    //전체 게시글 수를 반환하여 페이지 수 계산에 사용된다. //검색어 추가
    int getTotalPostCount(@Param("category") String category, @Param("searchKeyword") String searchKeyword);

    @Select("SELECT crew_name FROM crews WHERE id = #{crewCode}")
    String getCrewNameByCrewCode(Integer crewCode);

    void incrementViewCount(Integer postId);

    CrewPostDTO getPostById(Integer postId);

    @Select("SELECT COUNT(*) > 0 FROM user_post_heart WHERE post_code2 = #{postId} AND user_code = #{userId}") //매퍼대신 여기에 쿼리문 작성
    boolean isPostLikedByUser(@Param("postId") Integer postId, @Param("userId") Integer userId);

    @Select("SELECT profile_pic FROM users WHERE id = #{userId}")
    String getUserProfilePicById(Integer userId);


    List<CrewCommentDTO> getCommentsByPostId(Integer postId);

    CrewPostDTO getPreviousPost(@Param("postId") Integer postId);

    CrewPostDTO getNextPost(@Param("postId") Integer postId);

    int insertComment(CrewCommentDTO comment);

    int updateComment(CrewCommentDTO comment);

    int deleteComment(CrewCommentDTO comment);

    int getJustAddedPostById(int id);

    @Select("SELECT COUNT(*) FROM user_post_heart WHERE post_code2 = #{postId} AND user_code = #{userId}")
    boolean hasUserLikedPost(@Param("postId") int postId, @Param("userId") int userId);

    @Update("UPDATE crew_posts SET like_count = like_count + 1 WHERE id = #{postId}")
    void incrementHearts(@Param("postId") int postId);

    @Update("UPDATE crew_posts SET like_count = like_count - 1 WHERE id = #{postId}")
    void decrementHearts(@Param("postId") int postId);

    @Delete("DELETE FROM user_post_heart WHERE post_code2 = #{postId} AND user_code = #{userId}")
    void removeLike(int postId, Integer userId);

    @Insert("INSERT INTO user_post_heart (post_code2, user_code, category) VALUES (#{postId}, #{userId}, 2)")
    void addLike(int postId, Integer userId);

    List<CrewCommentDTO> getCommentsById(Integer id);

    void deletePost(Integer postId);

    void deleteCommentsByPostId(Integer postId);

    int getCrewCodeByPostId(Integer postId);
}

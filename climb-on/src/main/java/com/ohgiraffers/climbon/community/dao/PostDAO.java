package com.ohgiraffers.climbon.community.dao;

import com.ohgiraffers.climbon.community.dto.CommentDTO;
import com.ohgiraffers.climbon.community.dto.PostDTO;
import org.apache.ibatis.annotations.*;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Mapper
public interface PostDAO {

    PostDTO getPostById(Integer id);

    void insertPost(PostDTO post);

    void updatePost(PostDTO post);

    void deletePost(Integer id);

    void incrementViewCount(Integer id); // 조회수 증가 메소드

    // @Param 어노테이션을 사용해 MyBatis에서 offset, limit(pageSize로 설정), category 파라미터를 전달받는다. // LIMIT과 OFFSET을 사용하여 특정 페이지에 해당하는 게시글만 가져온다. //검색어 추가 //정렬조건 추가
    // 검색어와 카테고리를 기반으로 페이징 처리 및 정렬 조건에 따라 게시글 목록을 가져오는 메소드
    List<PostDTO> getPostsByPageAndCategoryAndSearch(@Param("offset") int offset, @Param("limit") int pageSize, @Param("category") String category, @Param("searchKeyword") String searchKeyword, @Param("sort") String sort, @Param("dday") String dday, @Param("status") Boolean status);

    int getTotalPostCount(@Param("category") String category, @Param("searchKeyword") String searchKeyword); //전체 게시글 수를 반환하여 페이지 수 계산에 사용된다. //검색어 추가

    PostDTO getPreviousPost(@Param("id") Integer id);

    PostDTO getNextPost(@Param("id") Integer id);

    List<CommentDTO> getCommentsByPostId(Integer postId);

    void insertComment(CommentDTO comment);

    @Select("SELECT id FROM users WHERE user_id = #{userId}")
    Integer getUserIdByUserName(@Param("userId") String userId);

    @Select("SELECT nickname FROM users WHERE id = #{userId}")
    String getUserNicknameById(@Param("userId") Integer userId);

    @Select("SELECT id FROM users WHERE user_id = #{email}")
    Integer getUserIdByEmail(@Param("email") String email);

    @Update("UPDATE community_posts SET like_count = like_count + 1 WHERE id = #{postId}")
    void incrementHearts(@Param("postId") int postId);

    @Update("UPDATE community_posts SET like_count = like_count - 1 WHERE id = #{postId}")
    void decrementHearts(@Param("postId") int postId);

    @Select("SELECT COUNT(*) FROM user_post_heart WHERE post_code = #{postId} AND user_code = #{userId}")
    boolean hasUserLikedPost(@Param("postId") int postId, @Param("userId") int userId);

    @Delete("DELETE FROM user_post_heart WHERE post_code = #{postId} AND user_code = #{userId}")
    void removeLike(int postId, Integer userId);

    @Insert("INSERT INTO user_post_heart (post_code, user_code) VALUES (#{postId}, #{userId})")  // 매퍼대신 여기에 쿼리문 작성
    void addLike(int postId, Integer userId);

    @Select("SELECT COUNT(*) > 0 FROM user_post_heart WHERE post_code = #{postId} AND user_code = #{userId}") //매퍼대신 여기에 쿼리문 작성
    boolean isPostLikedByUser(@Param("postId") Integer id, @Param("userId") Integer userId);

    void updateComment(CommentDTO comment);

    void deleteComment(CommentDTO comment);

    // 게시글과 댓글 연동 삭제를 위한 댓글 삭제 메소드 추가
    void deleteCommentsByPostId(Integer postId);

    List<PostDTO> getFixedPostsByCategory(@Param("category") String category, @Param("limit") int limit);

}

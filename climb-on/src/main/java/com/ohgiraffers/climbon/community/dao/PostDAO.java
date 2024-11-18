package com.ohgiraffers.climbon.community.dao;

import com.ohgiraffers.climbon.community.dto.CommentDTO;
import com.ohgiraffers.climbon.community.dto.PostDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

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
    List<PostDTO> getPostsByPageAndCategoryAndSearch(@Param("offset")int offset, @Param("limit") int pageSize, @Param("category") String category, @Param("searchKeyword") String searchKeyword, @Param("sort") String sort, @Param("dday") String dday);

    int getTotalPostCount(@Param("category") String category, @Param("searchKeyword") String searchKeyword); //전체 게시글 수를 반환하여 페이지 수 계산에 사용된다. //검색어 추가

    PostDTO getPreviousPost(@Param("id") Integer id);

    PostDTO getNextPost(@Param("id") Integer id);

    List<CommentDTO> getCommentsByPostId(Integer postId);

    void insertComment(CommentDTO comment);
}

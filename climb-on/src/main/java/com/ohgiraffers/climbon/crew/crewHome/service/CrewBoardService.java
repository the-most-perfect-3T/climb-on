package com.ohgiraffers.climbon.crew.crewHome.service;

import com.ohgiraffers.climbon.community.dto.CommentDTO;
import com.ohgiraffers.climbon.community.dto.PostDTO;
import com.ohgiraffers.climbon.crew.crewHome.dao.CrewBoardDAO;
import com.ohgiraffers.climbon.crew.crewHome.dto.CrewBoardDTO;
import com.ohgiraffers.climbon.crew.crewHome.dto.CrewPostDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CrewBoardService {

    @Autowired
    private CrewBoardDAO crewBoardDAO;

    public int insertPost(CrewBoardDTO crewBoardDTO) {
        int result = crewBoardDAO.insertPost(crewBoardDTO);
        return result;
    }

    public CrewBoardDTO selectLastPost() {
        return crewBoardDAO.selectLastPost();
    }


    public Integer getCrewCode(int id) {
        return crewBoardDAO.getCrewCode(id);
    }

    public CrewBoardDTO selectOnePostById(int id) {
        return crewBoardDAO.selectOnePostById(id);
    }

    public int updatePostById(CrewBoardDTO crewBoardDTO) {
        return crewBoardDAO.updatePostById(crewBoardDTO);
    }




    public List<CrewPostDTO> getPostsByPageAndCategoryAndSearch(int page, int pageSize, String category, String searchKeyword, String sort, Boolean status) {
        // 페이지 번호에 맞는 시작 위치 ex) 2page 면 16번째 게시글부터 불러옴 (첫번째 게시글 위치로)
        int offset = (page - 1) * pageSize;

        // 1. 공지 게시글 (2개 고정)
        List<CrewPostDTO> noticePosts = crewBoardDAO.getFixedPostsByCategory("공지", 3);

        // 2. 일반 게시글 (페이징 적용)
        List<CrewPostDTO> posts = crewBoardDAO.getPostsByPageAndCategoryAndSearch(offset, pageSize, category, searchKeyword, sort, status);
        // 해당 페이지의 게시글을 가져오기 위해 offset 값을 계산하고, 이를 기반으로 DAO에서 데이터 가져옴. ,searchKeyword 파라미터 추가

        // 3. 게시글 합치기
        List<CrewPostDTO> allPosts = new ArrayList<>();
        allPosts.addAll(noticePosts);
        allPosts.addAll(posts);
        return allPosts;
    }


    public Map<String, List<CrewPostDTO>> getPostsWithPinned(
            int page, int pageSize, String category, String searchKeyword, String sort, Boolean status) {
        int offset = (page - 1) * pageSize;

        Map<String, List<CrewPostDTO>> result = new HashMap<>();

        // 1. 공지 3개 고정
        List<CrewPostDTO> pinnedNoticePosts = crewBoardDAO.getFixedPostsByCategory("공지", 3);
        result.put("pinnedNoticePosts", pinnedNoticePosts);

        for (CrewPostDTO post : pinnedNoticePosts) {
            // 각 게시글의 userId를 사용해 닉네임 조회 후 설정
            String userNickname =  crewBoardDAO.getUserNicknameById(post.getUserId());
            post.setUserNickname(userNickname);
        }

        // 2. 일반 게시글
        List<CrewPostDTO> generalPosts = crewBoardDAO.getPostsByPageAndCategoryAndSearch(
                offset, pageSize, category, searchKeyword, sort, status);

        result.put("generalPosts", generalPosts);

        for (CrewPostDTO post : generalPosts) {
            // 각 게시글의 userId를 사용해 닉네임 조회 후 설정
            String userNickname =  crewBoardDAO.getUserNicknameById(post.getUserId());
            post.setUserNickname(userNickname);

            String htmlContent = post.getContent();
            String plainText = htmlContent.replaceAll("<[^>]*>", "");
            post.setContent(plainText);

            String images = post.getImgUrl();
            String firstImage;
            if(!Objects.isNull(images)){
                firstImage = images.split(",")[0];
            }else{
                firstImage = "";
            }
            post.setImgUrl(firstImage);

        }

        return result;
    }


    // 카테고리 필터링을 적용하여 총 게시글 수를 반환한다.
    public int getTotalPostCount(String category, String searchKeyword) {
        return crewBoardDAO.getTotalPostCount(category, searchKeyword); // 전체 게시글 수를 가져오는 메소드
    }

    /*public CrewPostDTO getPostById(Integer id, Integer userId) {
        crewBoardDAO.incrementViewCount(id); // 조회시 조회수 증가
        CrewPostDTO post = crewBoardDAO.getPostById(id); // 게시글 가져오기

        // 현재 사용자의 좋아요 여부 설정
        boolean isLiked = crewBoardDAO.isPostLikedByUser(id, userId);
        post.setLiked(isLiked);

        return post;
    }*/

    public String getUserNicknameById(Integer userId) {
        return crewBoardDAO.getUserNicknameById(userId);
    }

    /*public PostDTO getPostById(Integer id, Integer userId) {
    }

    public List<CommentDTO> getCommentsByPostId(Integer id) {
    }

    public PostDTO getPreviousPost(Integer id) {
    }

    public PostDTO getNextPost(Integer id) {
    }*/
}


package com.ohgiraffers.climbon.crew.crewHome.service;

import com.ohgiraffers.climbon.community.dto.CommentDTO;
import com.ohgiraffers.climbon.community.dto.PostDTO;
import com.ohgiraffers.climbon.crew.crewHome.dao.CrewBoardDAO;
import com.ohgiraffers.climbon.crew.crewHome.dto.CrewBoardDTO;
import com.ohgiraffers.climbon.crew.crewHome.dto.CrewCommentDTO;
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

        for (CrewPostDTO post : generalPosts) {
            post.setCrewName(crewBoardDAO.getCrewNameByCrewCode(post.getCrewCode()));
        }

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


    public String getUserNicknameById(Integer userId) {
        return crewBoardDAO.getUserNicknameById(userId);
    }

    public CrewPostDTO getPostById(Integer postId, Integer userId) {
        crewBoardDAO.incrementViewCount(postId); // 조회시 조회수 증가
        CrewPostDTO post = crewBoardDAO.getPostById(postId); // 게시글 가져오기

        // 현재 사용자의 좋아요 여부 설정
        boolean isLiked = crewBoardDAO.isPostLikedByUser(postId, userId);
        post.setLiked(isLiked);

        return post;
    }

    public String getUserProfilePicById(Integer userId) {
        return crewBoardDAO.getUserProfilePicById(userId);
    }

    // 댓글 조회
    public List<CrewCommentDTO> getCommentsByPostId(Integer postId) {
        return crewBoardDAO.getCommentsByPostId(postId);
    }


    public CrewPostDTO getPreviousPost(Integer postId) {
        return crewBoardDAO.getPreviousPost(postId);
    }

    public CrewPostDTO getNextPost(Integer postId) {
        return crewBoardDAO.getNextPost(postId);
    }


    public int insertComment(CrewCommentDTO comment) {
        return crewBoardDAO.insertComment(comment);
    }

    public int updateComment(CrewCommentDTO comment) {
        return crewBoardDAO.updateComment(comment);
    }

    public int deleteComment(CrewCommentDTO comment) {
        return crewBoardDAO.deleteComment(comment);
    }

    public int getJustAddedPostById(int id) {
        return crewBoardDAO.getJustAddedPostById(id);
    }

    public void toggleLike(int postId, Integer userId) {
        if (hasUserLikedPost(postId, userId)) {
            crewBoardDAO.removeLike(postId, userId); // 좋아요 취소
            decrementHearts(postId); // 좋아요 수 감소
        } else {
            crewBoardDAO.addLike(postId, userId); // 좋아요 추가
            incrementHearts(postId); // 좋아요 수 증가
        }
    }

    public void incrementHearts(int postId) {
        crewBoardDAO.incrementHearts(postId);
    }

    public void decrementHearts(int postId) {
        crewBoardDAO.decrementHearts(postId);
    }

    public boolean hasUserLikedPost(int postId, int userId) {
        return crewBoardDAO.hasUserLikedPost(postId, userId);
    }

    public List<CrewCommentDTO> getCommentsById(Integer id) {
        List<CrewCommentDTO> crewCommentDTOList = crewBoardDAO.getCommentsById(id);
        return crewCommentDTOList;
    }

    public void deletePost(Integer postId) {
        crewBoardDAO.deletePost(postId);

        // 해당 게시글의 모든 댓글 status = 0 으로 업데이트
        crewBoardDAO.deleteCommentsByPostId(postId);
    }

    public int getCrewCodeByPostId(Integer postId) {
        return crewBoardDAO.getCrewCodeByPostId(postId);
    }
}


package com.ohgiraffers.climbon.community.service;

import com.ohgiraffers.climbon.community.dao.PostDAO;
import com.ohgiraffers.climbon.community.dto.CommentDTO;
import com.ohgiraffers.climbon.community.dto.PostDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.xml.stream.events.Comment;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class PostService {

    @Autowired
    private PostDAO postDAO;

    // 페이지와 카테고리에 따라 필터링된 게시글 목록을 가져온다.
    public List<PostDTO> getPostsByPageAndCategoryAndSearch(int page, int pageSize, String category, String searchKeyword, String sort, String dday, Boolean status) {

        int offset = (page - 1) * pageSize; // 페이지 번호에 맞는 시작 위치 ex) 2page 면 16번째 게시글부터 불러옴 (첫번째 게시글 위치로)


        // 1. 공지 게시글 (2개 고정)
        List<PostDTO> noticePosts = postDAO.getFixedPostsByCategory("공지", 2);

        // 2. 가이드 게시글 (1개 고정)
        List<PostDTO> guidePosts = postDAO.getFixedPostsByCategory("가이드", 1);

        // 3. 일반 게시글 (페이징 적용)
        List<PostDTO> posts = postDAO.getPostsByPageAndCategoryAndSearch(offset, pageSize, category, searchKeyword, sort, dday, status);    // 해당 페이지의 게시글을 가져오기 위해 offset 값을 계산하고, 이를 기반으로 DAO에서 데이터 가져옴. ,searchKeyword 파라미터 추가

        // 소식 카테고리의 게시글에 대해 D-Day 계산
        for (PostDTO post : posts){
            if ("소식".equals(post.getCategory())){
                post.setDday(calculateDday(post.getEventStartDate(), post.getEventEndDate())); // D-Day 설정
            }
        }
        //4. 게시글 합치기
        List<PostDTO> allPosts = new ArrayList<>();
        allPosts.addAll(noticePosts);
        allPosts.addAll(guidePosts);
        allPosts.addAll(posts);

//        System.out.println(dday); dday 들어오는지 확인용 출력
        return allPosts;
    }

    public Map<String, List<PostDTO>> getPostsWithPinned(
            int page, int pageSize, String category, String searchKeyword, String sort, String dday, Boolean status) {
        int offset = (page - 1) * pageSize;

        Map<String, List<PostDTO>> result = new HashMap<>();

        // 1. 공지 2개 고정
        List<PostDTO> pinnedNoticePosts = postDAO.getFixedPostsByCategory("공지", 2);
        result.put("pinnedNoticePosts", pinnedNoticePosts);

        for (PostDTO post : pinnedNoticePosts) {
            // 각 게시글의 userId를 사용해 닉네임 조회 후 설정
            String userNickname =  postDAO.getUserNicknameById(post.getUserId());
            post.setUserNickname(userNickname);
        }

        // 2. 가이드 1개 고정
        List<PostDTO> pinnedGuidePosts = postDAO.getFixedPostsByCategory("가이드", 1);
        result.put("pinnedGuidePosts", pinnedGuidePosts);

        for (PostDTO post : pinnedGuidePosts) {
            // 각 게시글의 userId를 사용해 닉네임 조회 후 설정
            String userNickname =  postDAO.getUserNicknameById(post.getUserId());
            post.setUserNickname(userNickname);
        }

        // 3. 일반 게시글
        List<PostDTO> generalPosts = postDAO.getPostsByPageAndCategoryAndSearch(
                offset, pageSize, category, searchKeyword, sort, dday, status);
        result.put("generalPosts", generalPosts);

        for (PostDTO post : generalPosts) {
            // 각 게시글의 userId를 사용해 닉네임 조회 후 설정
            String userNickname =  postDAO.getUserNicknameById(post.getUserId());
            post.setUserNickname(userNickname);
        }

        return result;
    }

    // D-Day 계산 메소드
    private String calculateDday(String eventStartDate, String eventEndDate) {
        if (eventStartDate == null || eventEndDate == null){
            return "";  // 시작일 또는 종료일이 없으면 D-Day 계산하지 않음
        }

        LocalDate startDate = LocalDate.parse(eventStartDate);
        LocalDate endDate = LocalDate.parse(eventEndDate);
        LocalDate today = LocalDate.now();

        // 오늘 날짜가 시작일과 종료일 사이에 있을 경우 "진행 중" 반환
        if (!today.isBefore(startDate) && !today.isAfter(endDate)){
            return "진행중";
        }

        // 오늘 날짜가 행사 시작일 이전일 경우 D-Day 계산
        else if (today.isBefore(startDate)){
            long daysUntilStart = ChronoUnit.DAYS.between(today, startDate);
            return "D-" + daysUntilStart;
    }
        // 행사 종료일이 지난 경우 "종료됨" 반환
        else{
        return "종료됨";
        }
    }

    // 카테고리 필터링을 적용하여 총 게시글 수를 반환한다.
    public int getTotalPostCount(String category, String searchKeyword) {
        return postDAO.getTotalPostCount(category, searchKeyword); // 전체 게시글 수를 가져오는 메소드
    }

    public PostDTO getPostById(Integer id, Integer userId) {
        postDAO.incrementViewCount(id); // 조회시 조회수 증가
        PostDTO post = postDAO.getPostById(id); // 게시글 가져오기

        // 현재 사용자의 좋아요 여부 설정
        boolean isLiked = postDAO.isPostLikedByUser(id, userId);
        post.setLiked(isLiked);

        return post;
    }

    public void insertPost(PostDTO post) {
        postDAO.insertPost(post);
    }

    public void updatePost(PostDTO post) {
        // 수정 시 이미지 URL이 존재하지 않을 경우 기존 값을 유지
        PostDTO existingPost = postDAO.getPostById(post.getId());
        if (post.getImageUrl() == null || post.getImageUrl().isEmpty()){
            post.setImageUrl(existingPost.getImageUrl());
        }

        postDAO.updatePost(post);
    }

    public void deletePost(Integer postId) { //트랜잭션 처리
        postDAO.deletePost(postId);

        // 해당 게시글의 모든 댓글 status = 0 으로 업데이트
        postDAO.deleteCommentsByPostId(postId);
    }

    public PostDTO getPreviousPost(Integer id) {
        return postDAO.getPreviousPost(id);
    }

    public PostDTO getNextPost(Integer id) {  //Integer로 정의 하는 이유 : null을 허용 할 수 있기 때문에 또한 compareTo, toString을 제공하기 때문에 cf) MyBatis는 객체형 Integer와의 호환성이 더 높으므로, 매핑에 있어 Integer를 사용하는 것이 일반적이다.
        return postDAO.getNextPost(id);
    }

    // 댓글 조회, 추가 메소드
    public List<CommentDTO> getCommentsByPostId(Integer postId) {
        return postDAO.getCommentsByPostId(postId);
    }

    public void insertComment(CommentDTO comment){
        postDAO.insertComment(comment);
    }

    public Integer getUserIdByUserName(String userId) {
        return postDAO.getUserIdByUserName(userId);
    }

    public String getUserNicknameById(Integer userId) {
        return postDAO.getUserNicknameById(userId);
    }

    public Integer findUserIdByEmail(String email) {
        return postDAO.getUserIdByEmail(email);
    }

    public void incrementHearts(int postId) {
        postDAO.incrementHearts(postId);
    }

    public void decrementHearts(int postId) {
        postDAO.decrementHearts(postId);
    }

    public boolean hasUserLikedPost(int postId, int userId) {
        return postDAO.hasUserLikedPost(postId, userId);
    }

    public void toggleLike(int postId, Integer userId) {
        if (hasUserLikedPost(postId, userId)) {
            postDAO.removeLike(postId, userId); // 좋아요 취소
            decrementHearts(postId); // 좋아요 수 감소
        } else {
            postDAO.addLike(postId, userId); // 좋아요 추가
            incrementHearts(postId); // 좋아요 수 증가
        }
    }

    public void updateComment(CommentDTO comment) {
        postDAO.updateComment(comment);
    }

    public void deleteComment(CommentDTO comment) {
        postDAO.deleteComment(comment);
    }

}

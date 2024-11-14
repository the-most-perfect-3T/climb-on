package com.ohgiraffers.climbon.community.service;

import com.ohgiraffers.climbon.community.dao.PostDAO;
import com.ohgiraffers.climbon.community.dto.PostDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class PostService {

    @Autowired
    private PostDAO postDAO;

    // 페이지와 카테고리에 따라 필터링된 게시글 목록을 가져온다.
    public List<PostDTO> getPostsByPageAndCategoryAndSearch(int page, int pageSize, String category, String searchKeyword, String sort, String dday) {
        int offset = (page - 1) * pageSize; // 페이지 번호에 맞는 시작 위치 ex) 2page 면 16번째 게시글부터 불러옴 (첫번째 게시글 위치로)

        List<PostDTO> posts = postDAO.getPostsByPageAndCategoryAndSearch(offset, pageSize, category, searchKeyword, sort, dday);    // 해당 페이지의 게시글을 가져오기 위해 offset 값을 계산하고, 이를 기반으로 DAO에서 데이터 가져옴. ,searchKeyword 파라미터 추가

        // 소식 카테고리의 게시글에 대해 D-Day 계산
        for (PostDTO post : posts){
            if ("소식".equals(post.getCategory())){
                post.setDday(calculateDday(post.getEventStartDate(), post.getEventEndDate())); // D-Day 설정
            }
        }

//        System.out.println(dday); dday 들어오는지 확인용 출력
        return posts;
    }

    // D-Day 계산 메소드
    private String calculateDday(Date eventStartDate, Date eventEndDate) {
        if (eventStartDate == null || eventEndDate == null){
            return "";  // 시작일 또는 종료일이 없으면 D-Day 계산하지 않음
        }

        LocalDate startDate = eventStartDate.toLocalDate();
        LocalDate endDate = eventEndDate.toLocalDate();
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

    public PostDTO getPostById(Integer id) {
        postDAO.incrementViewCount(id); // 조회시 조회수 증가
        return postDAO.getPostById(id); // 게시글 자겨오기
    }

    public void insertPost(PostDTO post) {
        postDAO.insertPost(post);
    }

    public void updatePost(PostDTO post) {
        postDAO.updatePost(post);
    }

    public void deletePost(Integer id) {
        postDAO.deletePost(id);
    }

    public PostDTO getPreviousPost(Integer id) {
        return postDAO.getPreviousPost(id);
    }

    public PostDTO getNextPost(Integer id) {  //Integer로 정의 하는 이유 : null을 허용 할 수 있기 때문에 또한 compareTo, toString을 제공하기 때문에 cf) MyBatis는 객체형 Integer와의 호환성이 더 높으므로, 매핑에 있어 Integer를 사용하는 것이 일반적이다.
        return postDAO.getNextPost(id);
    }
}

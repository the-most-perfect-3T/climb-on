package com.ohgiraffers.climbon.community.dto;

import org.springframework.format.annotation.DateTimeFormat;

import java.sql.Date;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class PostDTO {

    private Integer id;

    private Integer userId;

    private String userNickname;

    private String userProfilePic;

    private String title;

    private String content;

    private String category;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private int viewCount;

    private int commentsCount;

    private String imageUrl;

    private boolean isAnonymous;

    private int heartsCount;

    private boolean isLiked; // 좋아요 여부

    private boolean status = true; // 기본값을 활성 상태로 설정

    private String eventStartDate;

    private String eventEndDate;

    private String dday; // D-Day 계산 결과 저장

    public PostDTO() {
    }

    public PostDTO(Integer id, Integer userId, String userNickname, String userProfilePic, String title, String content, String category, LocalDateTime createdAt, LocalDateTime updatedAt, int viewCount, int commentsCount, String imageUrl, boolean isAnonymous, int heartsCount, boolean isLiked, boolean status, String eventStartDate, String eventEndDate, String dday) {
        this.id = id;
        this.userId = userId;
        this.userNickname = userNickname;
        this.userProfilePic = userProfilePic;
        this.title = title;
        this.content = content;
        this.category = category;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.viewCount = viewCount;
        this.commentsCount = commentsCount;
        this.imageUrl = imageUrl;
        this.isAnonymous = isAnonymous;
        this.heartsCount = heartsCount;
        this.isLiked = isLiked;
        this.status = status;
        this.eventStartDate = eventStartDate;
        this.eventEndDate = eventEndDate;
        this.dday = dday;
    }

    public String getUserNickname() {
        return userNickname;
    }

    public void setUserNickname(String userNickname) {
        this.userNickname = userNickname;
    }

    public String getUserProfilePic() {
        return userProfilePic;
    }

    public void setUserProfilePic(String userProfilePic) {
        this.userProfilePic = userProfilePic;
    }


    // 작성일을 포맷팅해서 반환하는 메소드 : createdAt 필드가 오늘 날짜인 경우 몇시 몇분 형식으로 반환, 오늘날짜가 아닌경우 년/월/일 형식으로 반환 // 1,2,3일전은 하루전, 이틀전, 3일전으로 나타낼 수 있게 추가
    public String getFormattedCreatedAt() {

        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy.MM.dd.");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

        long daysAgo = ChronoUnit.DAYS.between(createdAt.toLocalDate(), now.toLocalDate());

        if (daysAgo == 0){
        // 오늘 작성된 경우 시간만 표시
            return createdAt.format(timeFormatter);
        } else if (daysAgo == 1){
            return "하루전";
        } else if (daysAgo == 2){
            return "이틀전";
        } else if (daysAgo ==3 ){
            return "3일전";
        } else {
            // 3일 이상 지난 경우 날짜 형식으로 표시
            return createdAt.format(dateFormatter);
        }
    }

    public String getFormattedUpdatedAt() {

        // 이부분에 null체크 필요함
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy.MM.dd.");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

        if(updatedAt!=null)
        {
            long daysAgo = ChronoUnit.DAYS.between(updatedAt.toLocalDate(), now.toLocalDate());

            if (daysAgo == 0){
                // 오늘 작성된 경우 시간만 표시
                return updatedAt.format(timeFormatter);
            } else if (daysAgo == 1){
                return "하루전";
            } else if (daysAgo == 2){
                return "이틀전";
            } else if (daysAgo ==3 ){
                return "3일전";
            } else {
                // 3일 이상 지난 경우 날짜 형식으로 표시
                return updatedAt.format(dateFormatter);
            }
        }

        return "";
    }


    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public int getViewCount() {
        return viewCount;
    }

    public void setViewCount(int viewCount) {
        this.viewCount = viewCount;
    }

    public int getCommentsCount() {
        return commentsCount;
    }

    public void setCommentsCount(int commentsCount) {
        this.commentsCount = commentsCount;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public boolean isAnonymous() {
        return isAnonymous;
    }

    public void setAnonymous(boolean anonymous) {
        isAnonymous = anonymous;
    }

    public String getDisplayName() {
        return isAnonymous ? "익명" : userNickname;
    }

    public int getHeartsCount() {
        return heartsCount;
    }

    public void setHeartsCount(int heartsCount) {
        this.heartsCount = heartsCount;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getEventStartDate() {
        return eventStartDate;
    }

    public void setEventStartDate(String eventStartDate) {
        this.eventStartDate = eventStartDate;
    }

    public String getEventEndDate() {
        return eventEndDate;
    }

    public void setEventEndDate(String eventEndDate) {
        this.eventEndDate = eventEndDate;
    }

    public String getDday() {
        return dday;
    }

    public void setDday(String dday) {
        this.dday = dday;
    }

    public boolean isLiked() {
        return isLiked;
    }

    public void setLiked(boolean liked) {
        isLiked = liked;
    }

    @Override
    public String toString() {
        return "PostDTO{" +
                "id=" + id +
                ", userId=" + userId +
                ", userNickname='" + userNickname + '\'' +
                ", userProfilePic='" + userProfilePic + '\'' +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", category='" + category + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", viewCount=" + viewCount +
                ", commentsCount=" + commentsCount +
                ", imageUrl='" + imageUrl + '\'' +
                ", isAnonymous=" + isAnonymous +
                ", heartsCount=" + heartsCount +
                ", isLiked=" + isLiked +
                ", status=" + status +
                ", eventStartDate='" + eventStartDate + '\'' +
                ", eventEndDate='" + eventEndDate + '\'' +
                ", dday='" + dday + '\'' +
                '}';
    }
}

package com.ohgiraffers.climbon.community.dto;

import org.springframework.format.annotation.DateTimeFormat;

import java.sql.Date;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class PostDTO {

    private int id;

    private String userId;

    private String userNickname;

    private String userProfilePic;

    private String title;

    private String content;

    private String category;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private int viewCount;

    private int commentCount;

    private String imageUrl;

    private boolean isAnonymous;

    private int likes;

    private byte status;


    private String eventStartDate;

    private String eventEndDate;

    private String dday; // D-Day 계산 결과 저장


    public PostDTO() {
    }

    public PostDTO(int id, String userId, String userNickname, String userProfilePic, String title, String content, String category, LocalDateTime createdAt, LocalDateTime updatedAt, int viewCount, int commentCount, String imageUrl, boolean isAnonymous, int likes, byte status, String eventStartDate, String eventEndDate, String dday) {
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
        this.commentCount = commentCount;
        this.imageUrl = imageUrl;
        this.isAnonymous = isAnonymous;
        this.likes = likes;
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

    // 작성일을 포맷팅해서 반환하는 메소드 : createdAt 필드가 오늘 날짜인 경우 몇시 몇분 형식으로 반환, 오늘날짜가 아닌경우 년/월/일 형식으로 반환
    public String getFormattedCreatedAt() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter;

        if (createdAt.toLocalDate().isEqual(now.toLocalDate())) {
            // 오늘 날짜인 경우 시간만 표시
            formatter = DateTimeFormatter.ofPattern("HH:mm");
        } else {
            // 오늘 아닌 경우 날짜만 표시
            formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd.");
        }
        return createdAt.format(formatter);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
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

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
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

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public byte getStatus() {
        return status;
    }

    public void setStatus(byte status) {
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
                ", commentCount=" + commentCount +
                ", imageUrl='" + imageUrl + '\'' +
                ", isAnonymous=" + isAnonymous +
                ", likes=" + likes +
                ", status=" + status +
                ", eventStartDate=" + eventStartDate +
                ", eventEndDate=" + eventEndDate +
                ", dday='" + dday + '\'' +
                '}';
    }
}

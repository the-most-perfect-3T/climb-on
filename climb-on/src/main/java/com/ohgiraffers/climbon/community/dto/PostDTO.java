package com.ohgiraffers.climbon.community.dto;

import java.sql.Date;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class PostDTO {

    private int id;

    private int userId;

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

    private Date eventStartDate;

    private Date eventEndDate;

    private String dday; // D-Day 계산 결과 저장


    public PostDTO() {
    }

    public PostDTO(int id, int userId, String title, String content, String category, LocalDateTime createdAt, LocalDateTime updatedAt, int viewCount, int commentCount, String imageUrl, boolean isAnonymous, int likes, byte status, Date eventStartDate, Date eventEndDate, String dday) {
        this.id = id;
        this.userId = userId;
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

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
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

    public Date getEventStartDate() {
        return eventStartDate;
    }

    public void setEventStartDate(Date eventStartDate) {
        this.eventStartDate = eventStartDate;
    }

    public Date getEventEndDate() {
        return eventEndDate;
    }

    public void setEventEndDate(Date eventEndDate) {
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

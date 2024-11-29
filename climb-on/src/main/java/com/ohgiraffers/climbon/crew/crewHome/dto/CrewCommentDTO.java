package com.ohgiraffers.climbon.crew.crewHome.dto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class CrewCommentDTO {
    private Integer id;
    private Integer postId;
    private Integer userId;
    private String userNickname;
    private String userProfilePic;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private boolean status = true;
    private int commentsCount;

    public CrewCommentDTO() {
    }

    public CrewCommentDTO(Integer id, Integer postId, Integer userId, String userNickname, String userProfilePic, String content, LocalDateTime createdAt, LocalDateTime updatedAt, boolean status, int commentsCount) {
        this.id = id;
        this.postId = postId;
        this.userId = userId;
        this.userNickname = userNickname;
        this.userProfilePic = userProfilePic;
        this.content = content;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.status = status;
        this.commentsCount = commentsCount;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPostId() {
        return postId;
    }

    public void setPostId(Integer postId) {
        this.postId = postId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }


    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }


    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public int getCommentsCount() {
        return commentsCount;
    }

    public void setCommentsCount(int commentsCount) {
        this.commentsCount = commentsCount;
    }

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
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy.MM.dd.");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

        long daysAgo = ChronoUnit.DAYS.between(updatedAt.toLocalDate(), now.toLocalDate());

        if (daysAgo == 0){
            // 오늘 작성된 경우 시간만 표시
            return updatedAt.format(timeFormatter);
        } else if (daysAgo == 1){
            return "하루전";
        } else if (daysAgo == 2){
            return "2일전";
        } else if (daysAgo == 3){
            return "3일전";
        } else {
            // 3일 이상 지난 경우 날짜 형식으로 표시
            return updatedAt.format(dateFormatter);
        }
    }

    @Override
    public String toString() {
        return "CrewCommentDTO{" +
                "id=" + id +
                ", postId=" + postId +
                ", userId=" + userId +
                ", userNickname='" + userNickname + '\'' +
                ", userProfilePic='" + userProfilePic + '\'' +
                ", content='" + content + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", status=" + status +
                ", commentsCount=" + commentsCount +
                '}';
    }
}

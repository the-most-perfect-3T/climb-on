package com.ohgiraffers.climbon.community.dto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CommentDTO {
    private Integer id;
    private Integer postId;
    private String userId;
    private String content;
    private LocalDateTime createdAt;
    private int commentsCount;

    public CommentDTO() {
    }

    public CommentDTO(Integer id, Integer postId, String userId, String content, LocalDateTime createdAt, int commentsCount) {
        this.id = id;
        this.postId = postId;
        this.userId = userId;
        this.content = content;
        this.createdAt = createdAt;
        this.commentsCount = commentsCount;
    }

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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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

    public int getCommentsCount() {
        return commentsCount;
    }

    public void setCommentsCount(int commentsCount) {
        this.commentsCount = commentsCount;
    }

    @Override
    public String toString() {
        return "CommentDTO{" +
                "id=" + id +
                ", postId=" + postId +
                ", userId='" + userId + '\'' +
                ", content='" + content + '\'' +
                ", createdAt=" + createdAt +
                ", commentsCount=" + commentsCount +
                '}';
    }

}
package com.ohgiraffers.climbon.community.dto;

import java.time.LocalDateTime;

public class CommentDTO {
    private Integer id;
    private Integer postId;
    //    private Integer userId;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private int commentCount;

    public CommentDTO() {
    }

    public CommentDTO(Integer id, Integer postId, String content, LocalDateTime createdAt, LocalDateTime updatedAt, int commentCount) {
        this.id = id;
        this.postId = postId;
        this.content = content;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.commentCount = commentCount;
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

    @Override
    public String toString() {
        return "CommentDTO{" +
                "id=" + id +
                ", postId=" + postId +
                ", content='" + content + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", commentCount=" + commentCount +
                '}';
    }
}
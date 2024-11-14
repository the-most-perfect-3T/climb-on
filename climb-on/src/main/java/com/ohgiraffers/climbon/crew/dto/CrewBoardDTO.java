package com.ohgiraffers.climbon.crew.dto;

import java.sql.Date;

public class CrewBoardDTO {

    private int id;
    private int crewCode;
    private int userId;
    private int categoryCode;
    private String title;
    private String content;
    private String imgUrl;
    private Date createdAt;
    private Date updatedAt;
    private int viewCount;
    private int likeCount;
    private String isAnonymous;
    private int status;

    public CrewBoardDTO() {
    }

    public CrewBoardDTO(int id, int crewCode, int userId, int categoryCode, String title, String content, String imgUrl, Date createdAt, Date updatedAt, int viewCount, int likeCount, String isAnonymous, int status) {
        this.id = id;
        this.crewCode = crewCode;
        this.userId = userId;
        this.categoryCode = categoryCode;
        this.title = title;
        this.content = content;
        this.imgUrl = imgUrl;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.viewCount = viewCount;
        this.likeCount = likeCount;
        this.isAnonymous = isAnonymous;
        this.status = status;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCrewCode() {
        return crewCode;
    }

    public void setCrewCode(int crewCode) {
        this.crewCode = crewCode;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getCategoryCode() {
        return categoryCode;
    }

    public void setCategoryCode(int categoryCode) {
        this.categoryCode = categoryCode;
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

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public int getViewCount() {
        return viewCount;
    }

    public void setViewCount(int viewCount) {
        this.viewCount = viewCount;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    public String isAnonymous() {
        return isAnonymous;
    }

    public void setAnonymous(String anonymous) {
        isAnonymous = anonymous;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "CrewBoardDTO{" +
                "id=" + id +
                ", crewCode=" + crewCode +
                ", userId=" + userId +
                ", categoryCode=" + categoryCode +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", imgUrl='" + imgUrl + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", viewCount=" + viewCount +
                ", likeCount=" + likeCount +
                ", isAnonymous=" + isAnonymous +
                ", status=" + status +
                '}';
    }
}

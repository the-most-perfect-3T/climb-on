package com.ohgiraffers.climbon.crew.crewHome.dto;

import java.sql.Date;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class CrewBoardDTO {

    private int id;
    private int crewCode;
    private int userId;
    private String category;
    private String title;
    private String content;
    private String imgUrl;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private int viewCount;
    private int likeCount;
    private boolean isAnonymous;
    private int status;

    public CrewBoardDTO() {
    }

    public CrewBoardDTO(int id, int crewCode, int userId, String category, String title, String content, String imgUrl, LocalDateTime createdAt, LocalDateTime updatedAt, int viewCount, int likeCount, boolean isAnonymous, int status) {
        this.id = id;
        this.crewCode = crewCode;
        this.userId = userId;
        this.category = category;
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


    public boolean getIsAnonymous() {
        return isAnonymous;
    }

    public void setIsAnonymous(boolean isAnonymous) {
        this.isAnonymous = isAnonymous;
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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
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

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
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
                ", category=" + category +
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

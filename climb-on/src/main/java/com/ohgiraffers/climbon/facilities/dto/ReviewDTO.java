package com.ohgiraffers.climbon.facilities.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;
import java.util.Date;

public class ReviewDTO {
   private Integer Id;
   private Integer FacilityId;
   private Integer userId;
    private int rating;
    private String comment;
    private LocalDateTime createdAt;

    private String userNickname;
    private int likeCount;
    private float averageRating;

    @Override
    public String toString() {
        return "ReviewDTO{" +
                "Id=" + Id +
                ", FacilityId=" + FacilityId +
                ", userId=" + userId +
                ", rating=" + rating +
                ", comment='" + comment + '\'' +
                ", createdAt=" + createdAt +
                ", userNickname='" + userNickname + '\'' +
                ", likeCount=" + likeCount +
                ", averageRating=" + averageRating +
                ", profilePic='" + profilePic + '\'' +
                '}';
    }

    public ReviewDTO(Integer id, Integer facilityId, Integer userId, int rating, String comment, LocalDateTime createdAt, String userNickname, int likeCount, float averageRating, String profilePic) {
        Id = id;
        FacilityId = facilityId;
        this.userId = userId;
        this.rating = rating;
        this.comment = comment;
        this.createdAt = createdAt;
        this.userNickname = userNickname;
        this.likeCount = likeCount;
        this.averageRating = averageRating;
        this.profilePic = profilePic;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    private String profilePic;                  // 사용자 프로필 사진 URL

    public ReviewDTO(Integer id, Integer facilityId, Integer reviewerId, int rating, String comment, LocalDateTime createdAt, String userNickname, int likeCount, float averageRating) {
        Id = id;
        FacilityId = facilityId;
        userId = reviewerId;
        this.rating = rating;
        this.comment = comment;
        this.createdAt = createdAt;
        this.userNickname = userNickname;
        this.likeCount = likeCount;
        this.averageRating = averageRating;
    }

    public float getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(float averageRating) {
        this.averageRating = averageRating;
    }

    public ReviewDTO(Integer id, Integer facilityId, Integer reviewerId, int rating, String comment, LocalDateTime  createdAt, String userNickname, int likeCount) {
        Id = id;
        FacilityId = facilityId;
        userId = reviewerId;
        this.rating = rating;
        this.comment = comment;
        this.createdAt = createdAt;
        this.userNickname = userNickname;
        this.likeCount = likeCount;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    public ReviewDTO(Integer id, Integer facilityId, Integer reviewerId, int rating, String comment, LocalDateTime  createdAt, String userNickname) {
        Id = id;
        FacilityId = facilityId;
        userId = reviewerId;
        this.rating = rating;
        this.comment = comment;
        this.createdAt = createdAt;
        this.userNickname = userNickname;
    }

    public String getUserNickname() {
        return userNickname;
    }

    public void setUserNickname(String userNickname) {
        this.userNickname = userNickname;
    }

    public ReviewDTO() {
    }



    public Integer getId() {
        return Id;
    }

    public void setId(Integer id) {
        Id = id;
    }

    public Integer getFacilityId() {
        return FacilityId;
    }

    public void setFacilityId(Integer facilityId) {
        FacilityId = facilityId;
    }

    public Integer getReviewerId() {
        return userId;
    }

    public void setReviewerId(Integer reviewerId) {
        userId = reviewerId;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}

package com.ohgiraffers.climbon.facilities.dto;

import java.time.LocalDateTime;

public class ReviewDTO {
   private Integer Id;
   private Integer FacilityId;
   private Integer ReviewerId;
    private int rating;
    private String comment;
    private LocalDateTime createdAt;

    @Override
    public String toString() {
        return "ReviewDTO{" +
                "Id=" + Id +
                ", FacilityId=" + FacilityId +
                ", ReviewerId=" + ReviewerId +
                ", rating=" + rating +
                ", comment='" + comment + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }

    public ReviewDTO() {
    }

    public ReviewDTO(Integer id, Integer facilityId, Integer reviewerId, int rating, String comment, LocalDateTime createdAt) {
        Id = id;
        FacilityId = facilityId;
        ReviewerId = reviewerId;
        this.rating = rating;
        this.comment = comment;
        this.createdAt = createdAt;
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
        return ReviewerId;
    }

    public void setReviewerId(Integer reviewerId) {
        ReviewerId = reviewerId;
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

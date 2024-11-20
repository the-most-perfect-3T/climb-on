package com.ohgiraffers.climbon.facilities.dto;

public class FacilityFavoriteDTO {
    Integer userId;
    Integer facilityId;

    public FacilityFavoriteDTO() {
    }

    @Override
    public String toString() {
        return "FacilityFavoriteDTO{" +
                "userId=" + userId +
                ", facilityId=" + facilityId +
                '}';
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getFacilityId() {
        return facilityId;
    }

    public void setFacilityId(Integer facilityId) {
        this.facilityId = facilityId;
    }

    public FacilityFavoriteDTO(Integer userId, Integer facilityId) {
        this.userId = userId;
        this.facilityId = facilityId;
    }
}
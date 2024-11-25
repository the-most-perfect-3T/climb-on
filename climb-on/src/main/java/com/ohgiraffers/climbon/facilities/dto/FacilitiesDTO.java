package com.ohgiraffers.climbon.facilities.dto;

import com.ohgiraffers.climbon.facilities.Enum.FacilityType;

import java.math.BigDecimal;

public class FacilitiesDTO {
    private int id;
    private String facilityName;
    private String address;
    private String contact;
    private String openingTime;
    private int categoryId;
    private BigDecimal latitude;        //소수점 6자리의 값까지 저장가능 좌표 위도
    private BigDecimal longitude;       //경도
    private FacilityType facilityType;   // 시설 타입
    private boolean isFavorite;
    private String imageUrl;

    @Override
    public String toString() {
        return "FacilitiesDTO{" +
                "id=" + id +
                ", facilityName='" + facilityName + '\'' +
                ", address='" + address + '\'' +
                ", contact='" + contact + '\'' +
                ", openingTime='" + openingTime + '\'' +
                ", categoryId=" + categoryId +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", facilityType=" + facilityType +
                ", isFavorite=" + isFavorite +
                ", imageUrl='" + imageUrl + '\'' +
                '}';
    }

    public FacilitiesDTO(int id, String facilityName, String address, String contact, String openingTime, int categoryId, BigDecimal latitude, BigDecimal longitude, FacilityType facilityType, boolean isFavorite, String imageUrl) {
        this.id = id;
        this.facilityName = facilityName;
        this.address = address;
        this.contact = contact;
        this.openingTime = openingTime;
        this.categoryId = categoryId;
        this.latitude = latitude;
        this.longitude = longitude;
        this.facilityType = facilityType;
        this.isFavorite = isFavorite;
        this.imageUrl = imageUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public FacilitiesDTO(int id, String facilityName, String address, String contact, String openingTime, int categoryId, BigDecimal latitude, BigDecimal longitude, FacilityType facilityType, boolean isFavorite) {
        this.id = id;
        this.facilityName = facilityName;
        this.address = address;
        this.contact = contact;
        this.openingTime = openingTime;
        this.categoryId = categoryId;
        this.latitude = latitude;
        this.longitude = longitude;
        this.facilityType = facilityType;
        this.isFavorite = isFavorite;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFacilityName() {
        return facilityName;
    }

    public void setFacilityName(String facilityName) {
        this.facilityName = facilityName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getOpeningTime() {
        return openingTime;
    }

    public void setOpeningTime(String openingTime) {
        this.openingTime = openingTime;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public BigDecimal getLatitude() {
        return latitude;
    }

    public void setLatitude(BigDecimal latitude) {
        this.latitude = latitude;
    }

    public BigDecimal getLongitude() {
        return longitude;
    }

    public void setLongitude(BigDecimal longitude) {
        this.longitude = longitude;
    }

    public FacilityType getFacilityType() {
        return facilityType;
    }

    public void setFacilityType(FacilityType facilityType) {
        this.facilityType = facilityType;
    }



    public FacilitiesDTO() {
    }

    public FacilitiesDTO(int id, String facilityName, String address, String contact, String openingTime, int categoryId, BigDecimal latitude, BigDecimal longitude, FacilityType facilityType) {
        this.id = id;
        this.facilityName = facilityName;
        this.address = address;
        this.contact = contact;
        this.openingTime = openingTime;
        this.categoryId = categoryId;
        this.latitude = latitude;
        this.longitude = longitude;
        this.facilityType = facilityType;

    }


}

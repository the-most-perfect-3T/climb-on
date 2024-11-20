package com.ohgiraffers.climbon.user.dto;

import org.springframework.web.multipart.MultipartFile;

public class BusinessDTO {

    private Integer id; // user_code
    private String facilityName;
    private MultipartFile businessFile;

    public BusinessDTO() {
    }

    public BusinessDTO(Integer id, String facilityName, MultipartFile businessFile) {
        this.id = id;
        this.facilityName = facilityName;
        this.businessFile = businessFile;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFacilityName() {
        return facilityName;
    }

    public void setFacilityName(String facilityName) {
        this.facilityName = facilityName;
    }

    public MultipartFile getBusinessFile() {
        return businessFile;
    }

    public void setBusinessFile(MultipartFile businessFile) {
        this.businessFile = businessFile;
    }

    @Override
    public String toString() {
        return "BusinessDTO{" +
                "id=" + id +
                ", facilityName='" + facilityName + '\'' +
                ", businessFile=" + businessFile +
                '}';
    }
}

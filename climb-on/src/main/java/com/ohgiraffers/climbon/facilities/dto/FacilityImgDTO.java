package com.ohgiraffers.climbon.facilities.dto;

public class FacilityImgDTO {
    private int id;
    private String fileName;
    private Integer FacilityId;
    private String filePath;


    public FacilityImgDTO() {
    }

    public FacilityImgDTO(int id, String fileName, Integer facilityId, String filePath) {
        this.id = id;
        this.fileName = fileName;
        FacilityId = facilityId;
        this.filePath = filePath;
    }

    public Integer getFacilityId() {
        return FacilityId;
    }

    public void setFacilityId(Integer facilityId) {
        FacilityId = facilityId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
}

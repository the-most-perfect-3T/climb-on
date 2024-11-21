package com.ohgiraffers.climbon.user.dto;

public class NoticeDTO {

    private int userCode;
    private int category;
    private int facilityCode;
    private String attachFile;
    private int isApproval;

    public NoticeDTO() {
    }

    public NoticeDTO(int userCode, int category, int facilityCode, String attachFile, int isApproval) {
        this.userCode = userCode;
        this.category = category;
        this.facilityCode = facilityCode;
        this.attachFile = attachFile;
        this.isApproval = isApproval;
    }

    public int getUserCode() {
        return userCode;
    }

    public void setUserCode(int userCode) {
        this.userCode = userCode;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public int getFacilityCode() {
        return facilityCode;
    }

    public void setFacilityCode(int facilityCode) {
        this.facilityCode = facilityCode;
    }

    public String getAttachFile() {
        return attachFile;
    }

    public void setAttachFile(String attachFile) {
        this.attachFile = attachFile;
    }

    public int getIsApproval() {
        return isApproval;
    }

    public void setIsApproval(int isApproval) {
        this.isApproval = isApproval;
    }

    @Override
    public String toString() {
        return "NoticeDTO{" +
                "userCode=" + userCode +
                ", category=" + category +
                ", facilityCode=" + facilityCode +
                ", attachFile='" + attachFile + '\'' +
                ", isApproval=" + isApproval +
                '}';
    }
}

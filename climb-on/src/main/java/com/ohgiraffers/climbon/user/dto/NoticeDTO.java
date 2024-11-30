package com.ohgiraffers.climbon.user.dto;

public class NoticeDTO {

    private int userCode;
    private int category;
    private int facilityCode;
    private String attachFile;
    private int isApprovalBusiness;
    private int isApprovalCrew;

    public NoticeDTO() {
    }

    public NoticeDTO(int userCode, int category, int facilityCode, String attachFile, int isApprovalBusiness, int isApprovalCrew) {
        this.userCode = userCode;
        this.category = category;
        this.facilityCode = facilityCode;
        this.attachFile = attachFile;
        this.isApprovalBusiness = isApprovalBusiness;
        this.isApprovalCrew = isApprovalCrew;
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

    public int getIsApprovalBusiness() {
        return isApprovalBusiness;
    }

    public void setIsApprovalBusiness(int isApprovalBusiness) {
        this.isApprovalBusiness = isApprovalBusiness;
    }

    public int getIsApprovalCrew() {
        return isApprovalCrew;
    }

    public void setIsApprovalCrew(int isApprovalCrew) {
        this.isApprovalCrew = isApprovalCrew;
    }

    @Override
    public String toString() {
        return "NoticeDTO{" +
                "userCode=" + userCode +
                ", category=" + category +
                ", facilityCode=" + facilityCode +
                ", attachFile='" + attachFile + '\'' +
                ", isApprovalBusiness=" + isApprovalBusiness +
                ", isApprovalCrew=" + isApprovalCrew +
                '}';
    }
}

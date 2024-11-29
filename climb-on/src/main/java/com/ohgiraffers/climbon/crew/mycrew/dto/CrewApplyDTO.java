package com.ohgiraffers.climbon.crew.mycrew.dto;

public class CrewApplyDTO {

    private int userCode;
    private int crewCode;
    private String content;
    private int isApproval;
    private boolean isPermission;

    public CrewApplyDTO() {
    }

    public CrewApplyDTO(int userCode, int crewCode, String content, int isApproval, boolean isPermission) {
        this.userCode = userCode;
        this.crewCode = crewCode;
        this.content = content;
        this.isApproval = isApproval;
        this.isPermission = isPermission;
    }

    public int getUserCode() {
        return userCode;
    }

    public void setUserCode(int userCode) {
        this.userCode = userCode;
    }

    public int getCrewCode() {
        return crewCode;
    }

    public void setCrewCode(int crewCode) {
        this.crewCode = crewCode;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getIsApproval() {
        return isApproval;
    }

    public void setIsApproval(int isApproval) {
        this.isApproval = isApproval;
    }

    public boolean getIsPermission() {
        return isPermission;
    }

    public void setIsPermission(boolean permission) {
        this.isPermission = permission;
    }

    @Override
    public String toString() {
        return "CrewApplyDTO{" +
                "userCode=" + userCode +
                ", crewCode=" + crewCode +
                ", content='" + content + '\'' +
                ", isApproval=" + isApproval +
                ", isPermission=" + isPermission +
                '}';
    }
}

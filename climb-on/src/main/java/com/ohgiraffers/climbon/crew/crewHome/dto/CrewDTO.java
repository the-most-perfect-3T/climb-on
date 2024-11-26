package com.ohgiraffers.climbon.crew.crewHome.dto;

import java.sql.Date;

public class CrewDTO {

    private int id;
    private String crewName;
    private String imgUrl;
    private String description;
    private String crewSns;
    private String climbingCategory;
    private boolean recruitingStatus;
    private boolean permissionToJoin;
    private String activeArea;
    private int captainId;
    private boolean status;
    private Date createdAt;
    private Date deletedAt;

    public CrewDTO() {
    }

    public CrewDTO(int id, String crewName, String imgUrl, String description, String crewSns, String climbingCategory, boolean recruitingStatus, boolean permissionToJoin, String activeArea,int captainId, boolean status, Date createdAt, Date deletedAt) {
        this.id = id;
        this.crewName = crewName;
        this.imgUrl = imgUrl;
        this.description = description;
        this.crewSns = crewSns;
        this.climbingCategory = climbingCategory;
        this.recruitingStatus = recruitingStatus;
        this.permissionToJoin = permissionToJoin;
        this.activeArea = activeArea;
        this.captainId = captainId;
        this.status = status;
        this.createdAt = createdAt;
        this.deletedAt = deletedAt;
    }

    public int getCaptainId() {
        return captainId;
    }

    public void setCaptainId(int captainId) {
        this.captainId = captainId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCrewName() {
        return crewName;
    }

    public void setCrewName(String crewName) {
        this.crewName = crewName;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCrewSns() {
        return crewSns;
    }

    public void setCrewSns(String crewSns) {
        this.crewSns = crewSns;
    }

    public String getClimbingCategory() {
        return climbingCategory;
    }

    public void setClimbingCategory(String climbingCategory) {
        this.climbingCategory = climbingCategory;
    }

    public boolean getRecruitingStatus() {
        return recruitingStatus;
    }

    public void setRecruitingStatus(boolean recruitingStatus) {
        this.recruitingStatus = recruitingStatus;
    }

    public boolean getPermissionToJoin() {
        return permissionToJoin;
    }

    public void setPermissionToJoin(boolean permissionToJoin) {
        this.permissionToJoin = permissionToJoin;
    }

    public String getActiveArea() {
        return activeArea;
    }

    public void setActiveArea(String activeArea) {
        this.activeArea = activeArea;
    }

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(Date deletedAt) {
        this.deletedAt = deletedAt;
    }

    @Override
    public String toString() {
        return "CrewDTO{" +
                "id=" + id +
                ", crewName='" + crewName + '\'' +
                ", imgUrl='" + imgUrl + '\'' +
                ", description='" + description + '\'' +
                ", crewSns='" + crewSns + '\'' +
                ", climbingCategory='" + climbingCategory + '\'' +
                ", recruitingStatus=" + recruitingStatus +
                ", permissionToJoin=" + permissionToJoin +
                ", activeArea='" + activeArea + '\'' +
                ", captainId=" + captainId +
                ", status=" + status +
                ", createdAt=" + createdAt +
                ", deletedAt=" + deletedAt +
                '}';
    }
}

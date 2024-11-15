package com.ohgiraffers.climbon.user.dto;

import com.ohgiraffers.climbon.auth.Enum.UserRole;

import java.time.LocalDateTime;

public class UserDTO {
    private Integer id;                         // 사용자 고유 ID (Integer로 변경)
    private String nickname;                    // 사용자 닉네임
    private String userId;                      // 사용자 고유 아이디
    private String password;                    // 사용자 비밀번호 (해시 처리된 값)
    private UserRole userRole;                    // 사용자 역할 (예: 'user', 'admin')
    private String profilePic;                  // 사용자 프로필 사진 URL
    private String oneLiner;                    // 사용자 한줄 소개
    private int facilityCode;                   // 홈짐
    private int status;                         // 사용자 계정 상태 (0: 비활성, 1: 활성)
    private Boolean isBusiness;                 // 사용자 계정이 비즈니스인지 여부
    private LocalDateTime createdAt;            // 계정 생성 시간
    private LocalDateTime inactiveAt;           // 계정 비활성화 시간


    public UserDTO() {
    }

    public UserDTO(Integer id, String nickname, String userId, String password, UserRole userRole, String profilePic, String oneLiner, int facilityCode, int status, Boolean isBusiness, LocalDateTime createdAt, LocalDateTime inactiveAt) {
        this.id = id;
        this.nickname = nickname;
        this.userId = userId;
        this.password = password;
        this.userRole = userRole;
        this.profilePic = profilePic;
        this.oneLiner = oneLiner;
        this.facilityCode = facilityCode;
        this.status = status;
        this.isBusiness = isBusiness;
        this.createdAt = createdAt;
        this.inactiveAt = inactiveAt;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UserRole getUserRole() {
        return userRole;
    }

    public void setUserRole(UserRole userRole) {
        this.userRole = userRole;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Boolean getBusiness() {
        return isBusiness;
    }

    public void setBusiness(Boolean business) {
        isBusiness = business;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getInactiveAt() {
        return inactiveAt;
    }

    public void setInactiveAt(LocalDateTime inactiveAt) {
        this.inactiveAt = inactiveAt;
    }

    public String getOneLiner() {
        return oneLiner;
    }

    public void setOneLiner(String oneLiner) {
        this.oneLiner = oneLiner;
    }

    public int getFacilityCode() {
        return facilityCode;
    }

    public void setFacilityCode(int facilityCode) {
        this.facilityCode = facilityCode;
    }

    @Override
    public String toString() {
        return "UserDTO{" +
                "id=" + id +
                ", nickname='" + nickname + '\'' +
                ", userId='" + userId + '\'' +
                ", password='" + password + '\'' +
                ", userRole=" + userRole +
                ", profilePic='" + profilePic + '\'' +
                ", oneLiner='" + oneLiner + '\'' +
                ", facilityCode=" + facilityCode +
                ", status=" + status +
                ", isBusiness=" + isBusiness +
                ", createdAt=" + createdAt +
                ", inactiveAt=" + inactiveAt +
                '}';
    }
}

package com.ohgiraffers.climbon.crew.mycrew.dto;

import com.ohgiraffers.climbon.crew.mycrew.Enum.CrewRole;

public class UserCrewDTO {

    private Integer userCode;
    private Integer crewCode;
    private CrewRole role;

    public UserCrewDTO() {
    }

    public UserCrewDTO(Integer userCode, Integer crewCode, CrewRole role) {
        this.userCode = userCode;
        this.crewCode = crewCode;
        this.role = role;
    }

    public Integer getUserCode() {
        return userCode;
    }

    public void setUserCode(Integer userCode) {
        this.userCode = userCode;
    }

    public Integer getCrewCode() {
        return crewCode;
    }

    public void setCrewCode(Integer crewCode) {
        this.crewCode = crewCode;
    }

    public CrewRole getRole() {
        return role;
    }

    public void setRole(CrewRole role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "UserCrewDTO{" +
                "userCode=" + userCode +
                ", crewCode=" + crewCode +
                ", role='" + role + '\'' +
                '}';
    }
}

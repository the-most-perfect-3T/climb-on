package com.ohgiraffers.climbon.crew.mycrew.Enum;

public enum CrewRole {
    CAPTAIN("CAPTAIN"),
    MEMBER("MEMBER");

    private String role;

    CrewRole(String role) {
        this.role = role;
    }

    public String getRole() {
        return role;
    }

    @Override
    public String toString() {
        return "CrewRole{" +
                "role='" + role + '\'' +
                '}';
    }
}

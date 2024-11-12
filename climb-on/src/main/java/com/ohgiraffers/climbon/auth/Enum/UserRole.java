package com.ohgiraffers.climbon.auth.Enum;

public enum UserRole {
    USER("USER"),
    ADMIN("ADMIN");
    // 크루 추가해야함 (크루리더,크루원)

    private String role;

    UserRole(String role) {
        this.role = role;
    }

    public String getRole() {
        return role;
    }

    @Override
    public String toString() {
        return "UserRole{" +
                "role='" + role + '\'' +
                '}';
    }
}

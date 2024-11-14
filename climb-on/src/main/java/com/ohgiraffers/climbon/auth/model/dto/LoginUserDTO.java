package com.ohgiraffers.climbon.auth.model.dto;

import com.ohgiraffers.climbon.auth.Enum.UserRole;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LoginUserDTO {

    private Integer id;                         // 사용자 고유 ID (Integer로 변경)
    private String nickname;                    // 사용자 닉네임
    private String userId;                      // 사용자 고유 아이디
    private String password;                    // 사용자 비밀번호 (해시 처리된 값)
    private UserRole userRole;                  // 사용자 역할 (예: 'user', 'admin')


    public LoginUserDTO() {
    }

    public LoginUserDTO(Integer id, String nickname, String userId, String password, UserRole userRole) {
        this.id = id;
        this.nickname = nickname;
        this.userId = userId;
        this.password = password;
        this.userRole = userRole;
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

    public List<String> getRoleList(){
        if(this.userRole.getRole().length() > 0){
            return Arrays.asList(this.userRole.getRole().split(","));
        }
        return new ArrayList<>();
    }


}

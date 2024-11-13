package com.ohgiraffers.climbon.auth.model;

import com.ohgiraffers.climbon.auth.model.dto.LoginUserDTO;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;

public class AuthDetail implements UserDetails, OAuth2User {

    /*일반유저*/
    private LoginUserDTO loginUserDTO;

    /*소셜로그인*/
    private Map<String, Object> attributes;
    private String provider;
    private String accessToken;


    public AuthDetail() {
    }
    /*일반유저*/
    public AuthDetail(Optional<LoginUserDTO> loginUserDTO) {
        // 빈 값 에러 방지를 위해 Optional 사용
        this.loginUserDTO = loginUserDTO.get();
    }

    /*소셜로그인*/
    public AuthDetail(LoginUserDTO loginUserDTO, Map<String, Object> attributes, String provider, String accessToken) {
        this.loginUserDTO = loginUserDTO;
        this.attributes = attributes;
        this.provider = provider;
        this.accessToken = accessToken;
    }

    /*일반유저*/
    public LoginUserDTO getLoginUserDTO() {
        return loginUserDTO;
    }

    public void setLoginUserDTO(LoginUserDTO loginUserDTO) {
        this.loginUserDTO = loginUserDTO;
    }

    /*소셜로그인*/
    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    /**
     * 권한 목록 반환 메소드
     * */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        for(String role : loginUserDTO.getRoleList()){
            authorities.add(new GrantedAuthority() {
                @Override
                public String getAuthority() {
                    return role;
                }
            });
        }
        return authorities;
    }

    /*일반유저*/
    @Override
    public String getPassword() {
        return loginUserDTO.getPassword();
    }

    @Override
    public String getUsername() {
        return loginUserDTO.getUserId();
    }

    /*소셜로그인*/
    @Override
    public String getName() {
        return (String) attributes.get("email");
    }
    @Override
    public <A> A getAttribute(String name) {
        return (A) attributes.get(name);
    }


    /**계정만료*/
    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    /**계정잠금 - 비밀번호 몇번 실패*/
    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    /**탈퇴*/
    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    /**비활성화*/
    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }
}

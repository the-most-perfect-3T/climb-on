package com.ohgiraffers.climbon.auth.service;

import com.ohgiraffers.climbon.auth.model.AuthDetail;
import com.ohgiraffers.climbon.auth.model.dao.UserMapper;
import com.ohgiraffers.climbon.auth.model.dto.KakaoUserInfo;
import com.ohgiraffers.climbon.auth.model.dto.LoginUserDTO;
import com.ohgiraffers.climbon.auth.model.dto.OAuth2UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class Oauth2UserService extends DefaultOAuth2UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2User oAuth2User = super.loadUser(userRequest);

        try {
            // 토큰 확인
            String accessToken = userRequest.getAccessToken().getTokenValue();
            if (accessToken == null || accessToken.isEmpty()) {
                throw new OAuth2AuthenticationException("유효하지 않은 토큰입니다.");
            }


            // 제공자 확인
            String provider = userRequest.getClientRegistration().getRegistrationId();

            OAuth2UserInfo oAuth2UserInfo = null;
            if(provider.equals("kakao")) {
                System.out.println("카카오 로그인");
                oAuth2UserInfo = new KakaoUserInfo(oAuth2User.getAttributes());
            } else {
                throw new OAuth2AuthenticationException("허용되지 않는 인증입니다.");
            }


            // 이메일 확인
            if (oAuth2UserInfo.getEmail() == null || oAuth2UserInfo.getEmail().isEmpty()) {
                throw new OAuth2AuthenticationException("이메일 정보가 존재하지 않습니다.");
            }


            // db 검증
            LoginUserDTO dbUser = userMapper.findByUserId(oAuth2UserInfo.getEmail());
            if(Objects.isNull(dbUser)){
                throw new UsernameNotFoundException("회원 정보가 존재하지 않습니다. 회원가입 후 로그인해주세요.");
            }

            return new AuthDetail(dbUser, oAuth2User.getAttributes(), provider, accessToken);


        } catch (UsernameNotFoundException e) {
            throw new UsernameNotFoundException("회원정보가 존재하지 않습니다." + e.getMessage());
        } catch (Exception e) {
            throw new OAuth2AuthenticationException("토큰 처리 중 오류가 발생했습니다." + e.getMessage());
        }

    }
}

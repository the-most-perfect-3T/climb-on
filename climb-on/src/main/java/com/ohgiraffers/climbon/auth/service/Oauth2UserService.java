package com.ohgiraffers.climbon.auth.service;

import com.ohgiraffers.climbon.auth.model.AuthDetail;
import com.ohgiraffers.climbon.auth.model.dao.UserMapper;
import com.ohgiraffers.climbon.auth.model.dto.KakaoUserInfo;
import com.ohgiraffers.climbon.auth.model.dto.LoginUserDTO;
import com.ohgiraffers.climbon.auth.model.dto.OAuth2UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
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

        String provider = userRequest.getClientRegistration().getRegistrationId();

        OAuth2UserInfo oAuth2UserInfo = null;
        if(provider.equals("kakao")) {
            System.out.println("카카오 로그인 요청");
            oAuth2UserInfo = new KakaoUserInfo(oAuth2User.getAttributes());

        } else {
            System.out.println("우리는 카카오만 지원합니다.");
            throw new OAuth2AuthenticationException("허용되지 않는 인증입니다.");
        }

        LoginUserDTO dbUser = userMapper.findByUserId(oAuth2UserInfo.getEmail());
        if(Objects.isNull(dbUser)){
            throw new UsernameNotFoundException("신규 사용자입니다. 회원가입이 필요합니다.");
        }
        // 로그인 성공 시 액세스 토큰을 세션에 저장
        String accessToken = userRequest.getAccessToken().getTokenValue();
        System.out.println("로그인" + accessToken);


        return new AuthDetail(dbUser, oAuth2User.getAttributes(), provider, accessToken);
    }
}

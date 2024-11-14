package com.ohgiraffers.climbon.auth.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

import java.io.IOException;
import java.net.URLEncoder;

@Configuration
public class AuthFailHandler extends SimpleUrlAuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        String errorMessage = null;

        if(exception instanceof BadCredentialsException){
            errorMessage = "아이디가 존재하지 않거나 비밀번호가 일치하지 않습니다.";
        }else if(exception instanceof InternalAuthenticationServiceException){
            errorMessage = "서버에서 오류가 발생되었습니다.";
        } else if(exception instanceof AuthenticationCredentialsNotFoundException){
            errorMessage = "인증 요청이 거부되었습니다.";
        } else if(exception instanceof UsernameNotFoundException){
            errorMessage = "회원 정보가 존재하지 않습니다. 회원가입 후 로그인해주세요.";
        } else if(exception instanceof OAuth2AuthenticationException){
            errorMessage = "소셜 로그인 중 오류가 발생했습니다. 다시 시도해주세요.";
        } else {
            errorMessage = "알 수 없는 오류로 로그인 요청을 처리할 수 없습니다.";
        }

        errorMessage = URLEncoder.encode(errorMessage, "UTF-8");

        setDefaultFailureUrl("/auth/fail?message=" + errorMessage);

        super.onAuthenticationFailure(request, response, exception);
    }
}

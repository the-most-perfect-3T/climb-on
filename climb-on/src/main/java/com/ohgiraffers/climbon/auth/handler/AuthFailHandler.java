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
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

import java.io.IOException;
import java.net.URLEncoder;

@Configuration
public class AuthFailHandler extends SimpleUrlAuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        String errorMessage = null;

        if(exception instanceof BadCredentialsException){
            // 사용자의 아이디가 DB 에 존재하지 않는 경우 or 비밀번호가 맞지 않는 경우
            errorMessage = "아이디가 존재하지 않거나 비밀번호가 일치하지 않습니다.";
        }else if(exception instanceof InternalAuthenticationServiceException){
            // 서버에서 사용자 정보를 검증하는 과정에서 발생하는 에러
            errorMessage = "서버에서 오류가 발생되었습니다.";
        } else if(exception instanceof AuthenticationCredentialsNotFoundException){
            // 인증정보가 없는 상태에서 보안처리된 리소스에 접근하는 경우
            errorMessage = "인증 요청이 거부되었습니다.";
        } else if(exception instanceof UsernameNotFoundException){
            errorMessage = "회원 정보가 존재하지 않습니다. 회원가입이 필요합니다.";
        } else {
            errorMessage = "알 수 없는 오류로 로그인 요청을 처리할 수 없습니다.";
        }

        // 주소창에 한글 들어갈 때 깨져서 인코딩해줌.
        errorMessage = URLEncoder.encode(errorMessage, "UTF-8");

        // 요청이 실패했을 시 보낼 곳 지정 - 인코딩 후
        setDefaultFailureUrl("/auth/fail?message=" + errorMessage);


        // 위의 내용이 잘 처리가 안됐을 때 security 가 기본으로 처리
        super.onAuthenticationFailure(request, response, exception);
    }
}

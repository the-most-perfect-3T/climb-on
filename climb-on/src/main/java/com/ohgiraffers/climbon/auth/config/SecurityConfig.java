package com.ohgiraffers.climbon.auth.config;


import com.ohgiraffers.climbon.auth.handler.AuthFailHandler;
import com.ohgiraffers.climbon.auth.model.AuthDetail;
import com.ohgiraffers.climbon.auth.service.Oauth2UserService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**인증실패핸들러*/
    @Autowired
    private AuthFailHandler authFailHandler;

    /**비밀번호 인코딩*/
    @Bean
    public BCryptPasswordEncoder encoder(){return new BCryptPasswordEncoder();}

    /**정적 리소스 요청 제외*/
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer(){
        return web -> web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }

    /**소셜 로그인 사용자 정보를 처리할 서비스*/
    @Autowired
    private Oauth2UserService oauth2UserService;

    /**REST API 호출*/
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    /**kakao 관련*/
    @Value("${spring.security.oauth2.client.registration.kakao.client-id}")
    private String kakaoClientId;

    @Value("${kakao.logout-redirect-uri}")
    private String kakaoLogoutRedirectUri;



    /**필터체인 커스텀*/
    @Bean
    public SecurityFilterChain configure(HttpSecurity http) throws Exception {

        http.authorizeHttpRequests(auth -> {
            auth.requestMatchers("/auth/login", "/auth/signup", "/auth/fail", "/", "/auth/checkUserId", "/auth/checkName", "/error/**",
                    "/search", "/facilities/**", "/community", "/crew/*", "/events", "/api/posts/recent/paginated", "/api/posts/popular").permitAll();
            /*auth.requestMatchers("/admin/*").hasAnyAuthority(UserRole.ADMIN.getRole());
            auth.requestMatchers("/user/*").hasAnyAuthority(UserRole.USER.getRole());*/
            auth.anyRequest().authenticated();

        }).formLogin(login -> {
            login.loginPage("/auth/login");
            login.usernameParameter("userId");
            login.passwordParameter("password");
            login.defaultSuccessUrl("/", true);
            login.failureHandler(authFailHandler);

        }).oauth2Login(oauth2 -> {
            oauth2.loginPage("/oauth2/authorization/kakao")
            .defaultSuccessUrl("/", true)
            .userInfoEndpoint(userInfo -> userInfo.userService(oauth2UserService))
            .failureHandler(authFailHandler);
        }).logout(logout -> {
            logout.logoutRequestMatcher(new AntPathRequestMatcher("/auth/logout"));
            logout.deleteCookies("JSESSIONID");
            logout.invalidateHttpSession(true);
            logout.logoutSuccessUrl("/");
            logout.addLogoutHandler((request, response, authentication) -> {
                if (authentication != null) {
                    AuthDetail authDetail = (AuthDetail) authentication.getPrincipal();
                    String accessToken = authDetail.getAccessToken();

                    if (accessToken != null) {
                        String provider = authDetail.getProvider();

                        if(provider.equals("kakao")){
                            // 카카오 로그아웃 API 호출
                            String url1 = "https://kapi.kakao.com/v1/user/logout";
                            RestTemplate restTemplate = new RestTemplate();
                            HttpHeaders headers = new HttpHeaders();
                            headers.setBearerAuth(accessToken);

                            HttpEntity<String> entity = new HttpEntity<>(headers);

                            // 카카오와 함께 로그아웃 호출
                            /*String url = "https://kauth.kakao.com/oauth/logout";
                            String requestLogout = url + "?client_id=" + kakaoClientId + "&logout_redirect_uri=" + kakaoLogoutRedirectUri;*/

                            try {
                                ResponseEntity<String> responseEntity = restTemplate.postForEntity(url1, entity, String.class);

                                // 카카오 로그아웃 성공 여부 확인
                                if (responseEntity.getStatusCode().is2xxSuccessful()) {
                                    /*response.sendRedirect(requestLogout);*/
                                    System.out.println("카카오 로그아웃 성공");
                                } else {
                                    System.out.println("카카오 로그아웃 실패: " + responseEntity.getStatusCode());
                                    throw new RuntimeException("카카오 로그아웃 실패: 상태 코드 " + responseEntity.getStatusCode());
                                }
                            } /*catch (IOException e) {
                                System.out.println("카카오 로그아웃 실패: " + e.getMessage());
                                throw new RuntimeException("카카오 로그아웃 중 IOException 발생: " + e.getMessage(), e);
                            }*/catch(Exception e){
                                e.printStackTrace();
                            }
                            authDetail.setAccessToken(null);
                        }
                    } else {
                        System.out.println("액세스 토큰이 없습니다.");
                    }
                }
            });

        }).sessionManagement(session -> {
            session.maximumSessions(1);
            session.invalidSessionUrl("/");
        }).csrf(csrf -> {
            csrf.disable();
        });

        return http.build();
    }
}


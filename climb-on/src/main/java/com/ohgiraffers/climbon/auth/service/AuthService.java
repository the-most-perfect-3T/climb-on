package com.ohgiraffers.climbon.auth.service;


import com.ohgiraffers.climbon.auth.model.AuthDetail;
import com.ohgiraffers.climbon.auth.model.dto.LoginUserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;


@Service
public class AuthService implements UserDetailsService {

    @Autowired
    private SignupService signupService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        LoginUserDTO login = signupService.findByUserId(username);

        if(Objects.isNull(login)){
            throw new UsernameNotFoundException("회원 정보가 존재하지 않습니다.");
        }

        return new AuthDetail(Optional.of(login));
    }
}

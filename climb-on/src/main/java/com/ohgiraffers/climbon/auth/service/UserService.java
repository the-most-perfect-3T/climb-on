package com.ohgiraffers.climbon.auth.service;

import com.ohgiraffers.climbon.auth.model.dao.UserMapper;
import com.ohgiraffers.climbon.auth.model.dto.LoginUserDTO;
import com.ohgiraffers.climbon.auth.model.dto.SignupDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PasswordEncoder encoder;

    /**회원가입 - db에 저장*/
    @Transactional
    public int regist(SignupDTO user) {

        // 유효성 검증 2차
        if(user.getUserId().isEmpty() || user.getUserId() == null){
            return 0;
        }
        if(user.getPassword().isEmpty() || user.getPassword() == null){
            return 0;
        }
        if(user.getNickname().isEmpty() || user.getNickname() == null){
            return 0;
        }

        // 비밀번호 암호화
        user.setPassword(encoder.encode(user.getPassword()));

        // 기본 값 추가
        user.setUserRole("USER");
        user.setStatus(1);

        // 프로필 이미지 api 나중에 !

        int result = userMapper.save(user);

        return result;
    }

    /**userId(email) 로 찾기
     * @return LoginUserDTO
     * */
    public LoginUserDTO findByUserId(String username) {
        LoginUserDTO loginUserDTO = userMapper.findByUserId(username);

        return loginUserDTO;
    }

    /**userId가 있는지 확인
     * @return boolean
     * */
    public boolean isUserIdExists(String userId) {
        LoginUserDTO login = userMapper.findByUserId(userId);
        if(Objects.isNull(login)){
            return false;
        }
        return true;
    }
}

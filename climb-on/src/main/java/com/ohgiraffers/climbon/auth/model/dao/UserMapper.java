package com.ohgiraffers.climbon.auth.model.dao;

import com.ohgiraffers.climbon.auth.model.dto.LoginUserDTO;
import com.ohgiraffers.climbon.auth.model.dto.SignupDTO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {

    int save(SignupDTO user);

    LoginUserDTO findByUserId(String username);

    LoginUserDTO findByNickname(String nickname);
}

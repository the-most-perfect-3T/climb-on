package com.ohgiraffers.climbon.user.dao;

import com.ohgiraffers.climbon.user.dto.UserDTO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {

    UserDTO findByKey(Integer key);

    String findCrewName(Integer key);

    String findHomeName(Integer key);


    int updateUser(UserDTO user);
}

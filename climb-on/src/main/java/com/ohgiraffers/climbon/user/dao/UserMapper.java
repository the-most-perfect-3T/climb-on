package com.ohgiraffers.climbon.user.dao;

import com.ohgiraffers.climbon.user.dto.UserDTO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@Mapper
public interface UserMapper {

    UserDTO findByKey(Integer key);

    String findCrewName(Integer key);

    String findHomeName(Integer key);


    int updateUser(UserDTO user);


    int updateProfile(Map<String, Object> map);
}

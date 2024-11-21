package com.ohgiraffers.climbon.user.dao;

import com.ohgiraffers.climbon.user.dto.NoticeDTO;
import com.ohgiraffers.climbon.user.dto.UserDTO;
import org.apache.ibatis.annotations.Mapper;


import java.util.List;
import java.util.Map;

@Mapper
public interface UserMapper {

    UserDTO findByKey(Integer key);

    String findCrewName(Integer key);

    String findHomeName(Integer key);


    int updateUser(UserDTO user);


    int updateProfile(Map<String, Object> map);

    int updateStatus(Map<String, Object> map);

    int saveApply(Map<String, Object> map);

    int saveAdminNotice(Integer key);

    String findById(Integer userCode);

    List<NoticeDTO> selectAdminNotice();


    int updateNotice(NoticeDTO notice);
}

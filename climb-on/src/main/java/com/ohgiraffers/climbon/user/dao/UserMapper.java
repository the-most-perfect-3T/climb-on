package com.ohgiraffers.climbon.user.dao;

import com.ohgiraffers.climbon.user.dto.NoticeDTO;
import com.ohgiraffers.climbon.user.dto.UserDTO;
import org.apache.ibatis.annotations.Mapper;


import java.util.List;
import java.util.Map;

@Mapper
public interface UserMapper {

    UserDTO findByKey(Integer id);

    String findCrewName(Integer id);

    String findHomeName(Integer id);


    int updateUser(UserDTO user);


    int updateProfile(Map<String, Object> map);

    int updateStatus(Map<String, Object> map);

    int saveApply(Map<String, Object> map);

    int saveAdminNotice(NoticeDTO noticeDTO);

    String findById(Integer userCode);

    List<NoticeDTO> selectAdminNotice();


    int updateNotice(NoticeDTO noticeDTO);

    int saveBusinessNotice(NoticeDTO noticeDTO);

    int saveUserNotice(NoticeDTO noticeDTO);

    int findByIdIsApproval(Integer id);

    int updateRole(UserDTO userDTO);
}

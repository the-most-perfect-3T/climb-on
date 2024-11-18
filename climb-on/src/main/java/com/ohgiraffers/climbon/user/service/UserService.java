package com.ohgiraffers.climbon.user.service;

import com.ohgiraffers.climbon.user.dao.UserMapper;
import com.ohgiraffers.climbon.user.dto.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PasswordEncoder encoder;

    /**
     * user의 pk로 user정보 불러오기
     * @Param Integer key
     * @return UserDTO
     * */
    public UserDTO findByKey(Integer key) {
        if(key == null){
            return null;
        }
        UserDTO user = userMapper.findByKey(key);

        return user;
    }

    /**
     * user의 pk로 크루이름 찾기
     * @Param Integer key
     * @return String
     * */
    public String findCrewName(Integer key) {
        if(key == null){
            return null;
        }
        String crewName = userMapper.findCrewName(key);

        return crewName;
    }

    /**
     * user의 pk로 홈짐 찾기
     * @Param Integer key
     * @return String
     * */
    public String findHomeName(Integer key) {
        if(key == null){
            return null;
        }
        String homeName = userMapper.findHomeName(key);

        return homeName;
    }

    /**
     * user 정보 업데이트 (닉네임, 비밀번호, 한줄소개)
     * @Param UserDTO user, Integer key
     * @return int
     * */
    @Transactional
    public int updateUser(UserDTO user, Integer key) {
        if (user == null || key == null) {
            return 0;
        }
        user.setId(key);
        return userMapper.updateUser(user);
    }

    /**
     * user 프로필 업데이트
     * @Param String profilePic, Integer key
     * @return int
     * */
    @Transactional
    public int updateProfile(String profilePic, Integer key) {
        if(profilePic == null || key == null){
            return 0;
        }

        Map<String, Object> map = new HashMap<>();
        map.put("profilePic", profilePic);
        map.put("id", key);
        System.out.println("profilePic = " + profilePic);

        int result = userMapper.updateProfile(map);

        return result;
    }

    /**
     * user 상태 업데이트(비활성)
     * @Param Integer key
     * @return int
     * */
    @Transactional
    public int updateStatus(Integer key) {
        if(key == null){
            return 0;
        }

        Map<String, Object> map = new HashMap<>();
        map.put("id", key);
        map.put("inactiveAt", LocalDateTime.now());

        int result = userMapper.updateStatus(map);

        return result;
    }
}

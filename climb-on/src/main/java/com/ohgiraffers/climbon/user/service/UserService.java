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
     * user 의 pk로 user 전체 정보 불러오기
     * */
    public UserDTO findByKey(Integer key) {
        if(key == null){
            return null;
        }
        UserDTO user = userMapper.findByKey(key);

        return user;
    }

    /**
     * user 의 pk로 크루이름 찾기
     * */
    public String findCrewName(Integer key) {
        if(key == null){
            return null;
        }
        String crewName = userMapper.findCrewName(key);

        return crewName;
    }

    /**
     * user 의 pk로 홈짐 찾기
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

    /**
     * 비즈니스계정 전환 신청
     * */
    @Transactional
    public int registBusiness(String newFileName, Integer key) {

        if(newFileName == null || key == null){
            return 0;
        }

        Map<String, Object> map = new HashMap<>();
        map.put("id", key);
        map.put("attachFile", newFileName);

        int result = userMapper.saveNotice(map);

        return result;
    }
}

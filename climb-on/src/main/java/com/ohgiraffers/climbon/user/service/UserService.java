package com.ohgiraffers.climbon.user.service;

import com.ohgiraffers.climbon.user.dao.UserMapper;
import com.ohgiraffers.climbon.user.dto.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PasswordEncoder encoder;


    public UserDTO findByKey(Integer key) {
        if(key == null){
            return null;
        }
        UserDTO user = userMapper.findByKey(key);

        return user;
    }

    public String findCrewName(Integer key) {
        if(key == null){
            return null;
        }
        String crewName = userMapper.findCrewName(key);

        return crewName;
    }

    public String findHomeName(Integer key) {
        if(key == null){
            return null;
        }
        String homeName = userMapper.findHomeName(key);

        return homeName;
    }


    public int updateUser(UserDTO user, Integer key) {
        if (user == null || key == null) {
            return 0;
        }
        user.setId(key);
        return userMapper.updateUser(user);
    }

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



}

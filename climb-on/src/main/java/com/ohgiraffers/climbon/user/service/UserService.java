package com.ohgiraffers.climbon.user.service;

import com.ohgiraffers.climbon.user.dao.UserMapper;
import com.ohgiraffers.climbon.user.dto.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PasswordEncoder encoder;


    public UserDTO findByKey(Integer key) {
        UserDTO user = userMapper.findByKey(key);

        return user;
    }

    public String findCrewName(Integer key) {

        String crewName = userMapper.findCrewName(key);

        return crewName;
    }

    public String findHomeName(Integer key) {
        String homeName = userMapper.findHomeName(key);

        return homeName;
    }


    public int updateUser(UserDTO user) {

        user.setPassword(encoder.encode(user.getPassword()));
        int result = userMapper.updateUser(user);

        return result;
    }
}

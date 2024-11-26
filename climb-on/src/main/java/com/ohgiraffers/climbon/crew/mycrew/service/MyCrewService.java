package com.ohgiraffers.climbon.crew.mycrew.service;

import com.ohgiraffers.climbon.crew.crewHome.dto.CrewDTO;
import com.ohgiraffers.climbon.crew.mycrew.dao.MyCrewMapper;
import com.ohgiraffers.climbon.crew.mycrew.dto.UserCrewDTO;
import com.ohgiraffers.climbon.user.dto.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MyCrewService
{
    @Autowired
    private MyCrewMapper myCrewMapper;


    public UserCrewDTO getMyCrewCodeAndRole(int myId) {
        return myCrewMapper.getMyCrewCodeAndRole(myId);
    }

    public CrewDTO getMyCrewById(int myId) {
        return myCrewMapper.getMyCrewById(myId);
    }

    public List<UserDTO> getCrewMemeberList(int id) {
        return myCrewMapper.getCrewMemeberList(id);
    }
}

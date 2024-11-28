package com.ohgiraffers.climbon.crew.mycrew.service;

import com.ohgiraffers.climbon.crew.crewHome.dto.CrewDTO;
import com.ohgiraffers.climbon.crew.mycrew.dao.MyCrewMapper;
import com.ohgiraffers.climbon.crew.mycrew.dto.CrewMembersDTO;
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

    public List<CrewMembersDTO> getCrewMemberList(int crewCode) {
        return myCrewMapper.getCrewMemberList(crewCode);
    }

    public List<String> getImgUrlList(Integer key) {
        return myCrewMapper.getImgUrlList(key);
    }

    public CrewDTO getCrewInfoByCrewCode(Integer crewCode) {
        return myCrewMapper.getCrewInfoByCrewCode(crewCode);
    }

    public int getMemberCount(int crewCode) {
        return myCrewMapper.getMemberCount(crewCode);
    }
}

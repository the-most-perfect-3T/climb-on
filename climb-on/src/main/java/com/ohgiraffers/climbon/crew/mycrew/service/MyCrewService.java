package com.ohgiraffers.climbon.crew.mycrew.service;

import com.ohgiraffers.climbon.crew.crewHome.dto.CrewDTO;
import com.ohgiraffers.climbon.crew.crewHome.dto.CrewPostDTO;
import com.ohgiraffers.climbon.crew.mycrew.dao.MyCrewMapper;
import com.ohgiraffers.climbon.crew.mycrew.dto.CrewApplyDTO;
import com.ohgiraffers.climbon.crew.mycrew.dto.CrewApplyWithUserInfoDTO;
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

    public int applyForCrew(CrewApplyDTO crewApplication) {
        return myCrewMapper.applyForCrew(crewApplication);
    }

    public boolean getHowToJoinCrew(int crewCode) {
        return myCrewMapper.getHowToJoinCrew(crewCode);
    }

    public int crewMemberInsert(UserCrewDTO newMember) {
        return myCrewMapper.crewMemberInsert(newMember);
    }

    public CrewApplyDTO getCrewApplyContent(int myId) {
        return myCrewMapper.getCrewApplyContent(myId);
    }

    public List<CrewApplyWithUserInfoDTO> getNewCrewApplyContentByCrewCode(int crewCode) {
        return myCrewMapper.getNewCrewApplyContentByCrewCode(crewCode);
    }


    public CrewApplyWithUserInfoDTO getCrewApplyWithUserInfo(int userId) {
        return  myCrewMapper.getCrewApplyWithUserInfo(userId);
    }

    public int updateCrewApplyResult(int userCode, int isApproval) {
        return myCrewMapper.updateCrewApplyResult(userCode, isApproval);
    }

    public void alertUser(int userCode, int category) {
        myCrewMapper.alertUser(userCode, category);
    }

    public List<CrewPostDTO> getCrewPostsList(int crewCode)
    {
        return myCrewMapper.getCrewPostsList(crewCode);
    }
}

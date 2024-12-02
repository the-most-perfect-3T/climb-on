package com.ohgiraffers.climbon.crew.mycrew.dto;

import com.ohgiraffers.climbon.crew.crewHome.dto.CrewDTO;

import java.util.List;

public class CrewMemberListWithCrewApplyList {

    private List<CrewApplyWithUserInfoDTO> crewApplys;

    private List<CrewMembersDTO> memberList;

    public CrewMemberListWithCrewApplyList() {
    }

    public List<CrewApplyWithUserInfoDTO> getCrewApplys() {
        return crewApplys;
    }

    public void setCrewApplys(List<CrewApplyWithUserInfoDTO> crewApplys) {
        this.crewApplys = crewApplys;
    }

    public List<CrewMembersDTO> getMemberList() {
        return memberList;
    }

    public void setMemberList(List<CrewMembersDTO> memberList) {
        this.memberList = memberList;
    }

    public CrewMemberListWithCrewApplyList(List<CrewApplyWithUserInfoDTO> crewApplys, List<CrewMembersDTO> memberList) {
        this.crewApplys = crewApplys;
        this.memberList = memberList;
    }

    @Override
    public String toString() {
        return "CrewMemberListWithCrewApplyList{" +
                "crewApplys=" + crewApplys +
                ", memberList=" + memberList +
                '}';
    }
}

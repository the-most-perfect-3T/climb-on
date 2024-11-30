package com.ohgiraffers.climbon.crew.mycrew.dao;

import com.ohgiraffers.climbon.crew.crewHome.dto.CrewDTO;
import com.ohgiraffers.climbon.crew.crewHome.dto.CrewPostDTO;
import com.ohgiraffers.climbon.crew.mycrew.dto.CrewApplyDTO;
import com.ohgiraffers.climbon.crew.mycrew.dto.CrewApplyWithUserInfoDTO;
import com.ohgiraffers.climbon.crew.mycrew.dto.CrewMembersDTO;
import com.ohgiraffers.climbon.crew.mycrew.dto.UserCrewDTO;
import com.ohgiraffers.climbon.user.dto.UserDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MyCrewMapper
{

    UserCrewDTO getMyCrewCodeAndRole(int myId);

    CrewDTO getMyCrewById(int myId);

    List<CrewMembersDTO> getCrewMemberList(int crewCode);

    List<String> getImgUrlList(Integer key);

    CrewDTO getCrewInfoByCrewCode(Integer crewCode);

    int getMemberCount(int crewCode);

    int applyForCrew(CrewApplyDTO crewApplication);

    boolean getHowToJoinCrew(int crewCode);

    int crewMemberInsert(UserCrewDTO newMember);

    CrewApplyDTO getCrewApplyContent(int myId);

    List<CrewApplyWithUserInfoDTO> getNewCrewApplyContentByCrewCode(int crewCode);

    List<CrewPostDTO> getCrewPostsList(int crewCode);
}

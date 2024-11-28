package com.ohgiraffers.climbon.crew.mycrew.dao;

import com.ohgiraffers.climbon.crew.crewHome.dto.CrewDTO;
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

    List<CrewMembersDTO> getCrewMemberList(int key);

    List<String> getImgUrlList(Integer key);
}

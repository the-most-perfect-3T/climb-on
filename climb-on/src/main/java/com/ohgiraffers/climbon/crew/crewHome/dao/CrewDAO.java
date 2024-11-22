package com.ohgiraffers.climbon.crew.crewHome.dao;

import com.ohgiraffers.climbon.crew.crewHome.dto.CrewBoardDTO;
import com.ohgiraffers.climbon.crew.crewHome.dto.CrewDTO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CrewDAO {


    CrewDTO isCrewNameExists(String crewName);

    int registerCrew(CrewDTO crewDTO);
}

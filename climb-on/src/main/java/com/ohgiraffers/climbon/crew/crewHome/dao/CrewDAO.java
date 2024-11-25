package com.ohgiraffers.climbon.crew.crewHome.dao;

import com.ohgiraffers.climbon.crew.crewHome.dto.CrewDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface CrewDAO {


    CrewDTO isCrewNameExists(String crewName);

    int registerCrew(CrewDTO crewDTO);

    List<CrewDTO> selectFiveCrews(Map<String, Object> map);

    int countAllCrews();

    List<CrewDTO> selectCrews();

    int countCrewsFilteredByAreas(List<String> areas);

    List<CrewDTO> getRecruitingCrews();
}

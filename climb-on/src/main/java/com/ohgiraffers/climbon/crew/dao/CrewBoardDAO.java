package com.ohgiraffers.climbon.crew.dao;

import com.ohgiraffers.climbon.crew.dto.CrewBoardDTO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CrewBoardDAO {

    int insertPost(CrewBoardDTO crewBoardDTO);
}

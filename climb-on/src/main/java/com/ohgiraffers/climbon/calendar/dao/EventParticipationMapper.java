package com.ohgiraffers.climbon.calendar.dao;

import com.ohgiraffers.climbon.calendar.dto.CrewEventDTO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface EventParticipationMapper
{
    void participateCrewEvents(CrewEventDTO crewEventDTO);

    int checkParticipateMember(CrewEventDTO crewEventDTO);
}

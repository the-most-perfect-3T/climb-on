package com.ohgiraffers.climbon.calendar.service;

import com.ohgiraffers.climbon.calendar.dao.EventParticipationMapper;
import com.ohgiraffers.climbon.calendar.dto.CrewEventDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EventParticipationService
{
    @Autowired
    private EventParticipationMapper eventParticipationMapper;


    public void participateCrewEvents(int crewCode, int userCode, int eventCode)
    {
        eventParticipationMapper.participateCrewEvents(new CrewEventDTO(crewCode, userCode, eventCode));
    }
}

package com.ohgiraffers.climbon.calendar.dao;

import com.ohgiraffers.climbon.calendar.dto.CrewEventDTO;
import com.ohgiraffers.climbon.calendar.dto.EventDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface EventMapper
{
    List<EventDTO> getAllEvents(int userCode);
    List<EventDTO> getAllEventsFromCrew(int crewCode);

    void insertEvent(EventDTO event);

    int checkDuplicate(String title, String start, String end, int userCode);

    void deleteEvent(int id);

    void modifyEvent(EventDTO event);

    List<EventDTO> getEventsByType(String type);

    List<EventDTO> getMainEvents(boolean admin);

    Integer isUserInCrew(CrewEventDTO crewEventDTO);

    List<EventDTO> getAllCrewsEvents();
}

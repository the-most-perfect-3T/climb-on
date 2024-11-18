package com.ohgiraffers.climbon.calendar.dao;

import com.ohgiraffers.climbon.calendar.dto.EventDTO;
import org.apache.ibatis.annotations.Mapper;

import java.sql.Date;
import java.util.List;

@Mapper
public interface EventMapper
{
    List<EventDTO> getAllEvents(int userCode);
    List<EventDTO> getAllEventsFromCrew();

    void insertEvent(EventDTO event);

    int checkDuplicate(String title, String start, String end);

    void deleteEvent(int id);

    void modifyEvent(EventDTO event);
}

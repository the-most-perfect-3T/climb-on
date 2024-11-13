package com.ohgiraffers.climbon.calendar.dao;

import com.ohgiraffers.climbon.calendar.dto.EventDTO;
import org.apache.ibatis.annotations.Mapper;

import java.sql.Date;
import java.util.List;

@Mapper
public interface EventMapper
{
    List<EventDTO> getAllEvents();
    void insertEvent(EventDTO event);

    int checkDuplicate(String title, String start);
}

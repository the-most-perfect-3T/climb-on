package com.ohgiraffers.climbon.calendar.service;

import com.ohgiraffers.climbon.calendar.dao.EventMapper;
import com.ohgiraffers.climbon.calendar.dto.EventDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.List;

@Service
public class EventService
{
    @Autowired
    private EventMapper eventMapper;

    public List<EventDTO> getAllEvents()
    {
        return eventMapper.getAllEvents();
    }

    public void addEvent(EventDTO event)
    {
        eventMapper.insertEvent(event);
    }

    public boolean checkDuplicate(String title, String start)
    {
        int result = eventMapper.checkDuplicate(title, start);
        System.out.println(result + " <= 이것은 result 값");
        return result > 0 ? true : false;
    }
}

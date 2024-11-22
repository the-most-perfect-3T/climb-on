package com.ohgiraffers.climbon.calendar.service;

import com.ohgiraffers.climbon.calendar.dao.EventMapper;
import com.ohgiraffers.climbon.calendar.dto.EventDTO;
import jdk.jfr.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.List;

@Service
public class EventService
{
    @Autowired
    private EventMapper eventMapper;

    public List<EventDTO> getAllEvents(int userCode)
    {
        return eventMapper.getAllEvents(userCode);
    }

    public List<EventDTO> getCrewEvents()
    {
        return eventMapper.getAllEventsFromCrew();
    }

    public void addEvent(EventDTO event)
    {
        eventMapper.insertEvent(event);
    }

    public boolean checkDuplicate(String title, String start, String end, int userCode)
    {
        int result = eventMapper.checkDuplicate(title, start, end, userCode);
        System.out.println(result + " <= 이것은 result 값");
        return result > 0 ? true : false;
    }

    public void deleteEvent(int id)
    {
        System.out.println("delete Event 실행");
        eventMapper.deleteEvent(id);
    }

    public void modifyEvent(EventDTO event)
    {
        System.out.println("+++ modifyEvent called +++");
        eventMapper.modifyEvent(event);
    }
}

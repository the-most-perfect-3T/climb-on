package com.ohgiraffers.climbon.calendar.service;

import com.ohgiraffers.climbon.calendar.dao.EventMapper;
import com.ohgiraffers.climbon.calendar.dto.CrewEventDTO;
import com.ohgiraffers.climbon.calendar.dto.EventDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public List<EventDTO> getCrewEvents(int crewCode)
    {
        return eventMapper.getAllEventsFromCrew();
    }

    public boolean isUserInTeam(CrewEventDTO crewEventDTO) throws Exception
    {
        int result = eventMapper.isUserInCrew(crewEventDTO);
        System.out.println("EventService isUserInTeam? : " + result);
        return result > 0 ? true : false;
    }

    public void addEvent(EventDTO event)
    {
        eventMapper.insertEvent(event);
    }

    public boolean checkDuplicate(String title, String start, String end, int userCode)
    {
        int result = eventMapper.checkDuplicate(title, start, end, userCode);
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

    public List<EventDTO> getEventsByType(String type)
    {
        System.out.println("getEventsByType called ");
        return eventMapper.getEventsByType(type);
    }

    public List<EventDTO> getMainEvents(boolean role)
    {
        System.out.println("getMainEvents called ");
        return eventMapper.getMainEvents(role);
    }

}

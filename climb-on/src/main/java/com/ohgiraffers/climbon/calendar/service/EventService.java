package com.ohgiraffers.climbon.calendar.service;

import com.ohgiraffers.climbon.calendar.dao.EventMapper;
import com.ohgiraffers.climbon.calendar.dto.CrewEventDTO;
import com.ohgiraffers.climbon.calendar.dto.EventDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
        List<EventDTO> crewEvents = eventMapper.getAllEventsFromCrew(crewCode);
        return eventMapper.getAllEventsFromCrew(crewCode);
    }

    public boolean isUserInCrew(CrewEventDTO crewEventDTO) throws Exception
    {
        System.out.println(crewEventDTO.getUserCode());
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

    public List<EventDTO> getAllCrewsEvents()
    {
        List<EventDTO> crewsEvents = eventMapper.getAllCrewsEvents();
        List<EventDTO> filteredEvents = new ArrayList<>();

        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
        Date now = new Date();
        String today = sdf1.format(now);

        for(EventDTO crewEvent : crewsEvents)
        {
            String eventStartDate = (crewEvent.getStart().length()>11)?crewEvent.getStart().substring(0, 11):crewEvent.getStart();
            if(crewEvent.getEnd()!=null)
            {
                String eventEndDate = (crewEvent.getEnd().length()>11)?crewEvent.getEnd().substring(0, 11):crewEvent.getEnd();
                if(eventEndDate.compareTo(today) > 0)
                {
                    filteredEvents.add(crewEvent);
                    System.out.println(crewEvent.getTitle());
                    continue;
                }
            }

            if(!(eventStartDate.compareTo(today) < 0))
            {
                filteredEvents.add(crewEvent);
                System.out.println(crewEvent.getTitle());
            }
        }
        return filteredEvents;
    }
}

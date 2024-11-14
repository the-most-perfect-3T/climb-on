/*
*   2024-11-13 최초 작성
*   작성자: 최예지
* */
package com.ohgiraffers.climbon.calendar.controller;

import com.ohgiraffers.climbon.calendar.dto.EventDTO;
import com.ohgiraffers.climbon.calendar.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/events")
public class EventController
{
    @Autowired
    private EventService eventService;

    @GetMapping
    public List<EventDTO> getAllEvents()
    {
        return eventService.getAllEvents();
    }

    @PostMapping("/batch")
    public void addEvent(@RequestBody List<EventDTO> events)
    {
        if(events==null || events.size()==0)
        {
            System.out.println("이벤트가 없습니다!");
        }

        for (EventDTO event : events)
        {
            System.out.println("이벤트 색깔: " + event.getBackgroundColor());

            if(eventService.checkDuplicate(event.getTitle(), event.getStart()))
            {
                // title, start가 일치하는지 count로 반환. 0보다 크면 true
                continue;
            }
            eventService.addEvent(event);
        }
    }
}

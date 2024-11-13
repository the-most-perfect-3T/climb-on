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

    @PostMapping("/events/batch")
    public void addEvent(@RequestBody List<EventDTO> events)
    {
        for (EventDTO event : events)
        {
            eventService.addEvent(event);
        }
    }
}

/*
*   2024-11-13 최초 작성
*   작성자: 최예지
* */
package com.ohgiraffers.climbon.calendar.controller;

import com.ohgiraffers.climbon.auth.model.AuthDetail;
import com.ohgiraffers.climbon.calendar.dto.EventDTO;
import com.ohgiraffers.climbon.calendar.service.EventService;
import jdk.jfr.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/events")
public class EventController
{
    public int userCode =0;
    @Autowired
    private EventService eventService;

    @GetMapping
    public List<EventDTO> getAllEvents(@AuthenticationPrincipal AuthDetail userDetails)
    {
        if(userDetails == null || userDetails.getLoginUserDTO() == null)
        {
            // 로그인 하지 않은 유저들에게도 다 보여주는 정보
            System.out.println("No login user");
        }
        userCode = userDetails.getLoginUserDTO().getId();
        return eventService.getAllEvents(userCode);
    }

    @PostMapping("/batch")
    public void addEvent(@RequestBody List<EventDTO> events)
    {
        if(events==null || events.size()==0 || userCode==0)
        {
            System.out.println("이벤트를 저장할 수 없습니다.");
        }

        for (EventDTO event : events)
        {
            event.setUserCode(userCode);
            // 조건 검사
            if(eventService.checkDuplicate(event.getTitle(), event.getStart(), event.getEnd(), event.getUserCode()))
            {
                // 매개변수로 넘긴 조건들이 일치하는 데이터가 있는지 count로 반환. 0보다 크면 true
                continue;
            }
            eventService.addEvent(event);
        }
    }

    @PostMapping("/modify")
    public void modifyEvent(@RequestBody EventDTO event)
    {
        if(event == null)
        {
            System.out.println("이벤트를 수정할 수 없음: null");
        }

        eventService.modifyEvent(event);
    }

    @PostMapping("/{id}")
    public void deleteEvent(@PathVariable("id") int id)
    {
        eventService.deleteEvent(id);
    }
}

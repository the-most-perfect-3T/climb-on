/*
*   2024-11-13 최초 작성
*   작성자: 최예지
* */
package com.ohgiraffers.climbon.calendar.controller;

import com.ohgiraffers.climbon.auth.Enum.UserRole;
import com.ohgiraffers.climbon.auth.model.AuthDetail;
import com.ohgiraffers.climbon.calendar.dto.CrewEventDTO;
import com.ohgiraffers.climbon.calendar.dto.EventDTO;
import com.ohgiraffers.climbon.calendar.service.EventService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@RestController
@RequestMapping("events")
public class EventController
{
    @Autowired
    private EventService eventService;

    // 큰 대회 이벤트 가져옴
    @GetMapping
    public List<EventDTO> getPublicEvents(@AuthenticationPrincipal AuthDetail userDetails)
    {
        if(userDetails != null)
        {
            if(userDetails.getLoginUserDTO().getUserRole()== UserRole.ADMIN) {
                System.out.println("EventController.getPublicEvents.   ::   you are admin");
            }
        }
        return eventService.getMainEvents(true);
    }

    // 마이크루 이벤트
    @GetMapping("/myCrew")
    public ResponseEntity<?> getMyCrewEvents(@RequestParam Integer crewCode, @AuthenticationPrincipal AuthDetail userDetails) throws Exception
    {
        // 수정 필요
        int userCode = userDetails.getLoginUserDTO().getId();
        if(!eventService.isUserInCrew(new CrewEventDTO(userCode, crewCode))){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("해당 크루 멤버가 아님");
        }
        List<EventDTO> crewEvents = eventService.getCrewEvents(crewCode);
        return ResponseEntity.ok(crewEvents);
    }

    @GetMapping("/crew")
    public ResponseEntity<?> getCrewEvents(@RequestParam Integer crewCode)
    {
        List<EventDTO> crewEvents = eventService.getCrewEvents(1);
        return ResponseEntity.ok(crewEvents);
    }

    // 마이페이지 캘린더
    @GetMapping("/mypage")
    public List<EventDTO> getPrivateEvents(@AuthenticationPrincipal AuthDetail userDetails)
    {
        if(userDetails == null) {
            System.out.println("No login user");
            return null;
        }

        int userCode = userDetails.getLoginUserDTO().getId(); // user mypage에서 보여줄 내용
        return eventService.getAllEvents(userCode);
    }

    // 이벤트 저장 로직
    @PostMapping("/batch")
    public void addEvent(@RequestBody EventDTO event, @AuthenticationPrincipal AuthDetail userDetails) {
        if (event == null || userDetails == null) {
            System.out.println("이벤트를 저장할 수 없습니다.");
        }
        else {
            event.setUserCode(userDetails.getLoginUserDTO().getId());
            if(userDetails.getLoginUserDTO().getUserRole()== UserRole.ADMIN) {
                event.setAdmin(true);
            }
            if (eventService.checkDuplicate(event.getTitle(), event.getStart(), event.getEnd(), event.getUserCode())) {
                // 매개변수로 넘긴 조건들이 일치하는 데이터가 있는지 count로 반환. 0보다 크면 true
                System.out.println("이벤트에 중복 있는 것 같애 괜찮아?");
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

    @GetMapping("/allevent")
    public List<EventDTO> getAllCrewsEvents(ModelAndView mv)
    {
        List<EventDTO> allCrewEvents = eventService.getAllCrewsEvents();
        System.out.println("allCrewEvents.isEmpty(): " + allCrewEvents.isEmpty());

        return allCrewEvents;
    }
}

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
import com.ohgiraffers.climbon.common.forconvenienttest.RoleUpdateRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class EventController
{
    public int userCode =0;
    @Autowired
    private EventService eventService;


//    @GetMapping
//    public List<EventDTO> getEventsByType(@RequestParam("type") String type) {
//        return eventService.getEventsByType(type);
//    }

    @RequestMapping(value = "/events", method = RequestMethod.GET)
    public List<EventDTO> getPublicEvents(@AuthenticationPrincipal AuthDetail userDetails)
    {
        if(userDetails != null)
        {
            if(userDetails.getLoginUserDTO().getUserRole()== UserRole.ADMIN) {
                System.out.println("EventController.getPublicEvents.   ::   you are admin");
            }
        }
        System.out.println("main calendar will show you the schedules");
        return eventService.getMainEvents(true);
    }

    @GetMapping("/myCrew")
    public ResponseEntity<?> getCrewEvents(@RequestParam Integer crewCode, @AuthenticationPrincipal AuthDetail userDetails) throws Exception //RequestParam으로 뭔가 해결해보자
    {
        //크루 코드 어떻게 불러와

        System.out.println("Event Controller get Crew Events => 어케 불러 옴");
        int userCode = userDetails.getLoginUserDTO().getId();
        if(!eventService.isUserInCrew(new CrewEventDTO(userCode, crewCode))){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("해당 크루 멤버가 아님"); // 크루에 가입하세요? 정도의 메세지
        }
        List<EventDTO> crewEvents = eventService.getCrewEvents(crewCode);
        return ResponseEntity.ok(crewEvents);
    }

    // 마이페이지 캘린더
    @GetMapping("/mypage")
    public List<EventDTO> getPrivateEvents(@AuthenticationPrincipal AuthDetail userDetails)
    {
        if(userDetails == null || userDetails.getLoginUserDTO() == null)
        {
            System.out.println("No login user");
            return null;
        }

        userCode = userDetails.getLoginUserDTO().getId(); // user mypage에서 보여줄 내용
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

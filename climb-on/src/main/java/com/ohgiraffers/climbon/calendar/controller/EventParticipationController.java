package com.ohgiraffers.climbon.calendar.controller;

import com.ohgiraffers.climbon.calendar.dto.CrewEventDTO;
import com.ohgiraffers.climbon.calendar.service.EventParticipationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/participate")
public class EventParticipationController
{
    @Autowired
    private EventParticipationService eventParticipationService;

    @PostMapping("/crewevents")
    public String participateCrewEvents(@RequestBody CrewEventDTO crewEventDTO)
    {
        try{
            eventParticipationService.participateCrewEvents(crewEventDTO.getCrewCode(), crewEventDTO.getUserCode(), crewEventDTO.getEventCode());
            return "성공적으로 참여 되었습니다.";
        }catch (Exception e) {
            e.printStackTrace();
            return "참여에 실패했습니다.";
        }
    }
}

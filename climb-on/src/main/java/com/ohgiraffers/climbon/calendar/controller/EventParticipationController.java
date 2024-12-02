package com.ohgiraffers.climbon.calendar.controller;

import com.ohgiraffers.climbon.auth.model.AuthDetail;
import com.ohgiraffers.climbon.calendar.dto.CrewEventDTO;
import com.ohgiraffers.climbon.calendar.service.EventParticipationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticatedPrincipal;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("participate")
public class EventParticipationController
{
    @Autowired
    private EventParticipationService eventParticipationService;

    @PostMapping("/crewevents")
    public String participateCrewEvents(@RequestBody CrewEventDTO crewEventDTO, @AuthenticationPrincipal AuthDetail userDetails)
    {
        try{
            crewEventDTO.setUserCode(userDetails.getLoginUserDTO().getId());
            eventParticipationService.participateCrewEvents(userDetails.getLoginUserDTO().getId(), crewEventDTO.getCrewCode(), crewEventDTO.getEventCode());
            return "성공적으로 참여 되었습니다.";
        }catch (Exception e) {
            e.printStackTrace();
            return "참여에 실패했습니다.";
        }
    }

    @GetMapping("/checkDuplication")
    public String checkParticipateMember(@RequestBody CrewEventDTO crewEventDTO, @AuthenticationPrincipal AuthDetail userDetails)
    {
        int result = eventParticipationService.checkParticipateMember(userDetails.getLoginUserDTO().getId(), crewEventDTO.getCrewCode(), crewEventDTO.getEventCode());

        return result > 0 ? "이 이벤트에는 이미 참여했습니다!" : "";
    }

}

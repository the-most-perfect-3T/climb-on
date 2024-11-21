package com.ohgiraffers.climbon.crew.mycrew.controller;

import com.ohgiraffers.climbon.crew.mycrew.service.MyCrewService;
import com.ohgiraffers.climbon.crew.service.CrewBoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(("/crew/myCrew"))
public class MyCrewController
{
    @Autowired
    private MyCrewService myCrewService;

    @GetMapping
    public String getMyCrew(Model model)
    {
        try {
            // Fetch data from service and add to model
        } catch (Exception e) {
            e.printStackTrace(); // Log error
            return "error"; // Redirect to an error page if needed
        }

        return "crew/myCrew/myCrew";
    }
}

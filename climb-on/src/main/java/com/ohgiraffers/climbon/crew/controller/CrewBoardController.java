package com.ohgiraffers.climbon.crew.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/crew")
public class CrewBoardController {

    @GetMapping("/home")
    public String writePost() {
        return "crewBoardWritePost";
    }
}

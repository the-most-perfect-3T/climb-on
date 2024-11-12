package com.ohgiraffers.climbon.facilities.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

public class MapController {

    @GetMapping("/map/map")
    public ModelAndView map(ModelAndView mv) {
        mv.setViewName("map/map");

        return mv;
    }
}

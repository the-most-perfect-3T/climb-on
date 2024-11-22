package com.ohgiraffers.climbon.crew.mycrew.service;

import com.ohgiraffers.climbon.crew.mycrew.dao.MyCrewMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MyCrewService
{
    @Autowired
    private MyCrewMapper myCrewMapper;
}

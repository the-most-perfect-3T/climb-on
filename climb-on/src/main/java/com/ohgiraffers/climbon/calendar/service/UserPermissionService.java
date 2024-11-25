package com.ohgiraffers.climbon.calendar.service;

import com.ohgiraffers.climbon.calendar.dao.UserPermissionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserPermissionService
{
    @Autowired
    private UserPermissionMapper userPermissionMapper;

    public int getCrewCodeByUserCode(int userCode)
    {
        return userPermissionMapper.getCrewCodeByUserCode(userCode);
    }


}

package com.ohgiraffers.climbon.calendar.dao;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserPermissionMapper
{
    int getCrewCodeByUserCode(int userCode);
}

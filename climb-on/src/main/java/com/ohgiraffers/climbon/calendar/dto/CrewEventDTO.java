package com.ohgiraffers.climbon.calendar.dto;

public class CrewEventDTO
{
    private int userCode;
    private int crewCode;

    public CrewEventDTO()
    {
    }

    public CrewEventDTO(int userCode, int crewCode)
    {
        this.userCode = userCode;
        this.crewCode = crewCode;
    }

    public int getUserCode()
    {
        return userCode;
    }

    public void setUserCode(int userCode)
    {
        this.userCode = userCode;
    }

    public int getCrewCode()
    {
        return crewCode;
    }

    public void setCrewCode(int crewCode)
    {
        this.crewCode = crewCode;
    }
}

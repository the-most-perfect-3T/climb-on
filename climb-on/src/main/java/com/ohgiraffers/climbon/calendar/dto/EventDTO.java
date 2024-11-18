package com.ohgiraffers.climbon.calendar.dto;

import java.sql.Date;

public class EventDTO
{
    private int id;
    private String title;
    private int userCode;
    private String start;
    private String end;
    private String description;
    private String backgroundColor;

    public EventDTO()
    {
    }

    public EventDTO(int id, String title, int userCode, String start, String end, String description, String backgroundColor)
    {
        this.id = id;
        this.title = title;
        this.userCode = userCode;
        this.start = start;
        this.end = end;
        this.description = description;
        this.backgroundColor = backgroundColor;
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public int getUserCode()
    {
        return userCode;
    }

    public void setUserCode(int userCode)
    {
        this.userCode = userCode;
    }

    public String getStart()
    {
        return start;
    }

    public void setStart(String start)
    {
        this.start = start;
    }

    public String getEnd()
    {
        return end;
    }

    public void setEnd(String end)
    {
        this.end = end;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public String getBackgroundColor()
    {
        return backgroundColor;
    }

    public void setBackgroundColor(String backgroundColor)
    {
        this.backgroundColor = backgroundColor;
    }
}

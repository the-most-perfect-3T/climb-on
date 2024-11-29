package com.ohgiraffers.climbon.calendar.dto;

public class EventDTO
{
    private int id;
    private String title;
    private int userCode;
    private int crewCode;
    private boolean isAdmin;
    private String start;
    private String end;
    private String description;
    private String backgroundColor;
    private boolean isAllDay;

    public EventDTO()
    {
    }

    public EventDTO(int id, String title, int userCode, int crewCode, boolean isAdmin, String start, String end, String description, String backgroundColor, boolean isAllDay)
    {
        this.id = id;
        this.title = title;
        this.userCode = userCode;
        this.crewCode = crewCode;
        this.isAdmin = isAdmin;
        this.start = start;
        this.end = end;
        this.description = description;
        this.backgroundColor = backgroundColor;
        this.isAllDay = isAllDay;
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

    public int getCrewCode()
    {
        return crewCode;
    }

    public void setCrewCode(int crewCode)
    {
        this.crewCode = crewCode;
    }

    public boolean isAdmin()
    {
        return isAdmin;
    }

    public boolean setAdmin(boolean isAdmin)
    {
        this.isAdmin = isAdmin;
        return isAdmin;
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

    public boolean isAllDay()
    {
        return isAllDay;
    }

    public void setAllDay(boolean allDay)
    {
        isAllDay = allDay;
    }
}

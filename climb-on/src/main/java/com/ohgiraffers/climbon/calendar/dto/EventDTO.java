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
    private String location;
    private boolean isAllDay;
    private String crewName;
    private String crewImgUrl;
    private boolean isInProgress;

    public EventDTO()
    {
    }

    public EventDTO(int id, String title, int userCode, int crewCode, boolean isAdmin, String start, String end, String description, String backgroundColor, String location, boolean isAllDay, String crewName, String crewImgUrl, boolean isInProgress)
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
        this.location = location;
        this.isAllDay = isAllDay;
        this.crewName = crewName;
        this.crewImgUrl = crewImgUrl;
        this.isInProgress = isInProgress;
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

    public String getLocation()
    {
        return location;
    }

    public void setLocation(String location)
    {
        this.location = location;
    }

    public String getCrewName()
    {
        return crewName;
    }

    public void setCrewName(String crewName)
    {
        this.crewName = crewName;
    }

    public String getCrewImgUrl()
    {
        return crewImgUrl;
    }

    public void setCrewImgUrl(String crewImgUrl)
    {
        this.crewImgUrl = crewImgUrl;
    }

    public boolean isInProgress()
    {
        return isInProgress;
    }

    public void setInProgress(boolean inProgress)
    {
        isInProgress = inProgress;
    }
}

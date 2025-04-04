package com.ohgiraffers.climbon.common.forconvenienttest;

public class RoleUpdateRequest
{
    private int id;
    private String role;

    public RoleUpdateRequest()
    {
    }

    public RoleUpdateRequest(int id, String role)
    {
        this.id = id;
        this.role = role;
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getRole()
    {
        return role;
    }

    public void setRole(String role)
    {
        this.role = role;
    }

    @Override
    public String toString()
    {
        return "RoleUpdateRequest{" +
                "id=" + id +
                ", role='" + role + '\'' +
                '}';
    }
}

package com.ohgiraffers.climbon.common.forconvenienttest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TestUserRoleService
{
    @Autowired
    private TestUserRoleMapper testUserRoleMapper;

    public void updateUserRole(int id, String role)
    {
        testUserRoleMapper.updateUserRole(id, role);
    }
}

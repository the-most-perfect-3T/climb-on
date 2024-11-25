package com.ohgiraffers.climbon.common.forconvenienttest;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TestUserRoleMapper
{
    void updateUserRole(RoleUpdateRequest roleUpdateRequest);
}

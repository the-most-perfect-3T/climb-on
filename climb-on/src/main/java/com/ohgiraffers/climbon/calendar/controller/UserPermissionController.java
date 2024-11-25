package com.ohgiraffers.climbon.calendar.controller;

import com.ohgiraffers.climbon.auth.Enum.UserRole;
import com.ohgiraffers.climbon.auth.model.AuthDetail;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/user")
public class UserPermissionController
{
    @GetMapping("/permissions")
    public Map<String, Boolean> getUserPermissions(@AuthenticationPrincipal AuthDetail userDetails) {
        if (userDetails == null) {
            System.out.println("tlqkfdk");
            return Map.of("isAdmin", false);
        }
        boolean isAdmin = userDetails.getLoginUserDTO().getUserRole() == UserRole.ADMIN;
        return Map.of("isAdmin", isAdmin);
    }
}

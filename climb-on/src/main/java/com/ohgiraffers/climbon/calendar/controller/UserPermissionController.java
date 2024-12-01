package com.ohgiraffers.climbon.calendar.controller;

import com.ohgiraffers.climbon.auth.Enum.UserRole;
import com.ohgiraffers.climbon.auth.model.AuthDetail;
import com.ohgiraffers.climbon.calendar.service.UserPermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/user")
public class UserPermissionController
{
    @Autowired
    private UserPermissionService userPermissionService;

    @GetMapping("/permissions")
    public Map<String, Boolean> getUserPermissions(@AuthenticationPrincipal AuthDetail userDetails) {
        if (userDetails == null) {
            return Map.of("isAdmin", false);
        }
        boolean isAdmin = userDetails.getLoginUserDTO().getUserRole() == UserRole.ADMIN;
        return Map.of("isAdmin", isAdmin);
    }

    @GetMapping("/crewcode")
    public ResponseEntity<?> getCrewCode(@AuthenticationPrincipal AuthDetail userDetails)
    {
        if(userDetails == null){
            return ResponseEntity.ok(0);
        }
        int userCode = userDetails.getLoginUserDTO().getId();
        Integer crewCode = userPermissionService.getCrewCodeByUserCode(userCode);

        return ResponseEntity.ok(Map.of("crewCode", crewCode));
    }
}

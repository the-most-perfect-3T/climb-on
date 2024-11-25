package com.ohgiraffers.climbon.common.forconvenienttest;


import com.ohgiraffers.climbon.auth.model.AuthDetail;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Enumeration;

@RestController
public class TestUserRoleController
{
    @Autowired
    private TestUserRoleService testUserService;

    @PostMapping("/updateUserRole")
    public ResponseEntity<String> updateRole(@AuthenticationPrincipal AuthDetail userDetails, @RequestBody RoleUpdateRequest requestRole, HttpServletRequest request) {

        System.out.println("현재 세션 코드: " + request.getSession().getId());
        System.out.println("현재 유저 권한: " + request.getSession().getAttribute("userRole"));
        System.out.println("바꾸려는 유저 권한: " + requestRole.getRole());
        System.out.println("현재 유저 코드: " + userDetails.getLoginUserDTO().getId());

        try {
            testUserService.updateUserRole(new RoleUpdateRequest(userDetails.getLoginUserDTO().getId(), requestRole.getRole()));
            request.getSession().setAttribute("userRole", requestRole.getRole());
            return ResponseEntity.ok("현재 권한: " + requestRole.getRole());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating role" + e.getMessage());
        }
    }
}

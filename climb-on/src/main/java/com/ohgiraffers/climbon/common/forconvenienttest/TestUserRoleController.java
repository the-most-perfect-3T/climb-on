package com.ohgiraffers.climbon.common.forconvenienttest;


import com.ohgiraffers.climbon.auth.model.AuthDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestUserRoleController
{
    @Autowired
    private TestUserRoleService testUserService;

    @PostMapping("/updateRole")
    public ResponseEntity<String> updateRole(@AuthenticationPrincipal AuthDetail userDetails, @RequestBody RoleUpdateRequest request) {
        try {
            testUserService.updateUserRole(userDetails.getLoginUserDTO().getId(), request.getRole());
            return ResponseEntity.ok("Role updated successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating role");
        }
    }
}

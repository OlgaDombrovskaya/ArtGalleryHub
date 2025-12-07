package com.art_gallery_hub.controller;

import com.art_gallery_hub.dto.user.UserAdminCreationRequest;
import com.art_gallery_hub.dto.user.UserAdminSummaryResponse;

import com.art_gallery_hub.dto.user.UserRegistrationResponse;
import com.art_gallery_hub.dto.user.UserRoleUpdateRequest;
import com.art_gallery_hub.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(
        name = "System Administration (User & Role Management)",
        description = "High-level administrative operations for managing all " +
                "system users and assigning/changing roles (requires ADMIN role)")
@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final UserService userService;

    // GET /api/admin/users – a list of all users with roles
    @GetMapping("/users")
    public List<UserAdminSummaryResponse> findAllUsers() {
        return userService.getAllUsers();
    }

    // POST /api/admin/users – create a new user (for example, a curator)
    @PostMapping("/users")
    public ResponseEntity<UserRegistrationResponse> createNewUser(
            @RequestBody UserAdminCreationRequest request) {
        UserRegistrationResponse response = userService.createAdminUser(request);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // POST /api/admin/users/{id}/roles – change/assign roles
    @PostMapping("/users/{id}/roles")
    public ResponseEntity<UserAdminSummaryResponse> updateRole(
            @PathVariable Long id,
            @RequestBody UserRoleUpdateRequest request
    ) {
        UserAdminSummaryResponse response = userService.updateUserRoles(id, request);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}

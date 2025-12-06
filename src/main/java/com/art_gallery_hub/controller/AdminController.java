package com.art_gallery_hub.controller;

import com.art_gallery_hub.dto.user.UserAdminSummaryResponse;

import com.art_gallery_hub.repository.UserRepository;
import com.art_gallery_hub.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "Admin Operations", description = "System and user management. Available only for the ADMIN role")
@RestController
@RequestMapping("/api/admin")
public class AdminController {
    private final UserRepository userRepository;
    private final UserService userService;

    public AdminController(UserRepository userRepository, UserService userService) {
        this.userRepository = userRepository;
        this.userService = userService;
    }

    // GET /api/admin/users – a list of all users with roles
    @GetMapping("/users")
    public List<UserAdminSummaryResponse> findAllUsers() {
        return userService.getAllUsers();
    }

    // TODO
    // POST /api/admin/users – create a new user (for example, a curator).
    // POST /api/admin/users/{id}/roles – change/assign roles
}

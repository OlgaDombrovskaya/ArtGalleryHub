package com.art_gallery_hub.controller;

import com.art_gallery_hub.model.User;

import com.art_gallery_hub.repository.UserRepository;
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

    public AdminController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/user")
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }
}

package com.art_gallery_hub.controller;

import com.art_gallery_hub.dto.user.UserRegistrationRequest;
import com.art_gallery_hub.dto.user.UserRegistrationResponse;
import com.art_gallery_hub.enums.RoleStatus;
import com.art_gallery_hub.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(
        name = "Authentication and User Management",
        description = "Endpoints for user registration and authentication (Artist and Visitor)")
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    // POST /api/auth/register-artist – registration of a user with the ARTIST role
    @PostMapping("/register-artist")
    public ResponseEntity<UserRegistrationResponse> registerArtist(
            @RequestBody UserRegistrationRequest userRegistrationRequest) {
        UserRegistrationResponse response =
                userService.createUser(userRegistrationRequest, RoleStatus.ROLE_ARTIST);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // POST /api/auth/register-visitor – VISITOR registration
    @PostMapping("/register-visitor")
    public ResponseEntity<UserRegistrationResponse> registerVisitor(
            @RequestBody UserRegistrationRequest userRegistrationRequest) {
        UserRegistrationResponse response =
                userService.createUser(userRegistrationRequest, RoleStatus.ROLE_VISITOR);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}

package com.art_gallery_hub.controller;

import com.art_gallery_hub.dto.UserRegistrationRequest;
import com.art_gallery_hub.dto.UserRegistrationResponse;
import com.art_gallery_hub.enums.RoleStatus;
import com.art_gallery_hub.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Authentication", description = "Login, registration," +
        " and password recovery operations. Available without authentication")
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    // POST /api/auth/register-artist – registration of a user with the ARTIST role
    @PostMapping("/register-artist")
    public UserRegistrationResponse registerArtist(@RequestBody UserRegistrationRequest userRegistrationRequest) {
        return userService.createUser(userRegistrationRequest, RoleStatus.ROLE_ARTIST);
    }
    // POST /api/auth/register-visitor – VISITOR registration
    @PostMapping("/register-visitor")
    public UserRegistrationResponse registerVisitor(@RequestBody UserRegistrationRequest userRegistrationRequest) {
        return  userService.createUser(userRegistrationRequest, RoleStatus.ROLE_VISITOR);
    }
}

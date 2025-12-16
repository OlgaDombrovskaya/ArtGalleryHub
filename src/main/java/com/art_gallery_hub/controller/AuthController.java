package com.art_gallery_hub.controller;

import com.art_gallery_hub.dto.user.UserRegistrationRequest;
import com.art_gallery_hub.dto.user.UserRegistrationResponse;
import com.art_gallery_hub.enums.RoleStatus;
import com.art_gallery_hub.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class AuthController {

    private final UserService userService;

    // POST /api/auth/register-artist – registration of a user with the ARTIST role
    /**
     * POST /api/auth/register-artist
     * Registers a new user with the default role of ARTIST.
     *
     * @param userRegistrationRequest DTO containing username, email, and password
     * @return the registration response with user details
     */
    @Operation(
            summary = "Register a new Artist",
            description = "Registers a new user account and automatically assigns the ARTIST role.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "User successfully registered and created"),
                    @ApiResponse(responseCode = "400", description = "Bad Request (Validation failed)"),
                    @ApiResponse(responseCode = "404", description = "Role not found (Configuration error)"),
                    @ApiResponse(responseCode = "409", description = "Conflict (User with this username already exists)")
            }
    )
    @PostMapping("/register-artist")
    public ResponseEntity<UserRegistrationResponse> registerArtist(
            @Valid @RequestBody UserRegistrationRequest userRegistrationRequest) {
        UserRegistrationResponse response =
                userService.createUser(userRegistrationRequest, RoleStatus.ROLE_ARTIST);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // POST /api/auth/register-visitor – VISITOR registration
    /**
     * POST /api/auth/register-visitor
     * Registers a new user with the default role of VISITOR.
     *
     * @param userRegistrationRequest DTO containing username, email, and password
     * @return the registration response with user details
     */
    @Operation(
            summary = "Register a new Visitor",
            description = "Registers a new user account and automatically assigns the VISITOR role.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "User successfully registered and created"),
                    @ApiResponse(responseCode = "400", description = "Bad Request (Validation failed)"),
                    @ApiResponse(responseCode = "404", description = "Role not found (Configuration error)"),
                    @ApiResponse(responseCode = "409", description = "Conflict (User with this username already exists)")
            }
    )
    @PostMapping("/register-visitor")
    public ResponseEntity<UserRegistrationResponse> registerVisitor(
            @Valid @RequestBody UserRegistrationRequest userRegistrationRequest) {
        UserRegistrationResponse response =
                userService.createUser(userRegistrationRequest, RoleStatus.ROLE_VISITOR);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}

package com.art_gallery_hub.controller;

import com.art_gallery_hub.dto.user.UserAdminCreationRequest;
import com.art_gallery_hub.dto.user.UserAdminSummaryResponse;

import com.art_gallery_hub.dto.user.UserRegistrationResponse;
import com.art_gallery_hub.dto.user.UserRoleUpdateRequest;
import com.art_gallery_hub.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
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
    /**
     * GET /api/admin/users
     * Returns a list of all system users with their current roles.
     *
     * @return a list of user summary responses for the administrator
     */
    @Operation(
            summary = "Get all users in the system",
            description = "Retrieves a summary list of all users and their assigned roles. Requires ADMIN role.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successful return of the list"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized"),
                    @ApiResponse(responseCode = "403", description = "Forbidden (Missing ADMIN role)")
            }
    )
    @GetMapping("/users")
    public List<UserAdminSummaryResponse> findAllUsers() {
        return userService.getAllUsers();
    }

    // POST /api/admin/users – create a new user (for example, a curator)
    /**
     * POST /api/admin/users
     * Creates a new user (e.g., another Curator or Admin) with the specified role.
     *
     * @param request data for creating the new user
     * @return a brief summary of the created user
     */
    @Operation(
            summary = "Create a new user/account",
            description = "Allows the Admin to create a new user account with specified roles (e.g., Curator).",
            responses = {
                    @ApiResponse(responseCode = "201", description = "User successfully created"),
                    @ApiResponse(responseCode = "400", description = "Bad Request (Validation failed or user already exists)"),
                    @ApiResponse(responseCode = "403", description = "Forbidden (Missing ADMIN role)"),
                    @ApiResponse(responseCode = "409", description = "Conflict (User with this username already exists)")
            }
    )
    @PostMapping("/users")
    public ResponseEntity<UserRegistrationResponse> createNewUser(
            @Valid @RequestBody UserAdminCreationRequest request) {
        UserRegistrationResponse response = userService.createAdminUser(request);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // POST /api/admin/users/{id}/roles – change/assign roles
    /**
     * POST /api/admin/users/{id}/roles
     * Обновляет или назначает роли для существующего пользователя по его ID.
     *
     * @param id ID пользователя, чьи роли нужно изменить
     * @param request DTO с новыми ролями
     * @return обновленная краткая сводка пользователя
     */
    @Operation(
            summary = "Update or assign user roles",
            description = "Changes the roles assigned to a specific user by ID. Requires ADMIN role.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Roles successfully updated"),
                    @ApiResponse(responseCode = "400", description = "Bad Request (Validation failed or invalid role)"),
                    @ApiResponse(responseCode = "403", description = "Forbidden (Missing ADMIN role)"),
                    @ApiResponse(responseCode = "404", description = "User not found")
            }
    )
    @PostMapping("/users/{id}/roles")
    public ResponseEntity<UserAdminSummaryResponse> updateRole(
            @PathVariable Long id,
            @Valid @RequestBody UserRoleUpdateRequest request
    ) {
        UserAdminSummaryResponse response = userService.updateUserRoles(id, request);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}

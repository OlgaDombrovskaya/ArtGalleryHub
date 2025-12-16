package com.art_gallery_hub.dto.user;

import jakarta.validation.constraints.NotEmpty;

import java.util.Set;

public record UserRoleUpdateRequest(
        @NotEmpty(message = "Role list cannot be empty. At least one role must be assigned.")
        Set<String> roleNames
) {
}

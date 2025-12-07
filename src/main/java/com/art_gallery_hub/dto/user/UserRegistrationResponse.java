package com.art_gallery_hub.dto.user;

public record UserRegistrationResponse(
        Long id,
        String username,
        String email
) {
}

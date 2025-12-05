package com.art_gallery_hub.dto;

public record UserRegistrationResponse(
        Long id,
        String username,
        String email
) {
}

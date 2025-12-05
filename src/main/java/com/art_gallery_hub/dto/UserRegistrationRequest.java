package com.art_gallery_hub.dto;

public record UserRegistrationRequest(
        String username,
        String email,
        String password
) {
}

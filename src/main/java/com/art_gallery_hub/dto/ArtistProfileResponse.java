package com.art_gallery_hub.dto;

public record ArtistProfileResponse(
        Long userId,
        String displayName,
        String bio,
        String website
) {
}

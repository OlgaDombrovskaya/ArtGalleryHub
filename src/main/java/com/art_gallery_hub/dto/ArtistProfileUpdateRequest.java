package com.art_gallery_hub.dto;

public record ArtistProfileUpdateRequest(
        String displayName,
        String bio,
        String website
) {
}

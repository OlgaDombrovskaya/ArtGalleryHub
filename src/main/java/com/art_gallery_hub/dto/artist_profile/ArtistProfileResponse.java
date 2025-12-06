package com.art_gallery_hub.dto.artist_profile;

public record ArtistProfileResponse(
        Long userId,
        String displayName,
        String bio,
        String website
) {
}

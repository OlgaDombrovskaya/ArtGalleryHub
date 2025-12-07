package com.art_gallery_hub.dto.artist_profile;

public record ArtistProfileUpdateRequest(
        String displayName,
        String bio,
        String website
) {
}

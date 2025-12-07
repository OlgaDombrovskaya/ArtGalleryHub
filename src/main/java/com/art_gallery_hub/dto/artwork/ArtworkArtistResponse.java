package com.art_gallery_hub.dto.artwork;

public record ArtworkArtistResponse(
        ArtworkPublicSummaryResponse summary,
        String description,
        boolean isPublic
) {
}

package com.art_gallery_hub.dto.artwork;

public record ArtworkPrivateResponse(
        ArtworkPublicSummaryResponse summary,
        String description,
        boolean isPublic
) {
}

package com.art_gallery_hub.dto.artwork;

import com.art_gallery_hub.enums.Style;

public record ArtworkPublicSummaryResponse(
        Long id,
        String artistDisplayName,
        String title,
        int year,
        Style style,
        String imagePath
) {
}
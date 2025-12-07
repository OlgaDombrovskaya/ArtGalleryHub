package com.art_gallery_hub.dto.artwork;

import com.art_gallery_hub.enums.Style;

public record ArtworkUpdateRequest(
        String title,
        String description,
        int year,
        Style style,
        boolean isPublic
) {
}

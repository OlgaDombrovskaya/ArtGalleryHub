package com.art_gallery_hub.dto;

import java.util.List;

public record ArtworkPublicDetailsResponse(
        ArtworkPublicSummaryResponse summary,
        String description,
        List<ReviewResponse> reviews
) {
}

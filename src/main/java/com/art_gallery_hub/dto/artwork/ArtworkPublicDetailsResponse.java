package com.art_gallery_hub.dto.artwork;

import com.art_gallery_hub.dto.review.ReviewResponse;

import java.util.List;

public record ArtworkPublicDetailsResponse(
        ArtworkPublicSummaryResponse summary,
        String description,
        List<ReviewResponse> reviews
) {
}

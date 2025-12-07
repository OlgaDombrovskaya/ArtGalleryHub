package com.art_gallery_hub.dto.review;

import java.time.LocalDateTime;

public record ReviewResponse(
        Long id,
        String authorUsername,
        int rating,
        String comment,
        LocalDateTime createdAt
) {
}

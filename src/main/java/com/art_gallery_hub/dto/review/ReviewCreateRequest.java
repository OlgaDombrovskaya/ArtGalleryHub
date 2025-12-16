package com.art_gallery_hub.dto.review;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ReviewCreateRequest(
        @NotNull(message = "Rating must be provided.")
        @Min(value = 1, message = "Rating cannot be less than 1.")
        @Max(value = 5, message = "Rating cannot be greater than 5.")
        int rating,

        @Size(max = 255, message = "Comment cannot exceed 255 characters.")
        String comment
) {
}

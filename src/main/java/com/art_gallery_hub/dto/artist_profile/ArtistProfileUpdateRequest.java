package com.art_gallery_hub.dto.artist_profile;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.URL;

public record ArtistProfileUpdateRequest(
        @NotBlank(message = "Display name is required and cannot be empty.")
        @Size(max = 50, message = "Display name cannot exceed 50 characters.")
        String displayName,

        @NotBlank(message = "Biography (bio) is required and cannot be empty.")
        @Size(max = 2000, message = "Bio cannot exceed 2000 characters.")
        String bio,

        @URL(message = "Website must be a valid URL format.", regexp = "^(http|https)://.*")
        String website
) {
}

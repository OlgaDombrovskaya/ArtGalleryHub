package com.art_gallery_hub.dto.artwork;

import com.art_gallery_hub.enums.Style;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.Year;

public record ArtworkCreateRequest(
        @NotBlank(message = "Title is required and cannot be empty")
        @Size(max = 255, message = "Title cannot exceed 255 characters")
        String title,

        @NotBlank(message = "Description is required and cannot be empty")
        @Size(max = 2000, message = "Description cannot exceed 2000 characters")
        String description,

        @Min(value = 1000, message = "Year must be a valid four-digit year (e.g., 1985)")
        @Max(value = 9999, message = "Year must be less than 9999")
        int year,

        @NotNull(message = "Style is required")
        Style style
) {
    private static final int CURRENT_YEAR = Year.now().getValue();

    public ArtworkCreateRequest(String title, String description, int year, Style style) {
        this.title = title;
        this.description = description;
        this.year = year;
        this.style = style;

        if (year > CURRENT_YEAR) {
            throw new IllegalArgumentException("Year cannot be in the future (Current year: " + CURRENT_YEAR + ")");
        }
    }
}

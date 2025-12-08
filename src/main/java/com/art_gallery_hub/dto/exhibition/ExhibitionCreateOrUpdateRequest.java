package com.art_gallery_hub.dto.exhibition;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record ExhibitionCreateOrUpdateRequest(
        @NotBlank(message = "Title is required")
        @Size(max = 255)
        String title,

        @NotNull(message = "Description is required")
        String description,

        @NotNull(message = "Start date is required")
        LocalDate startDate,

        @NotNull(message = "End date is required")
        LocalDate endDate
) {
}

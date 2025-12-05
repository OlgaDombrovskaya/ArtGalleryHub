package com.art_gallery_hub.dto;

import java.time.LocalDate;

public record ExhibitionSummaryResponse(
        Long id,
        String title,
        LocalDate startDate,
        LocalDate endDate,
        String curatorDisplayName
) {
}

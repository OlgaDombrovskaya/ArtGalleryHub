package com.art_gallery_hub.dto.exhibition;

import java.time.LocalDate;

public record ExhibitionPublicSummaryResponse(
        Long id,
        String title,
        LocalDate startDate,
        LocalDate endDate,
        String curatorDisplayName
) {
}

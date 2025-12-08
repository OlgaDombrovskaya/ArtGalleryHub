package com.art_gallery_hub.dto.exhibition;

import com.art_gallery_hub.enums.ExhibitionStatus;

import java.time.LocalDate;

public record ExhibitionCuratorSummaryResponse(
        Long id,
        String title,
        LocalDate startDate,
        LocalDate endDate,
        String curatorDisplayName,
        ExhibitionStatus status
) {
}

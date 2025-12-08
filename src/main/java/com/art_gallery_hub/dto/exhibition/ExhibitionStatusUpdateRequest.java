package com.art_gallery_hub.dto.exhibition;

import com.art_gallery_hub.enums.ExhibitionStatus;
import jakarta.validation.constraints.NotNull;

public record ExhibitionStatusUpdateRequest(
        @NotNull(message = "New status is required")
        ExhibitionStatus newStatus
) {
}

package com.art_gallery_hub.dto.invitation;

import com.art_gallery_hub.dto.exhibition.ExhibitionSummaryResponse;
import com.art_gallery_hub.enums.InvitationStatus;

public record InvitationResponse(
        Long id,
        InvitationStatus status,
        ExhibitionSummaryResponse exhibition
) {
}

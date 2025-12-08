package com.art_gallery_hub.dto.invitation;

import com.art_gallery_hub.dto.exhibition.ExhibitionPublicSummaryResponse;
import com.art_gallery_hub.enums.InvitationStatus;

public record InvitationArtistResponse(
        Long id,
        InvitationStatus status,
        ExhibitionPublicSummaryResponse exhibition
) {
}

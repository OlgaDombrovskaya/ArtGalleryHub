package com.art_gallery_hub.dto.invitation;

import com.art_gallery_hub.enums.InvitationStatus;

public record InvitationCuratorResponse(
        Long id,
        Long exhibitionId,
        Long artistId,
        String artistDisplayName,
        InvitationStatus invitationStatus
) {
}

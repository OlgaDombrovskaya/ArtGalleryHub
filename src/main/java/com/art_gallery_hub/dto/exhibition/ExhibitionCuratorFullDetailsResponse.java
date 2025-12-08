package com.art_gallery_hub.dto.exhibition;

import com.art_gallery_hub.dto.artwork.ArtworkPublicSummaryResponse;
import com.art_gallery_hub.dto.invitation.InvitationCuratorResponse;

import java.util.List;

public record ExhibitionCuratorFullDetailsResponse(
        ExhibitionCuratorSummaryResponse summary,
        String description,
        List<ArtworkPublicSummaryResponse> artworks,
        List<InvitationCuratorResponse> invitations
) {
}

package com.art_gallery_hub.mapper;

import com.art_gallery_hub.dto.exhibition.ExhibitionPublicSummaryResponse;
import com.art_gallery_hub.dto.invitation.InvitationArtistResponse;
import com.art_gallery_hub.dto.invitation.InvitationCuratorResponse;
import com.art_gallery_hub.model.Invitation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class InvitationMapper {

    private final ExhibitionMapper exhibitionMapper;

    public InvitationArtistResponse toInvitationArtistResponse(Invitation invitation) {
        ExhibitionPublicSummaryResponse exhibitionSummary =
                exhibitionMapper.toExhibitionPublicSummaryResponse(invitation.getExhibition());

        return new InvitationArtistResponse(
                invitation.getId(),
                invitation.getStatus(),
                exhibitionSummary
        );
    }

    public InvitationCuratorResponse toInvitationCuratorResponse(Invitation invitation) {
        return new InvitationCuratorResponse(
                invitation.getId(),
                invitation.getExhibition().getId(),
                invitation.getArtist().getId(),
                invitation.getArtist().getDisplayName(),
                invitation.getStatus()
        );
    }
}

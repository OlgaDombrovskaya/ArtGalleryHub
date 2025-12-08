package com.art_gallery_hub.mapper;

import com.art_gallery_hub.dto.exhibition.ExhibitionPublicSummaryResponse;
import com.art_gallery_hub.dto.invitation.InvitationArtistResponse;
import com.art_gallery_hub.model.Invitation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class InvitationMapper {

    private final ExhibitionMapper exhibitionMapper;

    public InvitationArtistResponse toInvitationResponse(Invitation invitation) {
        ExhibitionPublicSummaryResponse exhibitionSummary =
                exhibitionMapper.toExhibitionSummaryResponse(invitation.getExhibition());

        return new InvitationArtistResponse(
                invitation.getId(),
                invitation.getStatus(),
                exhibitionSummary
        );
    }
}

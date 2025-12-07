package com.art_gallery_hub.mapper;

import com.art_gallery_hub.dto.exhibition.ExhibitionSummaryResponse;
import com.art_gallery_hub.dto.invitation.InvitationResponse;
import com.art_gallery_hub.model.Invitation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class InvitationMapper {

    private final ExhibitionMapper exhibitionMapper;

    public InvitationResponse toInvitationResponse(Invitation invitation) {
        ExhibitionSummaryResponse exhibitionSummary =
                exhibitionMapper.toExhibitionSummaryResponse(invitation.getExhibition());

        return new InvitationResponse(
                invitation.getId(),
                invitation.getStatus(),
                exhibitionSummary
        );
    }
}

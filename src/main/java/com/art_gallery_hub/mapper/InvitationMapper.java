package com.art_gallery_hub.mapper;

import com.art_gallery_hub.dto.exhibition.ExhibitionSummaryResponse;
import com.art_gallery_hub.dto.invitation.InvitationResponse;
import com.art_gallery_hub.model.Exhibition;
import com.art_gallery_hub.model.Invitation;
import org.springframework.stereotype.Component;

@Component
public class InvitationMapper {

    public InvitationResponse toInvitationResponse(Invitation invitation) {
        Exhibition exhibition = invitation.getExhibition();

        return new InvitationResponse(
                invitation.getId(),
                invitation.getStatus(),
                new ExhibitionSummaryResponse(
                        exhibition.getId(),
                        exhibition.getTitle(),
                        exhibition.getStartDate(),
                        exhibition.getEndDate(),
                        exhibition.getCurator().getUsername()
                )
        );
    }
}

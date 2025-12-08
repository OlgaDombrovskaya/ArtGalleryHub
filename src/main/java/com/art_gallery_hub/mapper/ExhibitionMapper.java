package com.art_gallery_hub.mapper;

import com.art_gallery_hub.dto.artwork.ArtworkPublicSummaryResponse;
import com.art_gallery_hub.dto.exhibition.ExhibitionCreateOrUpdateRequest;
import com.art_gallery_hub.dto.exhibition.ExhibitionCuratorFullDetailsResponse;
import com.art_gallery_hub.dto.exhibition.ExhibitionCuratorSummaryResponse;
import com.art_gallery_hub.dto.exhibition.ExhibitionPublicSummaryResponse;
import com.art_gallery_hub.dto.invitation.InvitationCuratorResponse;
import com.art_gallery_hub.enums.ExhibitionStatus;
import com.art_gallery_hub.model.Exhibition;
import com.art_gallery_hub.model.User;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ExhibitionMapper {

    public Exhibition toEntity(
            ExhibitionCreateOrUpdateRequest request,
            User curator,
            ExhibitionStatus status
    ) {
        return new Exhibition(
                request.title(),
                request.description(),
                request.startDate(),
                request.endDate(),
                curator,
                status
        );
    }

    public ExhibitionPublicSummaryResponse toExhibitionPublicSummaryResponse(Exhibition exhibition) {
        return new ExhibitionPublicSummaryResponse(
                exhibition.getId(),
                exhibition.getTitle(),
                exhibition.getStartDate(),
                exhibition.getEndDate(),
                exhibition.getCurator().getUsername()
        );
    }

    public ExhibitionCuratorSummaryResponse toExhibitionCuratorSummaryResponse(Exhibition exhibition) {
        return new ExhibitionCuratorSummaryResponse(
                exhibition.getId(),
                exhibition.getTitle(),
                exhibition.getStartDate(),
                exhibition.getEndDate(),
                exhibition.getCurator().getUsername(),
                exhibition.getStatus()
        );
    }

    public ExhibitionCuratorFullDetailsResponse toExhibitionFullDetailsResponse(
            Exhibition exhibition,
            List<ArtworkPublicSummaryResponse> artworks,
            List<InvitationCuratorResponse> invitations
    ) {
        ExhibitionCuratorSummaryResponse summary = toExhibitionCuratorSummaryResponse(exhibition);
        return new ExhibitionCuratorFullDetailsResponse(
                summary,
                exhibition.getDescription(),
                artworks,
                invitations
        );
    }
}

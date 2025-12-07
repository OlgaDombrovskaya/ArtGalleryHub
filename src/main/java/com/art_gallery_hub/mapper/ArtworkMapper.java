package com.art_gallery_hub.mapper;

import com.art_gallery_hub.dto.artwork.ArtworkArtistResponse;
import com.art_gallery_hub.dto.artwork.ArtworkCreateRequest;
import com.art_gallery_hub.dto.artwork.ArtworkPublicDetailsResponse;
import com.art_gallery_hub.dto.artwork.ArtworkPublicSummaryResponse;
import com.art_gallery_hub.dto.review.ReviewResponse;
import com.art_gallery_hub.model.ArtistProfile;
import com.art_gallery_hub.model.Artwork;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ArtworkMapper {

    public Artwork toEntity(
            ArtistProfile artistProfile,
            ArtworkCreateRequest request,
            String imagePath
    ) {
        return new Artwork(
            artistProfile,
            request.title(),
            request.description(),
            request.year(),
            request.style(),
            imagePath
        );
    }

    public ArtworkPublicSummaryResponse toArtworkPublicSummaryResponse(Artwork artwork) {
        return new ArtworkPublicSummaryResponse(
                artwork.getId(),
                artwork.getArtist().getDisplayName(),
                artwork.getTitle(),
                artwork.getYear(),
                artwork.getStyle(),
                artwork.getImagePath()
        );
    }

    public ArtworkPublicDetailsResponse toArtworkPublicDetailsResponse(
            Artwork artwork,
            List<ReviewResponse> reviewResponses) {
        ArtworkPublicSummaryResponse summary = toArtworkPublicSummaryResponse(artwork);

        return new ArtworkPublicDetailsResponse(
                summary,
                artwork.getDescription(),
                reviewResponses
        );
    }

    public ArtworkArtistResponse toArtworkArtistResponse(Artwork artwork) {
        ArtworkPublicSummaryResponse summary = toArtworkPublicSummaryResponse(artwork);

        return new ArtworkArtistResponse(
                summary,
                artwork.getDescription(),
                artwork.isPublic()
        );
    }
}

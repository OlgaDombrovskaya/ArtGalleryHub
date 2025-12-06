package com.art_gallery_hub.mapper;

import com.art_gallery_hub.dto.artwork.ArtworkCreateRequest;
import com.art_gallery_hub.dto.artwork.ArtworkPrivateResponse;
import com.art_gallery_hub.dto.artwork.ArtworkPublicDetailsResponse;
import com.art_gallery_hub.dto.artwork.ArtworkPublicSummaryResponse;
import com.art_gallery_hub.dto.review.ReviewResponse;
import com.art_gallery_hub.model.Artwork;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ArtworkMapper {

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
        return new ArtworkPublicDetailsResponse(
                new ArtworkPublicSummaryResponse(
                        artwork.getId(),
                        artwork.getArtist().getDisplayName(),
                        artwork.getTitle(),
                        artwork.getYear(),
                        artwork.getStyle(),
                        artwork.getImagePath()
                ),
                artwork.getDescription(),
                reviewResponses
        );
    }

    public ArtworkCreateRequest toArtworkCreateRequest(Artwork artwork) {
        return new ArtworkCreateRequest(
                artwork.getTitle(),
                artwork.getDescription(),
                artwork.getYear(),
                artwork.getStyle()
        );
    }

    public ArtworkPrivateResponse toArtworkPrivateResponse(Artwork artwork) {
        return new ArtworkPrivateResponse(
                new ArtworkPublicSummaryResponse(
                        artwork.getId(),
                        artwork.getArtist().getDisplayName(),
                        artwork.getTitle(),
                        artwork.getYear(),
                        artwork.getStyle(),
                        artwork.getImagePath()
                ),
                artwork.getDescription(),
                artwork.isPublic()
        );
    }
}

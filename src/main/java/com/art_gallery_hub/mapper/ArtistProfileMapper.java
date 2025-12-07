package com.art_gallery_hub.mapper;

import com.art_gallery_hub.dto.artist_profile.ArtistProfileResponse;
import com.art_gallery_hub.model.ArtistProfile;
import org.springframework.stereotype.Component;

@Component
public class ArtistProfileMapper {

    public ArtistProfileResponse toArtistProfileResponse(ArtistProfile artistProfile) {
        return new ArtistProfileResponse(
                artistProfile.getUser().getId(),
                artistProfile.getDisplayName(),
                artistProfile.getBio(),
                artistProfile.getWebsite()
        );
    }
}

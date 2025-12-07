package com.art_gallery_hub.security.access;

import com.art_gallery_hub.repository.ArtworkRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component("artworkAccessChecker")
@RequiredArgsConstructor
public class ArtworkAccessChecker {

    private final ArtworkRepository artworkRepository;

    public boolean isOwner(Long artworkId, Authentication authentication) {
        String currentUsername = authentication.getName();

        return artworkRepository.findById(artworkId)
                .map(artwork -> artwork.getArtist().getUser().getUsername().equals(currentUsername))
                .orElse(false);
    }

}

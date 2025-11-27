package com.art_gallery_hub.service;

import com.art_gallery_hub.model.Artwork;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

@Service
public class ArtistService {
    @PreAuthorize("#artwork.artist.user.username == authentication.name or hasRole('ADMIN')")
    public Artwork updateArtwork(Artwork artwork) {
        return artwork;
    }

}

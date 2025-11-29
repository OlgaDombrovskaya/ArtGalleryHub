package com.art_gallery_hub.controller;

import com.art_gallery_hub.model.Artwork;
import com.art_gallery_hub.repository.ArtistProfileRepository;
import com.art_gallery_hub.repository.ArtworkRepository;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "Artist Operations", description = "Artist profile and artworks management. Available only for the ARTIST role")
@RestController
@RequestMapping("/api/artist")
public class ArtistController {

    private final ArtistProfileRepository artistProfileRepository;
    private final ArtworkRepository artworkRepository;

    public ArtistController(ArtistProfileRepository artistProfileRepository, ArtworkRepository artworkRepository) {
        this.artistProfileRepository = artistProfileRepository;
        this.artworkRepository = artworkRepository;
    }

    @GetMapping("/artworks/my")
    public List<Artwork> getMyArtworks(@AuthenticationPrincipal UserDetails userDetails) {
        String username = userDetails.getUsername();
        return List.of();
    }
}

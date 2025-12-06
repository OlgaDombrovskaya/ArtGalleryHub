package com.art_gallery_hub.controller;

import com.art_gallery_hub.model.ArtistProfile;
import com.art_gallery_hub.model.Artwork;
import com.art_gallery_hub.model.User;
import com.art_gallery_hub.repository.ArtistProfileRepository;
import com.art_gallery_hub.repository.ArtworkRepository;
import com.art_gallery_hub.repository.UserRepository;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@Tag(name = "Artist Operations", description = "Artist profile and artworks management. Available only for the ARTIST role")
@RestController
@RequestMapping("/api/artist")
public class ArtistController {

    private final ArtistProfileRepository artistProfileRepository;
    private final ArtworkRepository artworkRepository;
    private final UserRepository userRepository;

    public ArtistController(ArtistProfileRepository artistProfileRepository, ArtworkRepository artworkRepository, UserRepository userRepository) {
        this.artistProfileRepository = artistProfileRepository;
        this.artworkRepository = artworkRepository;
        this.userRepository = userRepository;
    }

    @GetMapping("/artworks/my")
    public List<Artwork> getMyArtworks(@AuthenticationPrincipal UserDetails userDetails) {
        String username = userDetails.getUsername();
        log.info("ArtistController: fetching artworks for artist username='{}'", username);

        // 1. получаем USER
        User user = userRepository
                .findByUsername(username)
                .orElseThrow(() -> {
            log.warn("ArtistController: user '{}' not found in DB", username);
            return new RuntimeException("User not found");
        });

        // 2. получаем профиль художника
        ArtistProfile artistProfile = artistProfileRepository
                .findByUser(user)
                .orElseThrow(()-> {
            log.warn("ArtistController: profile for user '{}' not found", username);
            return new RuntimeException("Artist profile not found");
        });

        // 3. получаем работы
        return artworkRepository.findByArtistId(artistProfile.getId());
    }
}

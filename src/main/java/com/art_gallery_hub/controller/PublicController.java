package com.art_gallery_hub.controller;


import com.art_gallery_hub.model.Artwork;
import com.art_gallery_hub.repository.ArtworkRepository;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "Public Access / Authentication", description = "Login, registration," +
        " and password recovery operations. Available without authentication")
@RestController
@RequestMapping("/api/public")
public class PublicController {

    private final ArtworkRepository artworkRepository;

    public PublicController(ArtworkRepository artworkRepository) {
        this.artworkRepository = artworkRepository;
    }

    @GetMapping("/info")
    public String publicInfo() {
        return "Welcome to Art Gallery Hub - digital art gallery and exhibitions platform";
    }

    @GetMapping("/artworks")
    public List<Artwork> getPublicArtworks() {
        return artworkRepository.findByIsPublicTrue();
    }
}

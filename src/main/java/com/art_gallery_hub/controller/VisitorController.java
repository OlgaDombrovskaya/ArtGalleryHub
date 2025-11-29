package com.art_gallery_hub.controller;

import com.art_gallery_hub.repository.ArtworkRepository;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Visitor Operations", description = "Viewing artworks and posting reviews. Available only for the VISITOR role")
@RestController
@RequestMapping("/api/visitor")
public class VisitorController {

    private final ArtworkRepository artworkRepository;

    public VisitorController(ArtworkRepository artworkRepository) {
        this.artworkRepository = artworkRepository;
    }
}

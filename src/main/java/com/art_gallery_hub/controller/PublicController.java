package com.art_gallery_hub.controller;

import com.art_gallery_hub.dto.ArtworkPublicDetailsResponse;
import com.art_gallery_hub.dto.ArtworkPublicSummaryResponse;
import com.art_gallery_hub.dto.ExhibitionSummaryResponse;
import com.art_gallery_hub.service.ArtworkService;
import com.art_gallery_hub.service.ExhibitionService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(
        name = "Public Artworks and Viewing",
        description = "Endpoints for unauthenticated access to general " +
                "gallery information, public artworks, and exhibitions")
@RestController
@RequestMapping("/api/public")
@RequiredArgsConstructor
public class PublicController {

    private final ArtworkService artworkService;
    private final ExhibitionService exhibitionService;

    // GET /api/public/info – short information about the gallery
    @GetMapping("/info")
    public String publicInfo() {
        return "Welcome to Art Gallery Hub - digital art gallery and exhibitions platform";
    }

    // GET /api/public/artworks – list of public works (IsPublic = true)
    @GetMapping("/artworks")
    public List<ArtworkPublicSummaryResponse> getPublicArtworks() {
        return artworkService.getAllArtworks();
    }

    // GET /api/public/artworks/{id} - short information about the work (without hidden data)
    @GetMapping("/artworks/{id}")
    public ArtworkPublicDetailsResponse getArtworkById(@PathVariable Long id) {
        return artworkService.getArtworkDetails(id);
    }

    // GET /api/public/exhibitions – current and planned exhibitions (status = OPEN or PLANNED)
    @GetMapping("/exhibitions")
    public List<ExhibitionSummaryResponse> getOpenOrPlannedExhibitions() {
        return exhibitionService.getOpenOrPlannedExhibitions();
    }
}

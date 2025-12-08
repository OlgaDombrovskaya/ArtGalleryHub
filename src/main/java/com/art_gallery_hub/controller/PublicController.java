package com.art_gallery_hub.controller;

import com.art_gallery_hub.dto.artwork.ArtworkPublicDetailsResponse;
import com.art_gallery_hub.dto.artwork.ArtworkPublicSummaryResponse;
import com.art_gallery_hub.dto.exhibition.ExhibitionPublicSummaryResponse;
import com.art_gallery_hub.service.ArtworkService;
import com.art_gallery_hub.service.ExhibitionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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
    /**
     * GET /api/public/info
     * Provides a short welcome message about the platform.
     *
     * @return a welcoming string
     */
    @Operation(
            summary = "Get Gallery Welcome Message",
            description = "Returns a simple string providing information about the gallery platform.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successful return of information")
            }
    )
    @GetMapping("/info")
    public String publicInfo() {
        return "Welcome to Art Gallery Hub - digital art gallery and exhibitions platform";
    }

    // GET /api/public/artworks – list of public works (IsPublic = true)
    /**
     * GET /api/public/artworks
     * Retrieves a summary list of all artworks marked as public (isPublic = true).
     *
     * @return a list of public artwork summaries
     */
    @Operation(
            summary = "Get all public artworks",
            description = "Retrieves a summary list of all artworks explicitly marked as public.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successful return of the list")
            }
    )
    @GetMapping("/artworks")
    public List<ArtworkPublicSummaryResponse> getPublicArtworks() {
        return artworkService.getAllPublicArtworks();
    }

    // GET /api/public/artworks/{id} - short information about the work (without hidden data)
    /**
     * GET /api/public/artworks/{id}
     * Retrieves detailed information about a specific public artwork, including its reviews.
     *
     * @param id ID of the artwork to retrieve
     * @return full public details of the artwork and its reviews
     */
    @Operation(
            summary = "Get public details of a specific artwork",
            description = "Retrieves full public metadata and associated reviews for an artwork by ID.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successful retrieval of artwork details"),
                    @ApiResponse(responseCode = "404", description = "Artwork not found with the specified ID")
            }
    )
    @GetMapping("/artworks/{id}")
    public ArtworkPublicDetailsResponse getArtworkById(@PathVariable Long id) {
        return artworkService.getArtworkPublicDetails(id);
    }

    // GET /api/public/exhibitions – current and planned exhibitions (status = OPEN or PLANNED)
    /**
     * GET /api/public/exhibitions
     * Retrieves a summary list of all exhibitions that are currently OPEN or PLANNED.
     *
     * @return a list of public exhibition summaries
     */
    @Operation(
            summary = "Get open and planned exhibitions",
            description = "Retrieves a list of exhibitions with status OPEN or PLANNED.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successful return of the list")
            }
    )
    @GetMapping("/exhibitions")
    public List<ExhibitionPublicSummaryResponse> getOpenOrPlannedExhibitions() {
        return exhibitionService.getOpenOrPlannedExhibitions();
    }
}

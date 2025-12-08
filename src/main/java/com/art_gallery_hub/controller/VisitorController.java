package com.art_gallery_hub.controller;

import com.art_gallery_hub.dto.artwork.ArtworkPublicSummaryResponse;
import com.art_gallery_hub.dto.review.ReviewCreateRequest;
import com.art_gallery_hub.dto.review.ReviewResponse;
import com.art_gallery_hub.service.ArtworkService;
import com.art_gallery_hub.service.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@Tag(
        name = "Visitor Interactions and Content Viewing",
        description = "Endpoints for authorized Visitors to browse, " +
                "filter artworks, and leave reviews/ratings")
@RestController
@RequestMapping("/api/visitor")
@RequiredArgsConstructor
public class VisitorController {

    private final ArtworkService artworkService;
    private final ReviewService reviewService;

    // GET /api/visitor/artworks – a list of public works with the ability to filter by style, year, and artist
    /**
     * GET /api/visitor/artworks
     * Retrieves a list of public artworks with optional filtering capabilities.
     *
     * @param style Optional filter for artwork style (based on enum)
     * @param year Optional filter for the artwork's year of creation
     * @param artist Optional filter by artist's name (partial match)
     * @return A list of filtered public artwork summaries
     */
    @Operation(
            summary = "Get filtered list of public artworks",
            description = "Retrieves public artworks, optionally filtered by style, year, or artist.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successful return of the filtered list"),
                    @ApiResponse(responseCode = "400", description = "Bad Request (e.g., Invalid style name or format error)")
            }
    )
    @GetMapping("/artworks")
    public List<ArtworkPublicSummaryResponse> getAllPublicArtworks(
            @RequestParam(required = false) Optional<String> style,
            @RequestParam(required = false) Optional<Integer> year,
            @RequestParam(required = false) Optional<String> artist
    ) {
        return artworkService.getAllPublicArtworksWithFilter(
                style.orElse(null),
                year.orElse(null),
                artist.orElse(null)
        );
    }

    // POST /api/visitor/artworks/{id}/reviews – leave a review/rating (rating, comment)
    /**
     * POST /api/visitor/artworks/{id}/reviews
     * Allows the authenticated visitor to leave a review (rating and comment) for a specific artwork.
     *
     * @param id ID of the artwork being reviewed
     * @param request DTO containing the rating and comment
     * @param userDetails details of the authenticated visitor
     * @return The created review response
     */
    @Operation(
            summary = "Create a new review for an artwork",
            description = "Allows an authenticated visitor to submit a rating and comment on a specific artwork.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Review successfully created"),
                    @ApiResponse(responseCode = "400", description = "Bad Request (Validation failed on rating/comment)"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized"),
                    @ApiResponse(responseCode = "404", description = "Artwork or Visitor user not found")
            }
    )
    @PostMapping("/artworks/{id}/reviews")
    public ResponseEntity<ReviewResponse> createReview(
            @PathVariable Long id,
            @Valid @RequestBody ReviewCreateRequest request,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        ReviewResponse response = reviewService.createReview(
                id,
                request,
                userDetails.getUsername()
        );

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // GET /api/visitor/artworks/{id}/reviews – list of work reviews
    /**
     * GET /api/visitor/artworks/{id}/reviews
     * Retrieves all reviews associated with a specific artwork.
     *
     * @param id ID of the artwork
     * @return A list of review responses
     */
    @Operation(
            summary = "Get all reviews for an artwork",
            description = "Retrieves a list of all submitted reviews for a specific artwork ID.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successful return of the review list"),
                    @ApiResponse(responseCode = "404", description = "Artwork not found with the specified ID")
            }
    )
    @GetMapping("/artworks/{id}/reviews")
    public List<ReviewResponse> getArtworkReviews(
            @PathVariable Long id
    ) {
        return reviewService.getReviewsByArtworkId(id);
    }
}

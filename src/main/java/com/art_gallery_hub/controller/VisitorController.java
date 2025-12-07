package com.art_gallery_hub.controller;

import com.art_gallery_hub.dto.artwork.ArtworkPublicSummaryResponse;
import com.art_gallery_hub.dto.review.ReviewCreateRequest;
import com.art_gallery_hub.dto.review.ReviewResponse;
import com.art_gallery_hub.service.ArtworkService;
import com.art_gallery_hub.service.ReviewService;
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
    @GetMapping("/artworks/{id}/reviews")
    public List<ReviewResponse> getArtworkReviews(
            @PathVariable Long id
    ) {
        return reviewService.getReviewsByArtworkId(id);
    }
}

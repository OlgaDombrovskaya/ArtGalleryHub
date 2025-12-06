package com.art_gallery_hub.service;

import com.art_gallery_hub.dto.ArtworkPublicDetailsResponse;
import com.art_gallery_hub.dto.ArtworkPublicSummaryResponse;
import com.art_gallery_hub.dto.ReviewResponse;
import com.art_gallery_hub.mapper.ArtworkMapper;
import com.art_gallery_hub.mapper.ReviewMapper;
import com.art_gallery_hub.model.Artwork;
import com.art_gallery_hub.model.Review;
import com.art_gallery_hub.repository.ArtworkRepository;
import com.art_gallery_hub.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ArtworkService {

    private final ArtworkRepository artworkRepository;
    private final ReviewRepository reviewRepository;
    private final ArtworkMapper artworkMapper;
    private final ReviewMapper reviewMapper;

    @Transactional
    public List<ArtworkPublicSummaryResponse> getAllArtworks() {

        List<Artwork> artworks = artworkRepository.findByIsPublicTrue();

        return artworks.stream()
                .map(artwork -> artworkMapper.toArtworkPublicSummaryResponse(artwork))
                .toList();
    }

    @Transactional
    public ArtworkPublicDetailsResponse getArtworkDetails(Long artworkId) {
        Artwork artwork = artworkRepository.findById(artworkId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Artwork not found with ID: " + artworkId));

        List<Review> reviews = reviewRepository.findByArtworkId(artworkId);

        List<ReviewResponse> reviewResponses = reviews.stream()
                .map(review -> reviewMapper.toReviewResponse(review))
                .toList();

        return artworkMapper.toArtworkPublicDetailsResponse(artwork, reviewResponses);
    }
}

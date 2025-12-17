package com.art_gallery_hub.service;

import com.art_gallery_hub.dto.review.ReviewCreateRequest;
import com.art_gallery_hub.dto.review.ReviewResponse;
import com.art_gallery_hub.mapper.ReviewMapper;
import com.art_gallery_hub.model.Artwork;
import com.art_gallery_hub.model.Review;
import com.art_gallery_hub.model.User;
import com.art_gallery_hub.repository.ArtworkRepository;
import com.art_gallery_hub.repository.ReviewRepository;
import com.art_gallery_hub.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final UserRepository userRepository;
    private final ArtworkRepository artworkRepository;
    private final ReviewRepository reviewRepository;
    private final ReviewMapper reviewMapper;

    private User findUserOrThrow(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "User not found with username: " + username));
    }

    private Artwork findArtworkOrThrow(Long artworkId) {
        return artworkRepository.findById(artworkId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Artwork not found with ID: " + artworkId));
    }

    @Transactional
    public ReviewResponse createReview(
            Long artworkId,
            ReviewCreateRequest request,
            String username
    ) {
        User author = findUserOrThrow(username);
        Artwork artwork = findArtworkOrThrow(artworkId);

        Review newReview = reviewMapper.toEntity(
                artwork,
                author,
                request
        );

        author.getReviews().add(newReview);
        artwork.getReviews().add(newReview);

        Review savedReview = reviewRepository.save(newReview);

        return reviewMapper.toReviewResponse(savedReview);
    }

    @Transactional
    public List<ReviewResponse> getReviewsByArtworkId(
            Long artworkId
    ) {
        List<Review> reviews = reviewRepository.findByArtworkId(artworkId);

        return reviews.stream()
                .map(reviewMapper::toReviewResponse)
                .toList();
    }
}

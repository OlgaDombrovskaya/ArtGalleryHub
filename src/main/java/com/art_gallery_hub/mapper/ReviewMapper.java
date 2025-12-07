package com.art_gallery_hub.mapper;

import com.art_gallery_hub.dto.review.ReviewCreateRequest;
import com.art_gallery_hub.dto.review.ReviewResponse;
import com.art_gallery_hub.model.Artwork;
import com.art_gallery_hub.model.Review;
import com.art_gallery_hub.model.User;
import org.springframework.stereotype.Component;

@Component
public class ReviewMapper {

    public Review toEntity(
            Artwork artwork,
            User author,
            ReviewCreateRequest request
    ) {
        Review review = new Review();

        review.setArtwork(artwork);
        review.setAuthor(author);
        review.setRating(request.rating());
        review.setComment(request.comment());

        return review;
    }

    public ReviewResponse toReviewResponse(Review review) {
        return new ReviewResponse(
                review.getId(),
                review.getArtwork().getId(),
                review.getAuthor().getUsername(),
                review.getRating(),
                review.getComment(),
                review.getCreatedAt()
        );
    }
}

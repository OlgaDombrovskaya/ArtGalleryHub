package com.art_gallery_hub.repository;

import com.art_gallery_hub.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findByArtwork_Id(Long artworkId);

    List<Review> findByAuthor_Id(Long authorId);
}

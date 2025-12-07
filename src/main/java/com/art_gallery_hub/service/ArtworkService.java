package com.art_gallery_hub.service;

import com.art_gallery_hub.dto.artwork.ArtworkCreateRequest;
import com.art_gallery_hub.dto.artwork.ArtworkArtistResponse;
import com.art_gallery_hub.dto.artwork.ArtworkPublicDetailsResponse;
import com.art_gallery_hub.dto.artwork.ArtworkPublicSummaryResponse;
import com.art_gallery_hub.dto.artwork.ArtworkUpdateRequest;
import com.art_gallery_hub.dto.review.ReviewResponse;
import com.art_gallery_hub.mapper.ArtworkMapper;
import com.art_gallery_hub.mapper.ReviewMapper;
import com.art_gallery_hub.model.ArtistProfile;
import com.art_gallery_hub.model.Artwork;
import com.art_gallery_hub.model.Review;
import com.art_gallery_hub.model.User;
import com.art_gallery_hub.repository.ArtistProfileRepository;
import com.art_gallery_hub.repository.ArtworkRepository;
import com.art_gallery_hub.repository.ReviewRepository;
import com.art_gallery_hub.repository.UserRepository;
import com.art_gallery_hub.service.storage.LocalFileStorageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ArtworkService {

    private final UserRepository userRepository;
    private final ArtistProfileRepository artistProfileRepository;
    private final ArtworkRepository artworkRepository;
    private final ReviewRepository reviewRepository;
    private final ArtworkMapper artworkMapper;
    private final ReviewMapper reviewMapper;

    private final LocalFileStorageService localFileStorageService;

    private User findUserOrThrow(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "User not found with username: " + username));
    }

    private ArtistProfile findProfileOrThrow(Long userId) {
        return artistProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Artist Profile not found with user ID: " + userId));
    }

    private Artwork findArtworkOrThrow(Long artworkId) {
        return artworkRepository.findById(artworkId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Artwork not found with ID: " + artworkId));
    }

    @Transactional
    public List<ArtworkPublicSummaryResponse> getAllArtworks() {
        List<Artwork> artworks = artworkRepository.findByIsPublicTrue();
        log.info("Found {} public artworks", artworks.size());

        return artworks.stream()
                .map(artwork -> artworkMapper.toArtworkPublicSummaryResponse(artwork))
                .toList();
    }

    @Transactional
    public ArtworkPublicDetailsResponse getArtworkDetails(Long artworkId) {
        Artwork artwork = findArtworkOrThrow(artworkId);
        List<Review> reviews = reviewRepository.findByArtworkId(artworkId);
        log.info("Found {} reviews for artwork id={}", reviews.size(), artworkId);

        List<ReviewResponse> reviewResponses = reviews.stream()
                .map(review -> reviewMapper.toReviewResponse(review))
                .toList();

        return artworkMapper.toArtworkPublicDetailsResponse(artwork, reviewResponses);
    }

    @Transactional
    public ArtworkArtistResponse createArtwork(
            ArtworkCreateRequest request,
            MultipartFile imageFile,
            String username
    ) {
        User user = findUserOrThrow(username);
        ArtistProfile artistProfile = findProfileOrThrow(user.getId());

        if (imageFile.isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Image file is required.");
        }

        String imagePath = localFileStorageService.storeFile(imageFile);

        Artwork newArtwork = new Artwork();
        newArtwork.setArtist(artistProfile);
        newArtwork.setTitle(request.title());
        newArtwork.setDescription(request.description());
        newArtwork.setYear(request.year());
        newArtwork.setStyle(request.style());
        newArtwork.setImagePath(imagePath);

        Artwork savedArtwork = artworkRepository.save(newArtwork);

        return artworkMapper.toArtworkArtistResponse(savedArtwork);
    }

    @Transactional
    public List<ArtworkArtistResponse> getArtworksPrivate(String username) {
        User user = findUserOrThrow(username);
        ArtistProfile artistProfile = findProfileOrThrow(user.getId());

        List<Artwork> artworks = artworkRepository.findByArtistId(artistProfile.getId());

        return artworks.stream()
                .map(artwork -> artworkMapper.toArtworkArtistResponse(artwork))
                .toList();
    }

    @Transactional
    public ArtworkArtistResponse updateArtwork(
            Long artworkId,
            ArtworkUpdateRequest request
    ) {
        Artwork artwork = findArtworkOrThrow(artworkId);

        artwork.setTitle(request.title());
        artwork.setDescription(request.description());
        artwork.setYear(request.year());
        artwork.setStyle(request.style());
        artwork.setPublic(request.isPublic());

        Artwork updatedArtwork = artworkRepository.save(artwork);

        return artworkMapper.toArtworkArtistResponse(updatedArtwork);
    }

    @Transactional
    public void deleteArtwork(Long artworkId) {
        Artwork artwork = findArtworkOrThrow(artworkId);

        artworkRepository.delete(artwork);
    }
}

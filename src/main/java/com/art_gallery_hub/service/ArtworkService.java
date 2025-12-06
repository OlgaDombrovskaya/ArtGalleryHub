package com.art_gallery_hub.service;

import com.art_gallery_hub.dto.artwork.ArtworkCreateRequest;
import com.art_gallery_hub.dto.artwork.ArtworkPrivateResponse;
import com.art_gallery_hub.dto.artwork.ArtworkPublicDetailsResponse;
import com.art_gallery_hub.dto.artwork.ArtworkPublicSummaryResponse;
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
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

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

    @Transactional
    public ArtworkPublicSummaryResponse createArtwork(
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

        return artworkMapper.toArtworkPublicSummaryResponse(savedArtwork);
    }

    @Transactional
    public List<ArtworkPrivateResponse> getArtworksPrivate(String username) {
        User user = findUserOrThrow(username);
        ArtistProfile artistProfile = findProfileOrThrow(user.getId());

        List<Artwork> artworks = artworkRepository.findByArtistId(artistProfile.getId());

        return artworks.stream()
                .map(artwork -> artworkMapper.toArtworkPrivateResponse(artwork))
                .toList();
    }

    @PreAuthorize("#artwork.artist.user.username == authentication.name or hasRole('ADMIN')")
    public Artwork updateArtwork(Artwork artwork) {
        return artwork;
    }
}

package com.art_gallery_hub.controller;

import com.art_gallery_hub.dto.artist_profile.ArtistProfileResponse;
import com.art_gallery_hub.dto.artist_profile.ArtistProfileUpdateRequest;
import com.art_gallery_hub.dto.artwork.ArtworkCreateRequest;
import com.art_gallery_hub.dto.artwork.ArtworkPrivateResponse;
import com.art_gallery_hub.dto.artwork.ArtworkPublicSummaryResponse;
import com.art_gallery_hub.model.ArtistProfile;
import com.art_gallery_hub.model.Artwork;
import com.art_gallery_hub.model.User;
import com.art_gallery_hub.repository.ArtistProfileRepository;
import com.art_gallery_hub.repository.ArtworkRepository;
import com.art_gallery_hub.repository.UserRepository;
import com.art_gallery_hub.service.ArtistProfileService;
import com.art_gallery_hub.service.ArtworkService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Tag(
        name = "Artist Profile and Management",
        description = "Operations related to the authenticated Artist's " +
                "personal profile, artworks, and exhibition invitations.")
@RestController
@RequestMapping("/api/artist")
@RequiredArgsConstructor
public class ArtistController {

    private final ArtistProfileService artistProfileService;
    private final ArtworkService artworkService;

    // GET /api/artist/profile – view your ArtistProfile
    @GetMapping("/profile")
    public ArtistProfileResponse getMyProfile(@AuthenticationPrincipal UserDetails userDetails) {
        return artistProfileService.getArtistProfile(
                userDetails.getUsername());
    }

    // PUT /api/artist/profile – edit your profile
    @PutMapping("/profile")
    public ArtistProfileResponse updateProfile(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody ArtistProfileUpdateRequest artistProfileUpdateRequest
    ) {
        return artistProfileService.updateArtistProfile(
                userDetails.getUsername(), artistProfileUpdateRequest);
    }

    // POST /api/artist/artworks – creating a work (painting) with image upload (MultipartFile)
    @PostMapping("/artworks")
    public ResponseEntity<ArtworkPublicSummaryResponse> createArtwork(
            @RequestPart("artwork") ArtworkCreateRequest request,
            @RequestPart("image") MultipartFile imageFile,
            @AuthenticationPrincipal UserDetails userDetails) {
        ArtworkPublicSummaryResponse response = artworkService.createArtwork(
                request,
                imageFile,
                userDetails.getUsername());

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // GET /api/artist/artworks/my – list of your own works
    @GetMapping("/artworks/my")
    public List<ArtworkPrivateResponse> getMyArtworks(@AuthenticationPrincipal UserDetails userDetails) {
        return artworkService.getArtworksPrivate(
                userDetails.getUsername());
    }

    // PUT /api/artist/artworks/{id} – editing (only your own works)
    // updateArtwork

    // DELETE /api/artist/artworks/{id} – deleting / hiding your own work
    // deleteArtwork

    // GET /api/artist/invitations – list of invitations to exhibitions
    // getInvitations

    // POST /api/artist/invitations/{id}/accept – accept the invitation
    // acceptInvitation

    // POST /api/artist/invitations/{id}/decline – decline
    // declineInvitation
}

package com.art_gallery_hub.controller;

import com.art_gallery_hub.dto.ArtistProfileResponse;
import com.art_gallery_hub.dto.ArtistProfileUpdateRequest;
import com.art_gallery_hub.model.ArtistProfile;
import com.art_gallery_hub.model.Artwork;
import com.art_gallery_hub.model.User;
import com.art_gallery_hub.repository.ArtistProfileRepository;
import com.art_gallery_hub.repository.ArtworkRepository;
import com.art_gallery_hub.repository.UserRepository;
import com.art_gallery_hub.service.ArtistProfileService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
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
    private final ArtistProfileRepository artistProfileRepository;
    private final ArtworkRepository artworkRepository;
    private final UserRepository userRepository;

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

    // TODO
    // POST /api/artist/artworks – creating a work (painting) with image upload (MultipartFile)
    @PostMapping("/artworks")
    public void createArtwork(
            @RequestPart("artwork") ArtworkCreationRequest request, // Данные о работе
            @RequestPart("image") MultipartFile image,             // Файл изображения
            @AuthenticationPrincipal UserDetails userDetails) {
        carService.attachImage(id, file);
    }

    // GET /api/artist/artworks/my – list of your own works
    @GetMapping("/artworks/my")
    public List<Artwork> getMyArtworks(@AuthenticationPrincipal UserDetails userDetails) {
        String username = userDetails.getUsername();
        // 1. получаем USER
        User user = userRepository
                .findByUsername(username)
                .orElseThrow();

        // 2. получаем профиль художника
        ArtistProfile artistProfile = artistProfileRepository
                .findByUser(user)
                .orElseThrow();

        // 3. получаем работы
        return artworkRepository.findByArtistId(artistProfile.getId());
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

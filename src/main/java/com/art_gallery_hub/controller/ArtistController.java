package com.art_gallery_hub.controller;

import com.art_gallery_hub.dto.artist_profile.ArtistProfileResponse;
import com.art_gallery_hub.dto.artist_profile.ArtistProfileUpdateRequest;
import com.art_gallery_hub.dto.artwork.ArtworkCreateRequest;
import com.art_gallery_hub.dto.artwork.ArtworkArtistResponse;
import com.art_gallery_hub.dto.artwork.ArtworkUpdateRequest;
import com.art_gallery_hub.dto.invitation.InvitationResponse;
import com.art_gallery_hub.service.ArtistProfileService;
import com.art_gallery_hub.service.ArtworkService;
import com.art_gallery_hub.service.InvitationService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
    private final InvitationService invitationService;

    // GET /api/artist/profile – view your ArtistProfile
    @GetMapping("/profile")
    public ArtistProfileResponse getMyProfile(
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        return artistProfileService.getArtistProfile(
                userDetails.getUsername());
    }

    // PUT /api/artist/profile – edit your profile
    @PutMapping("/profile")
    public ResponseEntity<ArtistProfileResponse> updateMyProfile(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody ArtistProfileUpdateRequest artistProfileUpdateRequest
    ) {
        ArtistProfileResponse response = artistProfileService.updateArtistProfile(
                userDetails.getUsername(),
                artistProfileUpdateRequest);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // POST /api/artist/artworks – creating a work (painting) with image upload (MultipartFile)
    @PostMapping("/artworks")
    public ResponseEntity<ArtworkArtistResponse> createArtwork(
            @RequestPart("artwork") ArtworkCreateRequest request,
            @RequestPart("image") MultipartFile imageFile,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        ArtworkArtistResponse response = artworkService.createArtwork(
                request,
                imageFile,
                userDetails.getUsername());

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // GET /api/artist/artworks/my – list of your own works
    @GetMapping("/artworks/my")
    public List<ArtworkArtistResponse> getMyArtworks(
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        return artworkService.getArtworksPrivate(
                userDetails.getUsername());
    }

    // PUT /api/artist/artworks/{id} – editing (only your own works)
    @PreAuthorize("@artworkAccessChecker.isOwner(#id, authentication) or hasRole('ADMIN')")
    @PutMapping("/artworks/{id}")
    public ResponseEntity<ArtworkArtistResponse> updateMyArtwork(
            @PathVariable Long id,
            @RequestBody ArtworkUpdateRequest request
    ) {
        ArtworkArtistResponse response = artworkService.updateArtwork(id, request);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // DELETE /api/artist/artworks/{id} – deleting / hiding your own work
    @PreAuthorize("@artworkAccessChecker.isOwner(#id, authentication) or hasRole('ADMIN')")
    @DeleteMapping("/artworks/{id}")
    public ResponseEntity<Void> deleteMyArtwork(
            @PathVariable Long id
    ) {
        artworkService.deleteArtwork(id);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // GET /api/artist/invitations – list of invitations to exhibitions
    @GetMapping("/invitations")
    public List<InvitationResponse> getMyInvitations(
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        return invitationService.getAllInvitations();
    }

    // POST /api/artist/invitations/{id}/accept – accept the invitation
    // acceptInvitation
    @PreAuthorize("@invitationAccessChecker.isOwner(#id, authentication) or hasRole('ADMIN')")
    @PostMapping("/invitations/{id}/accept")
    public ResponseEntity<InvitationResponse> acceptMyInvitation(
            @PathVariable Long id
    ) {
        InvitationResponse response = invitationService.acceptInvitation(id);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // POST /api/artist/invitations/{id}/decline – decline
    @PreAuthorize("@invitationAccessChecker.isOwner(#id, authentication) or hasRole('ADMIN')")
    @PostMapping("/invitations/{id}/decline")
    public ResponseEntity<InvitationResponse> declineMyInvitation(
            @PathVariable Long id
    ) {
        InvitationResponse response = invitationService.declineInvitation(id);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}

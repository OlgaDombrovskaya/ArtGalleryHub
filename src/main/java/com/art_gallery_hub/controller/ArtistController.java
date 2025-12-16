package com.art_gallery_hub.controller;

import com.art_gallery_hub.dto.artist_profile.ArtistProfileResponse;
import com.art_gallery_hub.dto.artist_profile.ArtistProfileUpdateRequest;
import com.art_gallery_hub.dto.artwork.ArtworkCreateRequest;
import com.art_gallery_hub.dto.artwork.ArtworkArtistResponse;
import com.art_gallery_hub.dto.artwork.ArtworkUpdateRequest;
import com.art_gallery_hub.dto.invitation.InvitationArtistResponse;
import com.art_gallery_hub.service.ArtistProfileService;
import com.art_gallery_hub.service.ArtworkService;
import com.art_gallery_hub.service.InvitationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
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
    /**
     * GET /api/artist/profile
     * Retrieves the profile details for the currently authenticated artist.
     *
     * @param userDetails details of the authenticated user
     * @return the artist's full profile response
     */
    @Operation(
            summary = "View current Artist Profile",
            description = "Retrieves the detailed profile information for the authenticated user.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successful retrieval"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized"),
                    @ApiResponse(responseCode = "403", description = "Forbidden (Missing ARTIST role)"),
                    @ApiResponse(responseCode = "404", description = "User or Artist Profile not found")
            }
    )
    @GetMapping("/profile")
    public ArtistProfileResponse getMyProfile(
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        return artistProfileService.getArtistProfile(
                userDetails.getUsername());
    }

    // PUT /api/artist/profile – edit your profile
    /**
     * PUT /api/artist/profile
     * Updates the artist's profile details (bio, website, display name).
     *
     * @param artistProfileUpdateRequest DTO containing new profile data
     * @param userDetails details of the authenticated user
     * @return the updated artist's profile response
     */
    @Operation(
            summary = "Update current Artist Profile",
            description = "Updates editable fields (display name, bio, website) on the authenticated artist's profile.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Profile successfully updated"),
                    @ApiResponse(responseCode = "400", description = "Bad Request (Validation failed)"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized"),
                    @ApiResponse(responseCode = "404", description = "User or Artist Profile not found")
            }
    )
    @PutMapping("/profile")
    public ResponseEntity<ArtistProfileResponse> updateMyProfile(
            @Valid @RequestBody ArtistProfileUpdateRequest artistProfileUpdateRequest,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        ArtistProfileResponse response = artistProfileService.updateArtistProfile(
                userDetails.getUsername(),
                artistProfileUpdateRequest);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // POST /api/artist/artworks – creating a work (painting) with image upload (MultipartFile)
    /**
     * POST /api/artist/artworks
     * Creates a new artwork entry and handles the required image upload via multipart form data.
     *
     * @param request DTO containing artwork metadata (title, year, style, etc.)
     * @param imageFile the image file for the artwork
     * @param userDetails details of the authenticated user
     * @return the created artwork response
     */
    @Operation(
            summary = "Create new Artwork with image upload",
            description = "Creates a new artwork entry linked to the authenticated artist. Requires 'multipart/form-data'.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Artwork successfully created"),
                    @ApiResponse(responseCode = "400", description = "Bad Request (Validation failed or missing image file)"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized"),
                    @ApiResponse(responseCode = "404", description = "Artist Profile not found"),
                    @ApiResponse(responseCode = "500", description = "Internal Server Error (e.g., file storage failure)")
            }
    )
    @PostMapping("/artworks")
    public ResponseEntity<ArtworkArtistResponse> createArtwork(
            @Valid @RequestPart("artwork") ArtworkCreateRequest request,
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
    /**
     * GET /api/artist/artworks/my
     * Retrieves a list of all artworks created by the authenticated artist.
     *
     * @param userDetails details of the authenticated user
     * @return list of the artist's own artwork responses
     */
    @Operation(
            summary = "Get current Artist's Artworks",
            description = "Returns a list of all artworks submitted by the authenticated artist.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successful return of the list"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized"),
                    @ApiResponse(responseCode = "404", description = "Artist Profile not found")
            }
    )
    @GetMapping("/artworks/my")
    public List<ArtworkArtistResponse> getMyArtworks(
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        return artworkService.getArtworksForArtist(
                userDetails.getUsername());
    }

    // PUT /api/artist/artworks/{id} – editing (only your own works)
    /**
     * PUT /api/artist/artworks/{id}
     * Updates the details of a specific artwork.
     * Access is restricted to the artwork's owner or an Administrator.
     *
     * @param id ID of the artwork to update
     * @param request DTO with the updated artwork details
     * @return the updated artwork response
     */
    @Operation(
            summary = "Update an existing Artwork",
            description = "Updates metadata of a specific artwork. Requires ownership or ADMIN role.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Artwork successfully updated"),
                    @ApiResponse(responseCode = "400", description = "Bad Request (Validation failed)"),
                    @ApiResponse(responseCode = "403", description = "Forbidden (Not the owner or Admin)"),
                    @ApiResponse(responseCode = "404", description = "Artwork not found")
            }
    )
    @PreAuthorize("@artworkAccessChecker.isOwner(#id, authentication) or hasRole('ADMIN')")
    @PutMapping("/artworks/{id}")
    public ResponseEntity<ArtworkArtistResponse> updateMyArtwork(
            @PathVariable Long id,
            @Valid @RequestBody ArtworkUpdateRequest request
    ) {
        ArtworkArtistResponse response = artworkService.updateArtwork(id, request);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // DELETE /api/artist/artworks/{id} – deleting / hiding your own work
    /**
     * DELETE /api/artist/artworks/{id}
     * Deletes (removes) a specific artwork.
     * Access is restricted to the artwork's owner or an Administrator.
     *
     * @param id ID of the artwork to delete
     * @return 204 No Content upon successful deletion
     */
    @Operation(
            summary = "Delete an Artwork",
            description = "Permanently deletes a specific artwork. Requires ownership or ADMIN role.",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Artwork successfully deleted"),
                    @ApiResponse(responseCode = "403", description = "Forbidden (Not the owner or Admin)"),
                    @ApiResponse(responseCode = "404", description = "Artwork not found")
            }
    )
    @PreAuthorize("@artworkAccessChecker.isOwner(#id, authentication) or hasRole('ADMIN')")
    @DeleteMapping("/artworks/{id}")
    public ResponseEntity<Void> deleteMyArtwork(
            @PathVariable Long id
    ) {
        artworkService.deleteArtwork(id);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // GET /api/artist/invitations – list of invitations to exhibitions
    /**
     * GET /api/artist/invitations
     * Retrieves a list of all exhibition invitations sent to the authenticated artist.
     *
     * @param userDetails details of the authenticated user
     * @return list of invitations received
     */
    @Operation(
            summary = "Get all received Invitations",
            description = "Returns a list of all exhibition invitations sent to the authenticated artist.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successful return of the list"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized"),
                    @ApiResponse(responseCode = "404", description = "Artist Profile not found")
            }
    )
    @GetMapping("/invitations")
    public List<InvitationArtistResponse> getMyInvitations(
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        return invitationService.getAllInvitationsByArtist(userDetails.getUsername());
    }

    // POST /api/artist/invitations/{id}/accept – accept the invitation
    /**
     * POST /api/artist/invitations/{id}/accept
     * Accepts a specific exhibition invitation, changing its status to ACCEPTED.
     * Access is restricted to the invited artist or an Administrator.
     *
     * @param id ID of the invitation to accept
     * @return the accepted invitation response
     */
    @Operation(
            summary = "Accept an Invitation",
            description = "Accepts the invitation by changing its status to ACCEPTED. Requires ownership or ADMIN role.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Invitation successfully accepted"),
                    @ApiResponse(responseCode = "400", description = "Bad Request (Invitation already accepted)"),
                    @ApiResponse(responseCode = "403", description = "Forbidden (Not the owner or Admin)"),
                    @ApiResponse(responseCode = "404", description = "Invitation not found")
            }
    )
    @PreAuthorize("@invitationAccessChecker.isOwner(#id, authentication) or hasRole('ADMIN')")
    @PostMapping("/invitations/{id}/accept")
    public ResponseEntity<InvitationArtistResponse> acceptMyInvitation(
            @PathVariable Long id
    ) {
        InvitationArtistResponse response = invitationService.acceptInvitation(id);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // POST /api/artist/invitations/{id}/decline – decline
    /**
     * POST /api/artist/invitations/{id}/decline
     * Declines a specific exhibition invitation, changing its status to DECLINED.
     * Access is restricted to the invited artist or an Administrator.
     *
     * @param id ID of the invitation to decline
     * @return the declined invitation response
     */
    @Operation(
            summary = "Decline an Invitation",
            description = "Declines the invitation by changing its status to DECLINED. Requires ownership or ADMIN role.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Invitation successfully declined"),
                    @ApiResponse(responseCode = "400", description = "Bad Request (Invitation already declined)"),
                    @ApiResponse(responseCode = "403", description = "Forbidden (Not the owner or Admin)"),
                    @ApiResponse(responseCode = "404", description = "Invitation not found")
            }
    )
    @PreAuthorize("@invitationAccessChecker.isOwner(#id, authentication) or hasRole('ADMIN')")
    @PostMapping("/invitations/{id}/decline")
    public ResponseEntity<InvitationArtistResponse> declineMyInvitation(
            @PathVariable Long id
    ) {
        InvitationArtistResponse response = invitationService.declineInvitation(id);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}

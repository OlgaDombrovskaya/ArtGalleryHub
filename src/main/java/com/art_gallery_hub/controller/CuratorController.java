package com.art_gallery_hub.controller;

import com.art_gallery_hub.dto.exhibition.ExhibitionCreateOrUpdateRequest;
import com.art_gallery_hub.dto.exhibition.ExhibitionCuratorFullDetailsResponse;
import com.art_gallery_hub.dto.exhibition.ExhibitionCuratorSummaryResponse;
import com.art_gallery_hub.dto.exhibition.ExhibitionStatusUpdateRequest;
import com.art_gallery_hub.dto.invitation.InvitationCuratorResponse;
import com.art_gallery_hub.service.ExhibitionService;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(
        name = "Curator and Exhibition Management",
        description = "Operations available to the Curator for creating, editing, " +
                "and managing exhibitions, including sending invitations to artists")
@RestController
@RequestMapping("/api/curator")
@RequiredArgsConstructor
public class CuratorController {

    private final ExhibitionService exhibitionService;

    // GET /api/curator/exhibitions/my – exhibitions that this curator is responsible for
    /**
     * GET /api/curator/exhibitions/my
     * Retrieves a list of all exhibitions for which the current authenticated curator is responsible.
     *
     * @param userDetails authenticated user details (for extracting the username)
     * @return a list of summary responses for the curator's exhibitions
     */
    @Operation(
            summary = "Get current curator's exhibitions",
            description = "Returns a list of all exhibitions created by the authenticated curator.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successful return of the list"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized"),
                    @ApiResponse(responseCode = "403", description = "Forbidden (User is not a Curator)")
            }
    )
    @GetMapping("/exhibitions/my")
    public List<ExhibitionCuratorSummaryResponse> getMyExhibitions(
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        return exhibitionService.getCuratorExhibitions(userDetails.getUsername());
    }

    // POST /api/curator/exhibitions – creating a new exhibition
    /**
     * POST /api/curator/exhibitions
     * Creates a new exhibition. The curator is derived from the current security context.
     *
     * @param request data for creating the exhibition (title, description, dates)
     * @param userDetails authenticated user details (for extracting the curator username)
     * @return full details of the created exhibition
     */
    @Operation(
            summary = "Create a new exhibition",
            description = "Creates a new exhibition assigned to the authenticated curator.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Exhibition successfully created"),
                    @ApiResponse(responseCode = "400", description = "Bad Request (Validation failed)"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized"),
                    @ApiResponse(responseCode = "404", description = "Curator user not found")
            }
    )
    @PostMapping("/exhibitions")
    public ResponseEntity<ExhibitionCuratorFullDetailsResponse> createExhibition(
            @Valid @RequestBody ExhibitionCreateOrUpdateRequest request,
            @AuthenticationPrincipal UserDetails userDetails // ?
    ) {
        ExhibitionCuratorFullDetailsResponse response = exhibitionService.createExhibition(
                request,
                userDetails.getUsername()
        );
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // PUT /api/curator/exhibitions/{id} – editing your exhibition
    /**
     * PUT /api/curator/exhibitions/{id}
     * Updates the details of an existing exhibition.
     * Access is restricted to the exhibition owner or an Administrator.
     *
     * @param id ID of the exhibition to update
     * @param request new exhibition data
     * @return full details of the updated exhibition
     */
    @Operation(
            summary = "Update an existing exhibition",
            description = "Updates the details (title, description, dates) of a specific exhibition. Requires ownership or ADMIN role.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Exhibition successfully updated"),
                    @ApiResponse(responseCode = "400", description = "Bad Request (Validation failed)"),
                    @ApiResponse(responseCode = "403", description = "Forbidden (Not the owner or Admin)"),
                    @ApiResponse(responseCode = "404", description = "Exhibition not found")
            }
    )
    @PreAuthorize("@exhibitionAccessChecker.isOwner(#id, authentication) or hasRole('ADMIN')")
    @PutMapping("/exhibitions/{id}")
    public ResponseEntity<ExhibitionCuratorFullDetailsResponse> updateExhibition(
            @PathVariable Long id,
            @Valid @RequestBody ExhibitionCreateOrUpdateRequest request
    ) {
        ExhibitionCuratorFullDetailsResponse response =  exhibitionService.updateExhibition(
                id,
                request
        );
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // PATCH /api/curator/exhibitions/{id}/status – status change (PLANNED/OPEN/CLOSED)
    /**
     * PATCH /api/curator/exhibitions/{id}/status
     * Changes the status of an exhibition (PLANNED, OPEN, CLOSED).
     * Access is restricted to the exhibition owner or an Administrator.
     *
     * @param id ID of the exhibition
     * @param request the new status
     * @return summary response of the updated exhibition
     */
    @Operation(
            summary = "Change exhibition status",
            description = "Updates the exhibition status (PLANNED/OPEN/CLOSED). Requires ownership or ADMIN role.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Status successfully updated"),
                    @ApiResponse(responseCode = "400", description = "Bad Request (Validation failed)"),
                    @ApiResponse(responseCode = "403", description = "Forbidden (Not the owner or Admin)"),
                    @ApiResponse(responseCode = "404", description = "Exhibition not found")
            }
    )
    @PreAuthorize("@exhibitionAccessChecker.isOwner(#id, authentication) or hasRole('ADMIN')")
    @PatchMapping("/exhibitions/{id}/status")
    public ResponseEntity<ExhibitionCuratorSummaryResponse> changeExhibitionStatus(
            @PathVariable Long id,
            @Valid @RequestBody ExhibitionStatusUpdateRequest request
    ) {
        ExhibitionCuratorSummaryResponse response = exhibitionService.updateExhibitionStatus(
                id,
                request
        );

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // POST /api/curator/exhibitions/{id}/artworks/{artworkId} – add a work to the exhibition
    /**
     * POST /api/curator/exhibitions/{id}/artworks/{artworkId}
     * Adds an existing artwork to the exhibition.
     * Access is restricted to the exhibition owner or an Administrator.
     *
     * @param id ID of the exhibition
     * @param artworkId ID of the artwork to add
     * @return full details of the updated exhibition
     */
    @Operation(
            summary = "Add an artwork to the exhibition",
            description = "Links an existing artwork to the exhibition. Requires ownership or ADMIN role.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Artwork successfully added"),
                    @ApiResponse(responseCode = "403", description = "Forbidden (Not the owner or Admin)"),
                    @ApiResponse(responseCode = "404", description = "Exhibition or Artwork not found")
            }
    )
    @PreAuthorize("@exhibitionAccessChecker.isOwner(#id, authentication) or hasRole('ADMIN')")
    @PostMapping("/exhibitions/{id}/artworks/{artworkId}")
    public ResponseEntity<ExhibitionCuratorFullDetailsResponse> addArtworkToExhibition(
            @PathVariable Long id,
            @PathVariable Long artworkId
    ) {
        ExhibitionCuratorFullDetailsResponse response = exhibitionService.addArtwork(
                id,
                artworkId
        );
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // GET /api/curator/exhibitions/{id}/invitations – invitations artists
    /**
     * GET /api/curator/exhibitions/{id}/invitations
     * Retrieves a list of all invitations associated with a specific exhibition.
     * Access is restricted to the exhibition owner or an Administrator.
     *
     * @param id ID of the exhibition
     * @return a list of curator invitation responses
     */
    @Operation(
            summary = "Get all invitations for an exhibition",
            description = "Returns a list of all invitations associated with the exhibition. Requires ownership or ADMIN role.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successful return of the list"),
                    @ApiResponse(responseCode = "403", description = "Forbidden (Not the owner or Admin)"),
                    @ApiResponse(responseCode = "404", description = "Exhibition not found")
            }
    )
    @PreAuthorize("@exhibitionAccessChecker.isOwner(#id, authentication) or hasRole('ADMIN')")
    @GetMapping("/exhibitions/{id}/invitations")
    public List<InvitationCuratorResponse> getInvitationsByExhibitionId(
            @PathVariable Long id
    ) {
        return exhibitionService.getInvitations(id);
    }

    // POST /api/curator/exhibitions/{id}/invite/{artistId} – create an invitation to an artist
    /**
     * POST /api/curator/exhibitions/{id}/invite/{artistId}
     * Creates a new PENDING invitation for a specific artist to join the exhibition.
     * Access is restricted to the exhibition owner or an Administrator.
     *
     * @param id ID of the exhibition
     * @param artistId ID of the artist profile to invite
     * @return the created invitation response
     */
    @Operation(
            summary = "Create an invitation for an artist",
            description = "Creates a new PENDING invitation from the exhibition to a specific artist. Requires ownership or ADMIN role.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Invitation successfully created"),
                    @ApiResponse(responseCode = "403", description = "Forbidden (Not the owner or Admin)"),
                    @ApiResponse(responseCode = "404", description = "Exhibition or Artist Profile not found")
            }
    )
    @PreAuthorize("@exhibitionAccessChecker.isOwner(#id, authentication) or hasRole('ADMIN')")
    @PostMapping("/exhibitions/{id}/invite/{artistId}")
    public ResponseEntity<InvitationCuratorResponse> createInvitation(
            @PathVariable Long id,
            @PathVariable Long artistId
    ) {
        InvitationCuratorResponse response = exhibitionService.createInvitation(
                id,
                artistId
        );
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}

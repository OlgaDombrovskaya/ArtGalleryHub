package com.art_gallery_hub.controller;

import com.art_gallery_hub.dto.exhibition.ExhibitionCreateOrUpdateRequest;
import com.art_gallery_hub.dto.exhibition.ExhibitionCuratorFullDetailsResponse;
import com.art_gallery_hub.dto.exhibition.ExhibitionCuratorSummaryResponse;
import com.art_gallery_hub.dto.exhibition.ExhibitionStatusUpdateRequest;
import com.art_gallery_hub.dto.invitation.InvitationCuratorResponse;
import com.art_gallery_hub.service.ExhibitionService;
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
    @GetMapping("/exhibitions/my")
    public List<ExhibitionCuratorSummaryResponse> getMyExhibitions(
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        return exhibitionService.getCuratorExhibitions(userDetails.getUsername());
    }

    // POST /api/curator/exhibitions – creating a new exhibition
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
    @PreAuthorize("@exhibitionAccessChecker.isOwner(#id, authentication) or hasRole('ADMIN')")
    @GetMapping("/exhibitions/{id}/invitations")
    public List<InvitationCuratorResponse> getInvitationsByExhibitionId(
            @PathVariable Long id
    ) {
        return exhibitionService.getInvitations(id);
    }

    // POST /api/curator/exhibitions/{id}/invite/{artistId} – create an invitation to an artist
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

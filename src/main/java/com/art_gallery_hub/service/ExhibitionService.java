package com.art_gallery_hub.service;

import com.art_gallery_hub.dto.artwork.ArtworkPublicSummaryResponse;
import com.art_gallery_hub.dto.exhibition.ExhibitionCreateOrUpdateRequest;
import com.art_gallery_hub.dto.exhibition.ExhibitionCuratorFullDetailsResponse;
import com.art_gallery_hub.dto.exhibition.ExhibitionCuratorSummaryResponse;
import com.art_gallery_hub.dto.exhibition.ExhibitionPublicSummaryResponse;
import com.art_gallery_hub.dto.exhibition.ExhibitionStatusUpdateRequest;
import com.art_gallery_hub.dto.invitation.InvitationCuratorResponse;
import com.art_gallery_hub.enums.ExhibitionStatus;
import com.art_gallery_hub.enums.InvitationStatus;
import com.art_gallery_hub.mapper.ArtworkMapper;
import com.art_gallery_hub.mapper.ExhibitionMapper;
import com.art_gallery_hub.mapper.InvitationMapper;
import com.art_gallery_hub.model.ArtistProfile;
import com.art_gallery_hub.model.Artwork;
import com.art_gallery_hub.model.Exhibition;
import com.art_gallery_hub.model.Invitation;
import com.art_gallery_hub.model.User;
import com.art_gallery_hub.repository.ArtistProfileRepository;
import com.art_gallery_hub.repository.ArtworkRepository;
import com.art_gallery_hub.repository.ExhibitionRepository;
import com.art_gallery_hub.repository.InvitationRepository;
import com.art_gallery_hub.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ExhibitionService {

    private final ExhibitionRepository exhibitionRepository;
    private final UserRepository userRepository;
    private final ArtistProfileRepository artistProfileRepository;
    private final ArtworkRepository artworkRepository;
    private final InvitationRepository invitationRepository;
    private final ExhibitionMapper exhibitionMapper;
    private final ArtworkMapper artworkMapper;
    private final InvitationMapper invitationMapper;

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

    private Exhibition findExhibitionOrThrow(Long id) {
        return exhibitionRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Exhibition not found with ID: " + id));
    }

    private void checkIfInvitationExists(Long exhibitionId, Long artistProfileId) {
        boolean alreadyExists = invitationRepository.existsByExhibitionIdAndArtistId(
                exhibitionId,
                artistProfileId
        );
        if (alreadyExists) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Invitation already exists for this artist (with ID " + artistProfileId
                            + ") and exhibition (with ID " + exhibitionId + ")");
        }
    }

    private List<ArtworkPublicSummaryResponse> getArtworksAsArtworkPublicSummaryResponse(Exhibition exhibition) {
        return exhibition.getArtworks()
                .stream()
                .map(artworkMapper::toArtworkPublicSummaryResponse)
                .toList();
    }

    private List<InvitationCuratorResponse> getInvitationsAsCuratorResponse(Long exhibitionId) {
        return invitationRepository.findByExhibitionId(exhibitionId)
                .stream()
                .map(invitationMapper::toInvitationCuratorResponse)
                .toList();
    }

    @Transactional
    public List<ExhibitionPublicSummaryResponse> getOpenOrPlannedExhibitions() {
        List<Exhibition> exhibitions = exhibitionRepository.findByStatusIn(
                List.of(ExhibitionStatus.OPEN, ExhibitionStatus.PLANNED));
        log.info("Found {} open or planned exhibitions", exhibitions.size());

        return exhibitions.stream()
                .map(exhibitionMapper::toExhibitionPublicSummaryResponse)
                .toList();
    }

    @Transactional
    public List<ExhibitionCuratorSummaryResponse> getCuratorExhibitions(String curatorUsername) {
        User curator = findUserOrThrow(curatorUsername);

        List<Exhibition> exhibitions = exhibitionRepository.findByCuratorId(curator.getId());
        log.info("Found {} curator exhibitions", exhibitions.size());
        return exhibitions.stream()
                .map(exhibitionMapper::toExhibitionCuratorSummaryResponse)
                .toList();
    }

    @Transactional
    public ExhibitionCuratorFullDetailsResponse createExhibition(
            ExhibitionCreateOrUpdateRequest request,
            String curatorUsername
    ) {
        User curator = findUserOrThrow(curatorUsername);

        Exhibition exhibition = exhibitionMapper.toEntity(
                request,
                curator,
                ExhibitionStatus.PLANNED
        );
        curator.getCuratedExhibitions().add(exhibition);

        List<ArtworkPublicSummaryResponse> artworks = new ArrayList<>();
        List<InvitationCuratorResponse> invitations = new ArrayList<>();

        Exhibition savedExhibition = exhibitionRepository.save(exhibition);
        log.info("Saved exhibition {}", savedExhibition.getId());

        return exhibitionMapper.toExhibitionFullDetailsResponse(
                savedExhibition,
                artworks,
                invitations
        );
    }

    @Transactional
    public ExhibitionCuratorFullDetailsResponse updateExhibition(
            Long exhibitionId,
            ExhibitionCreateOrUpdateRequest request
    ) {
        Exhibition exhibition = findExhibitionOrThrow(exhibitionId);

        exhibition.setTitle(request.title());
        exhibition.setDescription(request.description());
        exhibition.setStartDate(request.startDate());
        exhibition.setEndDate(request.endDate());

        List<ArtworkPublicSummaryResponse> artworks = getArtworksAsArtworkPublicSummaryResponse(exhibition);
        List<InvitationCuratorResponse> invitations = getInvitationsAsCuratorResponse(exhibitionId);

        Exhibition updatedExhibition = exhibitionRepository.save(exhibition);
        log.info("Updating exhibition id={} by curator='{}'",
                exhibition.getId(),
                exhibition.getCurator().getUsername());

        return exhibitionMapper.toExhibitionFullDetailsResponse(
                updatedExhibition,
                artworks,
                invitations
        );
    }

    @Transactional
    public ExhibitionCuratorSummaryResponse updateExhibitionStatus(
            Long exhibitionId,
            ExhibitionStatusUpdateRequest request
    ) {
        Exhibition exhibition = findExhibitionOrThrow(exhibitionId);

        exhibition.setStatus(request.newStatus());

        Exhibition updatedExhibition = exhibitionRepository.save(exhibition);
        log.info("Updating exhibition's status exhibitionId={} newStatus={} by curator='{}'",
                exhibition.getId(),
                request.newStatus(),
                exhibition.getCurator().getUsername());
        return exhibitionMapper.toExhibitionCuratorSummaryResponse(updatedExhibition);
    }

    @Transactional
    public ExhibitionCuratorFullDetailsResponse addArtwork(
            Long exhibitionId,
            Long artworkId
    ) {
        Exhibition exhibition = findExhibitionOrThrow(exhibitionId);
        Artwork artwork = findArtworkOrThrow(artworkId);

        exhibition.addArtwork(artwork);

        List<ArtworkPublicSummaryResponse> artworks = getArtworksAsArtworkPublicSummaryResponse(exhibition);
        List<InvitationCuratorResponse> invitations = getInvitationsAsCuratorResponse(exhibitionId);

        Exhibition updatedExhibition = exhibitionRepository.save(exhibition);
        log.info("Artwork id={} added to the exhibition id={} by curator='{}'",
                artworkId,
                exhibition.getId(),
                exhibition.getCurator().getUsername());
        return exhibitionMapper.toExhibitionFullDetailsResponse(
                updatedExhibition,
                artworks,
                invitations
        );
    }

    @Transactional
    public List<InvitationCuratorResponse> getInvitations(Long exhibitionId) {
        List<InvitationCuratorResponse> invitations = getInvitationsAsCuratorResponse(exhibitionId);
        log.info("Found {} invitation(s) to exhibitions ", invitations.size());

        return invitations;
    }

    @Transactional
    public InvitationCuratorResponse createInvitation(
            Long exhibitionId,
            Long artistId
    ) {
        Exhibition exhibition = findExhibitionOrThrow(exhibitionId);
        ArtistProfile artist = findProfileOrThrow(artistId);

        checkIfInvitationExists(exhibitionId, artistId);

        Invitation invitation = new Invitation();
        invitation.setExhibition(exhibition);
        invitation.setArtist(artist);
        invitation.setStatus(InvitationStatus.PENDING);

        artist.getInvitations().add(invitation);
        exhibition.getInvitations().add(invitation);

        Invitation savedInvitation = invitationRepository.save(invitation);
        log.info("Saved invitation {}", savedInvitation.getId());

        return invitationMapper.toInvitationCuratorResponse(savedInvitation);
    }
}

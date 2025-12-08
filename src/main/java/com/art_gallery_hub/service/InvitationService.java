package com.art_gallery_hub.service;

import com.art_gallery_hub.dto.invitation.InvitationArtistResponse;
import com.art_gallery_hub.enums.InvitationStatus;
import com.art_gallery_hub.mapper.InvitationMapper;
import com.art_gallery_hub.model.ArtistProfile;
import com.art_gallery_hub.model.Invitation;
import com.art_gallery_hub.model.User;
import com.art_gallery_hub.repository.ArtistProfileRepository;
import com.art_gallery_hub.repository.InvitationRepository;
import com.art_gallery_hub.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InvitationService {

    private final UserRepository userRepository;
    private final ArtistProfileRepository artistProfileRepository;
    private final InvitationRepository invitationRepository;
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

    private Invitation findInvitationOrThrow(Long invitationId) {
        return invitationRepository.findById(invitationId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Invitation not found with ID: " + invitationId));
    }

    @Transactional
    public List<InvitationArtistResponse> getAllInvitationsByArtist(String username) {
        User user = findUserOrThrow(username);
        ArtistProfile artistProfile = findProfileOrThrow(user.getId());

        List<Invitation> invitations = invitationRepository.findByArtistId(artistProfile.getId());

        return invitations.stream()
                .map(invitationMapper::toInvitationResponse)
                .toList();
    }

    @Transactional
    public InvitationArtistResponse acceptInvitation(Long invitationId) {
        Invitation invitation = findInvitationOrThrow(invitationId);

        if (invitation.getStatus() == InvitationStatus.ACCEPTED) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Invitation is already accepted");
        }
        invitation.setStatus(InvitationStatus.ACCEPTED);

        Invitation acceptedInvitation = invitationRepository.save(invitation);

        return invitationMapper.toInvitationResponse(acceptedInvitation);
    }

    @Transactional
    public InvitationArtistResponse declineInvitation(Long invitationId) {
        Invitation invitation = findInvitationOrThrow(invitationId);

        if (invitation.getStatus() == InvitationStatus.DECLINED) {
            throw  new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Invitation is already declined");
        }
        invitation.setStatus(InvitationStatus.DECLINED);

        Invitation declinedInvitation = invitationRepository.save(invitation);

        return invitationMapper.toInvitationResponse(declinedInvitation);
    }

}

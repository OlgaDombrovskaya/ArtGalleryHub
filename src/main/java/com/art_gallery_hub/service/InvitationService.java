package com.art_gallery_hub.service;

import com.art_gallery_hub.dto.invitation.InvitationResponse;
import com.art_gallery_hub.enums.InvitationStatus;
import com.art_gallery_hub.mapper.InvitationMapper;
import com.art_gallery_hub.model.Invitation;
import com.art_gallery_hub.repository.InvitationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InvitationService {

    private final InvitationRepository invitationRepository;
    private final InvitationMapper invitationMapper;

    private Invitation findInvitationOrThrow(Long invitationId) {
        return invitationRepository.findById(invitationId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Invitation not found with ID: " + invitationId));
    }

    @Transactional
    public List<InvitationResponse> getAllInvitations() {
        List<Invitation> invitations = invitationRepository.findAll();

        return invitations.stream()
                .map(response -> invitationMapper.toInvitationResponse(response))
                .toList();
    }

    @Transactional
    public InvitationResponse acceptInvitation(Long invitationId) {
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
    public InvitationResponse declineInvitation(Long invitationId) {
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

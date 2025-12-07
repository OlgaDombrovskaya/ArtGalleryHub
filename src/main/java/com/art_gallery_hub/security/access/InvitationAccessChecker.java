package com.art_gallery_hub.security.access;

import com.art_gallery_hub.repository.InvitationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component("invitationAccessChecker")
@RequiredArgsConstructor
public class InvitationAccessChecker {

    private final InvitationRepository invitationRepository;

    public boolean isOwner(Long invitationId, Authentication authentication) {
        String currentUsername = authentication.getName();

        return invitationRepository.findById(invitationId)
                .map(invitation -> invitation.getArtist().getUser().getUsername().equals(currentUsername))
                .orElse(false);
    }
}

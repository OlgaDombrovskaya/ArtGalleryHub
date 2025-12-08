package com.art_gallery_hub.security.access;

import com.art_gallery_hub.repository.ExhibitionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component("exhibitionAccessChecker")
@RequiredArgsConstructor
public class ExhibitionAccessChecker {

    private final ExhibitionRepository exhibitionRepository;

    public boolean isOwner(Long exhibitionId, Authentication authentication) {
        String currentUsername = authentication.getName();

        return exhibitionRepository.findById(exhibitionId)
                .map(exhibition -> exhibition.getCurator().getUsername().equals(currentUsername))
                .orElse(false);
    }
}

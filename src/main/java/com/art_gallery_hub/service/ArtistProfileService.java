package com.art_gallery_hub.service;

import com.art_gallery_hub.dto.artist_profile.ArtistProfileResponse;
import com.art_gallery_hub.dto.artist_profile.ArtistProfileUpdateRequest;
import com.art_gallery_hub.mapper.ArtistProfileMapper;
import com.art_gallery_hub.model.ArtistProfile;
import com.art_gallery_hub.model.User;
import com.art_gallery_hub.repository.ArtistProfileRepository;
import com.art_gallery_hub.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class ArtistProfileService {

    private final UserRepository userRepository;
    private final ArtistProfileRepository artistProfileRepository;
    private final ArtistProfileMapper artistProfileMapper;

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

    @Transactional
    public ArtistProfileResponse getArtistProfile(String username) {
        User user = findUserOrThrow(username);
        ArtistProfile artistProfile = findProfileOrThrow(user.getId());

        return artistProfileMapper.toArtistProfileResponse(artistProfile);
    }

    @Transactional
    public ArtistProfileResponse updateArtistProfile(
            String username,
            ArtistProfileUpdateRequest request
    ) {
        User user = findUserOrThrow(username);
        ArtistProfile existingArtistProfile = findProfileOrThrow(user.getId());

        existingArtistProfile.setDisplayName(request.displayName());
        existingArtistProfile.setBio(request.bio());
        existingArtistProfile.setWebsite(request.website());

        ArtistProfile updatedProfile = artistProfileRepository.save(existingArtistProfile);

        return artistProfileMapper.toArtistProfileResponse(updatedProfile);
    }

}

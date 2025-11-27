package com.art_gallery_hub.repository;

import com.art_gallery_hub.model.ArtistProfile;
import com.art_gallery_hub.model.Exhibition;
import com.art_gallery_hub.model.Invitation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InvitationRepository extends JpaRepository<Invitation, Long> {

    List<Invitation> findByExhibition(Long exhibitionId);

    List<Invitation> findByArtist(Long artistId);
}

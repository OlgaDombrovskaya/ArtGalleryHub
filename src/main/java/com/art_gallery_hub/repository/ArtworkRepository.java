package com.art_gallery_hub.repository;

import com.art_gallery_hub.model.Artwork;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ArtworkRepository extends JpaRepository<Artwork,Long> {

    List<Artwork> findByIsPublicTrue();

//    List<Artwork> findByUser_Id(Long id);

//    List<Artwork> findByArtist(String artist);

    List<Artwork> findByArtistId(Long artistId);
//    List<Artwork> findByArtist(Long artistId);

}

package com.art_gallery_hub.repository;

import com.art_gallery_hub.enums.Style;
import com.art_gallery_hub.model.Artwork;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ArtworkRepository extends JpaRepository<Artwork,Long> {

    List<Artwork> findByIsPublicTrue();

    List<Artwork> findByArtistId(Long artistId);

    @Query("SELECT a FROM Artwork a " +
            "WHERE a.isPublic = true " + // Базовое условие
            "AND (:style IS NULL OR a.style = :style) " +
            "AND (:year IS NULL OR a.year = :year) " +
            "AND (:artistName IS NULL OR a.artist.displayName = :artistName)")
    List<Artwork> findPublicArtworksFiltered(
            @Param("style") Style style,
            @Param("year") Integer year,
            @Param("artistName") String artistName
    );

}

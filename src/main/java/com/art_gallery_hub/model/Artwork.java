package com.art_gallery_hub.model;

import com.art_gallery_hub.enums.Style;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "artworks")
@Data
@NoArgsConstructor
public class Artwork {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // TODO
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "artist_profile_id", referencedColumnName = "id",
            nullable = false, unique = true)
    private ArtistProfile artist;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private int year;

    @Column(nullable = false)
    private Style style;

    @Column(nullable = false)
    private String imagePath;

    @Column(nullable = false)
    private boolean isPublic;

    @Column(nullable = false)
    private LocalDate createdAt;
}

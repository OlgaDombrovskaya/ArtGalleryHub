package com.art_gallery_hub.model;

import com.art_gallery_hub.enums.Style;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

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
    @JoinColumn(name = "artist_profile_id", nullable = false)
    private ArtistProfile artist;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private int year;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING) // so that JPA retains the name of the style
    // (for example, "IMPRESSIONISM") as a string, not a number (index)
    private Style style;

    @Column(name = "image_path", nullable = false)
    private String imagePath;

    @Column(name = "is_public", nullable = false)
    private boolean isPublic;

    @Column(name = "created_at", nullable = false)
    private LocalDate createdAt;
}

package com.art_gallery_hub.model;

import com.art_gallery_hub.enums.Style;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PreRemove;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "artworks")
@NoArgsConstructor
@Getter
@Setter
@ToString(exclude = {"artist", "reviews", "exhibitions"})
@EqualsAndHashCode(exclude = {"artist", "reviews", "exhibitions"})
public class Artwork {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "artist_profile_id", nullable = false)
    private ArtistProfile artist;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String description;

    @Column(name = "artwork_year", nullable = false)
    private int year;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING) // so that JPA retains the name of the style
    // (for example, "IMPRESSIONISM") as a string, not a number (index)
    private Style style;

    @Column(name = "image_path", nullable = false)
    private String imagePath;

    @Column(name = "is_public", nullable = false)
    private boolean isPublic = true;

    @Column(name = "created_at", nullable = false)
    @CreationTimestamp // Automatically sets the date at creation
    private LocalDateTime createdAt;

    //---------------Inverse relationship------------------
    @OneToMany(mappedBy = "artwork", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<Review> reviews = new HashSet<>();

    @ManyToMany(mappedBy = "artworks", fetch = FetchType.LAZY)
    private Set<Exhibition> exhibitions = new HashSet<>();

    //-----------------------------------------------------

    public Artwork(
            ArtistProfile artist,
            String title,
            String description,
            int year,
            Style style,
            String imagePath
    ) {
        this.artist = artist;
        this.title = title;
        this.description = description;
        this.year = year;
        this.style = style;
        this.imagePath = imagePath;
    }

    //-----------------------------------------------------
    @PreRemove
    private void preRemove() {
        for (Exhibition exhibition : new HashSet<>(this.exhibitions)) {
            exhibition.getArtworks().remove(this);
        }
        this.exhibitions.clear();
    }
}

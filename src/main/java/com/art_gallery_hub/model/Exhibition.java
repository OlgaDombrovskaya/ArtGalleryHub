package com.art_gallery_hub.model;

import com.art_gallery_hub.enums.ExhibitionStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "exhibitions")
@Data
@NoArgsConstructor
public class Exhibition {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String description;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "curator_id", nullable = false)
    private User curator;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING) // so that JPA retains the name of the style
    // (for example, "IMPRESSIONISM") as a string, not a number (index)
    private ExhibitionStatus status;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "exhibition_artworks",
            joinColumns = @JoinColumn(name = "exhibition_id"),
            inverseJoinColumns = @JoinColumn(name = "artwork_id")
    )
    private Set<Artwork> artworks = new HashSet<>();

    public void addArtwork(Artwork artwork) {
        this.artworks.add(artwork);
    }

    public Exhibition(String title, String description, LocalDate startDate, LocalDate endDate, User curator, ExhibitionStatus status) {
        this.title = title;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.curator = curator;
        this.status = status;
    }
}

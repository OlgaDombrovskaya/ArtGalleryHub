package com.art_gallery_hub.model;

import com.art_gallery_hub.enums.ExhibitionStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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

    @Column(nullable = false)
    private LocalDate startDate;

    @Column(nullable = false)
    private LocalDate endDate;

    // TODO
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "curator_id", referencedColumnName = "id",
            nullable = false, unique = true)
    private User curator;

    @Column(nullable = false)
    private ExhibitionStatus status;

    // TODO
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "exhibition_artworks",
            joinColumns = @JoinColumn(name = "exhibition_id"),
            inverseJoinColumns = @JoinColumn(name = "artwork_id")
    )
    private Set<Artwork> artworks = new HashSet<>();
}

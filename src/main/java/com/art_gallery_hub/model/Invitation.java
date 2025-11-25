package com.art_gallery_hub.model;

import com.art_gallery_hub.enums.InvitationStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "invitations")
@Data
@NoArgsConstructor
public class Invitation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // TODO
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exhibition_id", referencedColumnName = "id",
            nullable = false, unique = true)
    private Exhibition exhibition;

    // TODO
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "artist_profile_id", referencedColumnName = "id",
            nullable = false, unique = true)
    private ArtistProfile artist;

    @Column(nullable = false)
    private InvitationStatus status;

    @Column(nullable = false)
    private LocalDate createdAt;
}

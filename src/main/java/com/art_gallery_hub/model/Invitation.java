package com.art_gallery_hub.model;

import java.time.LocalDate;

public class Invitation {
    private Long id;
    private Exhibition exhibition;
    private ArtistProfile artist;
    private String status;
    private LocalDate createdAt;
}

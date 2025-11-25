package com.art_gallery_hub.model;

import java.time.LocalDate;

public class Artwork {
    private Long id;
    private ArtistProfile artist;
    private String title;
    private String description;
    private int year;
    private String style;
    private String imagePath;
    private boolean isPublic;
    private LocalDate createdAt;
}

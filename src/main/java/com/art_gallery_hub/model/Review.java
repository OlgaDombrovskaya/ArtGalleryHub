package com.art_gallery_hub.model;

import java.time.LocalDate;

public class Review {
    private Long id;
    private Artwork artwork;
    private User author;
    private int rating;
    private String comment;
    private LocalDate createdAt;
}

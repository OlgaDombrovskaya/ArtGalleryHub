package com.art_gallery_hub.mapper;

import com.art_gallery_hub.dto.artwork.ArtworkArtistResponse;
import com.art_gallery_hub.dto.artwork.ArtworkPublicDetailsResponse;
import com.art_gallery_hub.dto.artwork.ArtworkPublicSummaryResponse;
import com.art_gallery_hub.dto.review.ReviewResponse;
import com.art_gallery_hub.enums.Style;
import com.art_gallery_hub.model.ArtistProfile;
import com.art_gallery_hub.model.Artwork;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@WebMvcTest(controllers = ArtistProfileMapper.class)
class ArtworkMapperTest {

    private final ArtworkMapper mapper = new ArtworkMapper();

    private Artwork buildArtwork() {
        ArtistProfile artist = new ArtistProfile();
        artist.setDisplayName("Artist One");

        Artwork artwork = new Artwork();
        artwork.setId(1L);
        artwork.setArtist(artist);
        artwork.setTitle("Sunset");
        artwork.setYear(2020);
        artwork.setStyle(Style.ABSTRACT);
        artwork.setImagePath("/images/sunset.jpg");
        artwork.setDescription("Beautiful sunset");
        artwork.setPublic(true);
        return artwork;
    }

    @Test
    @DisplayName("toArtworkPublicSummaryResponse — корректно мапит базовые поля")
    void toArtworkPublicSummaryResponse_mapsBasicFields() {
        Artwork artwork = buildArtwork();

        ArtworkPublicSummaryResponse dto =
                mapper.toArtworkPublicSummaryResponse(artwork);

        assertEquals(1L, dto.id());
        assertEquals("Artist One", dto.artistDisplayName());
        assertEquals("Sunset", dto.title());
        assertEquals(2020, dto.year());
        assertEquals("/images/sunset.jpg", dto.imagePath());
    }

    @Test
    @DisplayName("toArtworkPublicDetailsResponse — мапит summary, описание и отзывы")
    void toArtworkPublicDetailsResponse_mapsNestedSummaryAndDescription() {
        Artwork artwork = buildArtwork();
        List<ReviewResponse> reviews = List.of();

        ArtworkPublicDetailsResponse dto =
                mapper.toArtworkPublicDetailsResponse(artwork, reviews);

        ArtworkPublicSummaryResponse summary = dto.summary();

        assertEquals(1L, summary.id());
        assertEquals("Sunset", summary.title());
        assertEquals("Beautiful sunset", dto.description());
        assertEquals(0, dto.reviews().size());
    }

    @Test
    @DisplayName("toArtworkArtistResponse — мапит данные для личного кабинета художника")
    void toArtworkArtistResponse_mapsForArtistView() {
        Artwork artwork = buildArtwork();

        ArtworkArtistResponse dto =
                mapper.toArtworkArtistResponse(artwork);

        ArtworkPublicSummaryResponse summary = dto.summary();

        assertEquals(1L, summary.id());
        assertEquals("Sunset", summary.title());
        assertEquals("Beautiful sunset", dto.description());
        assertTrue(dto.isPublic());
    }
}


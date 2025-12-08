package com.art_gallery_hub.controller;

import com.art_gallery_hub.config.SecurityConfig;
import com.art_gallery_hub.dto.artwork.ArtworkPublicSummaryResponse;
import com.art_gallery_hub.enums.Style;
import com.art_gallery_hub.service.ArtUserDetailsService;
import com.art_gallery_hub.service.ArtworkService;
import com.art_gallery_hub.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = VisitorController.class)
@Import(SecurityConfig.class)
class VisitorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ArtworkService artworkService;

    @MockitoBean
    private ReviewService reviewService;

    @MockitoBean
    private ArtUserDetailsService artUserDetailsService;

    @Test
    @DisplayName("GET /api/visitor/artworks возвращает список публичных работ для VISITOR, статус OK")
    @WithMockUser(username = "visitor1", roles = "VISITOR")
    void testGetAvailableArtworksSuccess() throws Exception {
        ArtworkPublicSummaryResponse artwork1 = new ArtworkPublicSummaryResponse(
                1L,
                "Artist One",
                "Street Art",
                2020,
                Style.PHOTOGRAPHY,
                "/images/street_art.jpg"
        );

        ArtworkPublicSummaryResponse artwork2 = new ArtworkPublicSummaryResponse(
                2L,
                "Artist Two",
                "Minimalism",
                2021,
                Style.MINIMALISM,
                "/images/minimalism.jpg"
        );

        given(artworkService.getAllPublicArtworksWithFilter(null, null, null))
                .willReturn(List.of(artwork1, artwork2));


        mockMvc.perform(get("/api/visitor/artworks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[?(@.title == 'Street Art')]").exists())
                .andExpect(jsonPath("$[?(@.title == 'Minimalism')]").exists());
    }

    @Test
    @DisplayName("GET /api/visitor/artworks — нет работ, статус OK, пустой список")
    @WithMockUser(username = "visitor1", roles = "VISITOR")
    void testGetAvailableArtworksEmpty() throws Exception {
        given(artworkService.getAllPublicArtworks())
                .willReturn(List.of());

        mockMvc.perform(get("/api/visitor/artworks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }
}
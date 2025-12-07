package com.art_gallery_hub.controller;

import com.art_gallery_hub.config.SecurityConfig;
import com.art_gallery_hub.dto.artwork.ArtworkPublicSummaryResponse;
import com.art_gallery_hub.enums.Style;
import com.art_gallery_hub.repository.ArtworkRepository;
import com.art_gallery_hub.service.ArtUserDetailsService;
import com.art_gallery_hub.service.ArtworkService;
import com.art_gallery_hub.service.ExhibitionService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = PublicController.class)
@Import(SecurityConfig.class)
class PublicControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ArtworkRepository artworkRepository;

    @MockitoBean
    private ArtworkService artworkService;

    @MockitoBean
    private ExhibitionService exhibitionService;

    @MockitoBean
    private ArtUserDetailsService artUserDetailsService;

    @Test
    @DisplayName("GET /api/public/info возвращает приветственное сообщение")
    void shouldReturnPublicInfoWithoutAuth() throws Exception {
        mockMvc.perform(get("/api/public/info"))
                .andExpect(status().isOk())
                .andExpect(content().string(
                        "Welcome to Art Gallery Hub - digital art gallery and exhibitions platform"
                ));
    }

    @Test
    @DisplayName("GET /api/public/artworks возвращает список 2-х публичных работ, статус OK")
    void testGetPublicArtworksSuccess() throws Exception {
        ArtworkPublicSummaryResponse artwork1 =
                new ArtworkPublicSummaryResponse(1L,
                        "artist1",
                        "Sunset",
                        2020,
                        Style.ABSTRACT,
                        "/images/sunset.jpg"
                );

        ArtworkPublicSummaryResponse artwork2 =
                new ArtworkPublicSummaryResponse(
                        2L,
                        "artist2",
                        "Portrait",
                        2021,
                        Style.PORTRAIT,
                        "/images/portrait.jpg"
                );

        given(artworkService.getAllArtworks())
                .willReturn(List.of(artwork1, artwork2));


        mockMvc.perform(get("/api/public/artworks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[?(@.title == 'Sunset')]").exists())
                .andExpect(jsonPath("$[?(@.title == 'Portrait')]").exists());
    }

    @Test
    @DisplayName("GET /api/public/artworks — 0 работ, статус OK")
    void testGetPublicArtworksEmpty() throws Exception {
        given(artworkRepository.findByIsPublicTrue())
                .willReturn(List.of());

        mockMvc.perform(get("/api/public/artworks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }
}

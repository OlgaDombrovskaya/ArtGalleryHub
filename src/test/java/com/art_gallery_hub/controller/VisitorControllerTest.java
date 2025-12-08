package com.art_gallery_hub.controller;

import com.art_gallery_hub.config.SecurityConfig;
import com.art_gallery_hub.repository.ArtworkRepository;
import com.art_gallery_hub.service.ArtUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import com.art_gallery_hub.model.Artwork;
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
    private ArtworkRepository artworkRepository;

    @MockitoBean
    private ArtUserDetailsService artUserDetailsService;

    @Test
    @DisplayName("GET /api/visitor/artworks возвращает список публичных работ для VISITOR, статус OK")
    @WithMockUser(username = "visitor1", roles = "VISITOR")
    void testGetAvailableArtworksSuccess() throws Exception {
        Artwork artwork1 = new Artwork();
        artwork1.setTitle("Street Art");

        Artwork artwork2 = new Artwork();
        artwork2.setTitle("Minimalism");

        given(artworkRepository.findByIsPublicTrue())
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
        given(artworkRepository.findByIsPublicTrue())
                .willReturn(List.of());

        mockMvc.perform(get("/api/visitor/artworks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }
}
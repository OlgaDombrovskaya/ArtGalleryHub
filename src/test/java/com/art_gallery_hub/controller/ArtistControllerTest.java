package com.art_gallery_hub.controller;

import com.art_gallery_hub.config.SecurityConfig;
import com.art_gallery_hub.service.ArtUserDetailsService;
import com.art_gallery_hub.service.ArtistProfileService;
import com.art_gallery_hub.service.ArtworkService;
import com.art_gallery_hub.service.InvitationService;
import com.art_gallery_hub.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@WebMvcTest(controllers = ArtistController.class)
@Import(SecurityConfig.class)
class ArtistControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ArtistProfileService artistProfileService;

    @MockitoBean
    private ArtworkService artworkService;

    @MockitoBean
    private InvitationService invitationService;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private ArtUserDetailsService artUserDetailsService;

    @Test
    @DisplayName("GET /api/artist/artworks/my — без авторизации 401 Unauthorized")
    void shouldReturn401ArtworksMyWithoutAuth() throws Exception {
        mockMvc.perform(get("/api/artist/artworks/my"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("GET /api/artist/artworks/my — авторизованный ARTIST получает 200 OK")
    @WithMockUser(username = "artist1", roles = "ARTIST")
    void shouldReturn200ArtworksMyWithAuthArtist() throws Exception {
        mockMvc.perform(get("/api/artist/artworks/my"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("GET /api/artist/artworks/my для артиста возвращает пустой список и статус OK")
    @WithMockUser(username = "artist1", roles = "ARTIST")
    void testGetMyArtworksEmpty() throws Exception {
        mockMvc.perform(get("/api/artist/artworks/my"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }
}

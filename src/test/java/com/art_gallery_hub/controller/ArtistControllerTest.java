package com.art_gallery_hub.controller;

import com.art_gallery_hub.repository.ArtistProfileRepository;
import com.art_gallery_hub.repository.ArtworkRepository;
import com.art_gallery_hub.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@WebMvcTest (controllers = ArtistController.class)
class ArtistControllerTest {

        @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ArtistProfileRepository artistProfileRepository;

    @MockitoBean
    private ArtworkRepository artworkRepository;

    @MockitoBean
    private UserRepository userRepository; //нужен, потому что ArtistController в конструкторе принимает UserRepository

    @Test
    @DisplayName("GET /api/artist/artworks/my — без авторизации 401 Unauthorized")
    void shouldReturn401ArtworksMyWithoutAuth() throws Exception{
        mockMvc.perform(get("/api/artist/artworks/my"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("GET /api/artist/artworks/my — авторизованный ARTIST получает 200 OK")
    @WithMockUser(username = "artist1", roles = "ARTIST")
    void shouldReturn200ArtworksMyWithAuthArtist() throws Exception{
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

package com.art_gallery_hub.controller;

import com.art_gallery_hub.config.SecurityConfig;
import com.art_gallery_hub.repository.ExhibitionRepository;
import com.art_gallery_hub.repository.UserRepository;
import com.art_gallery_hub.service.ArtUserDetailsService;
import com.art_gallery_hub.service.CuratorService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = CuratorController.class)
@Import(SecurityConfig.class)
class CuratorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ExhibitionRepository exhibitionRepository;

    @MockitoBean
    private UserRepository userRepository;

    @MockitoBean
    private CuratorService curatorService;

    @MockitoBean
    private ArtUserDetailsService artUserDetailsService;

    @Test
    @DisplayName("GET /api/curator/exhibitions/my — без авторизации 401 Unauthorized")
    void shouldReturn401WithoutAuth() throws Exception {
        mockMvc.perform(get("/api/curator/exhibitions/my"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("GET /api/curator/exhibitions/my — VISITOR получает 403 Forbidden")
    @WithMockUser(username = "visitor1", roles = "VISITOR")
    void shouldReturn403ForWrongRole() throws Exception {
        mockMvc.perform(get("/api/curator/exhibitions/my"))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("GET /api/curator/exhibitions/my для куратора возвращает пустой список и статус OK")
    @WithMockUser(username = "curator1", roles = "CURATOR")
    void testGetMyExhibitionsEmpty() throws Exception {
        mockMvc.perform(get("/api/curator/exhibitions/my"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }
}

package com.art_gallery_hub.controller;

import com.art_gallery_hub.config.SecurityConfig;
import com.art_gallery_hub.dto.user.UserAdminSummaryResponse;
import com.art_gallery_hub.repository.UserRepository;
import com.art_gallery_hub.service.ArtUserDetailsService;
import com.art_gallery_hub.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.List;
import java.util.Set;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = AdminController.class)
@Import(SecurityConfig.class)
class AdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserRepository userRepository;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private ArtUserDetailsService artUserDetailsService;

    @Test
    @DisplayName("GET /api/admin/users возвращает список из 2 пользователей, статус OK")
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testFindAllUsersSuccess() throws Exception {

        UserAdminSummaryResponse user1 = new UserAdminSummaryResponse(1L, "artist1", "artist1@example.com", Set.of("ARTIST"));
        UserAdminSummaryResponse user2 = new UserAdminSummaryResponse(2L, "visitor1", "visitor1@example.com", Set.of("VISITOR"));

        given(userService.getAllUsers())
                .willReturn(List.of(user1, user2));

        // если в контроллере @GetMapping("/user")
        mockMvc.perform(get("/api/admin/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[?(@.username == 'artist1')]").exists())
                .andExpect(jsonPath("$[?(@.username == 'visitor1')]").exists());
    }

    @Test
    @DisplayName("GET /api/admin/users, пользователей нет, статус OK, пустой список")
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testFindAllUsersEmpty() throws Exception {
        given(userRepository.findAll())
                .willReturn(List.of());

        mockMvc.perform(get("/api/admin/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    @DisplayName("GET /api/admin/users — пользователь без роли ADMIN получает 403 Forbidden")
    @WithMockUser(username = "visitor1", roles = {"VISITOR"})
    void testAccessDeniedForNonAdmin() throws Exception {

        mockMvc.perform(get("/api/admin/users"))
                .andExpect(status().isForbidden());
    }
}

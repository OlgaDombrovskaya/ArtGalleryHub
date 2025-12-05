//package com.art_gallery_hub.controller;
//
//import com.art_gallery_hub.model.User;
//import com.art_gallery_hub.repository.UserRepository;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.security.test.context.support.WithMockUser;
//import org.springframework.test.context.bean.override.mockito.MockitoBean;
//
//import java.util.List;
//
//import static org.mockito.BDDMockito.given;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@WebMvcTest(controllers = AdminController.class )
//public class AdminControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @MockitoBean
//    private UserRepository userRepository;
//
//    @Test
//    @DisplayName("GET /api/admin/user возвращает список из 2 пользователей, статус OK")
//    @WithMockUser(username = "admin", roles = {"ADMIN"})
//    void testFindAllUsersSuccess() throws Exception {
//
//        User user1 = new User();
//        user1.setUsername("artist1");
//
//        User user2 = new User();
//        user2.setUsername("visitor1");
//
//        given(userRepository.findAll())
//                .willReturn(List.of(user1, user2));
//
//        // если в контроллере @GetMapping("/user")
//        mockMvc.perform(get("/api/admin/user"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.length()").value(2))
//                .andExpect(jsonPath("$[0].username").value("artist1"))
//                .andExpect(jsonPath("$[1].username").value("visitor1"));
//    }
//
//    @Test
//    @DisplayName("GET /api/admin/user, пользователей нет, статус OK, пустой список")
//    @WithMockUser(username = "admin", roles = {"ADMIN"})
//    void testFindAllUsersEmpty() throws Exception {
//        given(userRepository.findAll())
//                .willReturn(List.of());
//
//        mockMvc.perform(get("/api/admin/user"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.length()").value(0));
//    }
//}

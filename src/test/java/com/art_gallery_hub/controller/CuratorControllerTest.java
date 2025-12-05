//package com.art_gallery_hub.controller;
//
//import com.art_gallery_hub.repository.ExhibitionRepository;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.security.test.context.support.WithMockUser;
//import org.springframework.test.context.bean.override.mockito.MockitoBean;
//
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@WebMvcTest (controllers = CuratorController.class)
//public class CuratorControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @MockitoBean
//    private ExhibitionRepository exhibitionRepository;
//
//    @Test
//    @DisplayName("GET /api/curator/exhibitions/my для куратора возвращает пустой список и статус OK")
//    @WithMockUser(username = "curator1", roles = "CURATOR")
//    void testGetMyExhibitionsEmpty() throws Exception {
//        mockMvc.perform(get("/api/curator/exhibitions/my"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.length()").value(0));
//    }
//
//}

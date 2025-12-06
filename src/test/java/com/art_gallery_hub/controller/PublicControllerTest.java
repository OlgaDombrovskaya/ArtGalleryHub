//package com.art_gallery_hub.controller;
//
//import com.art_gallery_hub.model.Artwork;
//import com.art_gallery_hub.repository.ArtworkRepository;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.context.bean.override.mockito.MockitoBean;
//
//import java.util.List;
//
//import static org.mockito.BDDMockito.given;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@WebMvcTest(controllers = PublicController.class)
//public class PublicControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @MockitoBean
//    private ArtworkRepository artworkRepository;
//
//    @Test
//    @DisplayName("GET /api/public/info возвращает приветственное сообщение")
//    void shouldReturnPublicInfoWithoutAuth() throws Exception {
//        mockMvc.perform(get("/api/public/info"))
//                .andExpect(status().isOk())
//                .andExpect(content().string(
//                        "Welcome to Art Gallery Hub - digital art gallery and exhibitions platform"
//                ));
//    }
//    @Test
//    @DisplayName("GET /api/public/artworks возвращает список публичных работ, статус OK")
//    void testGetPublicArtworksSuccess() throws Exception {
//        Artwork artwork1 = new Artwork();
//        artwork1.setTitle("Sunset");
//
//        Artwork artwork2 = new Artwork();
//        artwork2.setTitle("Portrait");
//
//        given(artworkRepository.findByIsPublicTrue())
//                .willReturn(List.of(artwork1, artwork2));
//
//        mockMvc.perform(get("/api/public/artworks"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.length()").value(2))
//                .andExpect(jsonPath("$[0].title").value("Sunset"))
//                .andExpect(jsonPath("$[1].title").value("Portrait"));
//    }
//}

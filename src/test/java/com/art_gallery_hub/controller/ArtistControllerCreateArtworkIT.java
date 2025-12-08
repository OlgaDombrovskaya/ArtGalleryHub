package com.art_gallery_hub.controller;

import com.art_gallery_hub.dto.artwork.ArtworkCreateRequest;
import com.art_gallery_hub.enums.Style;
import com.art_gallery_hub.model.Artwork;
import com.art_gallery_hub.repository.ArtworkRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ArtistControllerCreateArtworkIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ArtworkRepository artworkRepository;

    @Test
    @DisplayName("POST /api/artist/artworks — ARTIST создаёт Artwork (multipart), файл пустой, запись сохраняется в БД")
    @Sql(scripts = {
            "classpath:sql/clear.sql",
            "classpath:sql/seed_users.sql",
            // если есть скрипт с профилями художников — добавь и его:
            // "classpath:sql/seed_artist_profiles.sql",
            "classpath:sql/seed_public_artworks.sql"
    })
    @WithMockUser(username = "artist1", roles = "ARTIST")
    void createArtwork_multipart_savesToDb() throws Exception {

        long countBefore = artworkRepository.count();

        // DTO как в реальном запросе
        ArtworkCreateRequest dto = new ArtworkCreateRequest(
                "Test Artwork",
                "Created in test",
                2025,
                Style.ABSTRACT
        );

        String json = objectMapper.writeValueAsString(dto);

        // part "artwork" -> @RequestPart("artwork")
        MockMultipartFile artworkPart = new MockMultipartFile(
                "artwork",              // имя part — как в контроллере
                "artwork.json",
                APPLICATION_JSON_VALUE,
                json.getBytes(StandardCharsets.UTF_8)
        );

        // part "image" -> @RequestPart("image"), пустой файл
        MockMultipartFile imagePart = new MockMultipartFile(
                "image",
                "test.png",
                "image/png",
                new byte[0]
        );

        // отправляем multipart POST на /api/artist/artworks
        mockMvc.perform(
                        multipart("/api/artist/artworks")
                                .file(artworkPart)
                                .file(imagePart)
                                .contentType(MULTIPART_FORM_DATA_VALUE)
                                .characterEncoding("UTF-8")
                )
                .andExpect(status().isCreated());   // 201 CREATED

        // Проверяем, что запись появилась в БД
        List<Artwork> artworks = artworkRepository.findAll();

        assertThat(artworks.stream()
                .anyMatch(a -> "Test Artwork".equals(a.getTitle())))
                .isTrue();

        assertThat(artworkRepository.count())
                .isEqualTo(countBefore + 1);
    }
}
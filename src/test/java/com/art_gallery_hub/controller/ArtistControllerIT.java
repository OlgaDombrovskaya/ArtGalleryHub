package com.art_gallery_hub.controller;

import com.art_gallery_hub.dto.artwork.ArtworkArtistResponse;
import com.art_gallery_hub.dto.artwork.ArtworkCreateRequest;
import com.art_gallery_hub.enums.Style;
import com.art_gallery_hub.repository.ArtworkRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.http.*;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class ArtistControllerIT {

    @LocalServerPort
    private int port;

    @Autowired
    TestRestTemplate restTemplate;

    @Autowired
    private ArtworkRepository artworkRepository;

    private String url(String path) {
        return "http://localhost:" + port + path;
    }

    @Test
    @DisplayName("GET /api/artist/artworks/my — без авторизации возвращает статус 401/403")
    void myArtworks_shouldReturnUnauthorizedWithoutAuth() {
        ResponseEntity<String> response =
                restTemplate.getForEntity(
                        url("/api/artist/artworks/my"),
                        String.class
                );

        assertThat(response.getStatusCode())
                .isIn(HttpStatus.UNAUTHORIZED, HttpStatus.FORBIDDEN);
    }

    @Test
    @DisplayName("GET /api/artist/artworks/my — с авторизованной ролью ARTIST ожидаем 200 OK и JSON-список")
    @Sql(scripts = {"classpath:sql/clear.sql", "classpath:sql/seed_users.sql",
            "classpath:sql/seed_public_artworks.sql"
    })
    void myArtworks_shouldReturnArtworksForArtist() {

        ResponseEntity<ArtworkArtistResponse[]> response =
                restTemplate.withBasicAuth
                                ("artist1", "artist123")
                        .getForEntity(
                                url("/api/artist/artworks/my"),

                                ArtworkArtistResponse[].class
                        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        ArtworkArtistResponse[] body = response.getBody();
        assertThat(body).isNotNull();

        List<ArtworkArtistResponse> artworks = Arrays.asList(response.getBody());
        assertThat(artworks.size()).isEqualTo(2);

        assertThat(artworks.get(0).summary()).isNotNull();
        assertThat(artworks.get(0).summary().title()).isEqualTo("Sunset");
    }

//    @Test
//    @DisplayName("POST /api/artist/artworks — с ролью ARTIST создаёт Artwork без файла и сохраняет в БД")
//    @Sql(scripts = {"classpath:sql/clear.sql", "classpath:sql/seed_users.sql",
//            "classpath:sql/seed_public_artworks.sql"
//    })
//    void createArtwork_shouldSaveArtworkWithoutRealFile(){
//        long countBefore = artworkRepository.count();
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_JSON);
//        ArtworkCreateRequest testArtwork = new ArtworkCreateRequest(
//                "Test Artwork",
//                "Created in integration test",
//                2025,
//                Style.ABSTRACT
//        );
//
//        HttpEntity<ArtworkCreateRequest> request = new HttpEntity<>(testArtwork, headers);
//
//        ResponseEntity<ArtworkArtistResponse> response =
//                restTemplate.withBasicAuth("artist1", "artist123")
//                        .exchange(
//                                url("/api/artist/artworks"),
//                                HttpMethod.POST,
//                                request,
//                                ArtworkArtistResponse.class
//                        );
//
//        assertEquals(HttpStatus.CREATED, response.getStatusCode(), "Unexpected http status");
//
//        ArtworkArtistResponse savedArtwork = response.getBody();
//
//        assertNotNull(savedArtwork, "Saved artwork should not be null");
//        assertNotNull(savedArtwork.summary(), "Saved artwork summary should not be null");
//        assertEquals("Test Artwork", savedArtwork.summary().title(), "Saved artwork title is incorrect");
//
//        // Проверяем, что запись реально появилась в БД
//        long countAfter = artworkRepository.count();
//        assertEquals(countBefore + 1, countAfter, "Artwork was not saved in DB");
//    }
}
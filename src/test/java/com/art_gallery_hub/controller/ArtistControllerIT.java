package com.art_gallery_hub.controller;

import com.art_gallery_hub.model.Artwork;
import com.art_gallery_hub.repository.RoleRepository;
import com.art_gallery_hub.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Transactional
class ArtistControllerIT {

    @LocalServerPort
    private int port;

    // создала вручную т.к. @Autowired TestRestTemplate в версии 4.0 почему-то не работает
    private final TestRestTemplate restTemplate = new TestRestTemplate();

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

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
                .as("Ожидаем, что без авторизации доступ запрещён")
                .isIn(HttpStatus.UNAUTHORIZED, HttpStatus.FORBIDDEN);
    }

    @Test
    @DisplayName("GET /api/artist/artworks/my — с ролью ARTIST- 200 OK и JSON-список")
    void myArtworks_shouldReturnArtworksForArtist() {
        TestRestTemplate artistClient =
                restTemplate.withBasicAuth("artist1", "artist123");

        ResponseEntity<Artwork[]> response =
                artistClient.getForEntity(
                        url("/api/artist/artworks/my"),
                        Artwork[].class
                );

        assertThat(response.getStatusCode())
                .as("Ожидаем 200 OK для авторизованного ARTIST")
                .isEqualTo(HttpStatus.OK);

        Artwork[] body = response.getBody();
        assertThat(body).isNotNull();

        assertThat(body.length)
                .as("Список работ не должен быть null")
                .isGreaterThanOrEqualTo(0);
    }
}
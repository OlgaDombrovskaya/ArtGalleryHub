package com.art_gallery_hub.controller;

import com.art_gallery_hub.model.Artwork;
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


import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class PublicControllerIT {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    private String url(String path) {
        return "http://localhost:" + port + path;
    }

    @Test
    @DisplayName("GET /api/public/artworks — возвращает список публичных работ, статус OK")
    @Sql(scripts = {"classpath:sql/clear.sql", "classpath:sql/seed_users.sql", "classpath:sql/seed_public_artworks.sql"})
    void shouldReturnPublicArtworks() {

        ResponseEntity<Artwork[]> response =
                restTemplate.getForEntity(url("/api/public/artworks"), Artwork[].class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        Artwork[] body = response.getBody();
        assertThat(body).isNotNull();

        List<Artwork> artworks = Arrays.asList(body);

        assertThat(artworks.size()).isEqualTo(2);
        assertThat(artworks.get(0).getTitle()).isEqualTo("Sunset");
        assertThat(artworks.get(1).getTitle()).isEqualTo("Portrait");
    }
}
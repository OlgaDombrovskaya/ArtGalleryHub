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
class VisitorControllerIT {

    @LocalServerPort
    private int port;

    @Autowired
    TestRestTemplate restTemplate;

    private String url(String path) {
        return "http://localhost:" + port + path;
    }

    @Test
    @DisplayName("GET /api/visitor/artworks — публичные работы для VISITOR, статус OK")
    @Sql(scripts = {"classpath:sql/clear.sql", "classpath:sql/seed_artworks.sql"})
    void shouldReturnArtworksForVisitor() {
        ResponseEntity<Artwork[]> response =
                restTemplate.getForEntity(url("/api/visitor/artworks"), Artwork[].class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        List<Artwork> artworks = Arrays.asList(response.getBody());
        assertThat(artworks.size()).isEqualTo(2);
    }
}
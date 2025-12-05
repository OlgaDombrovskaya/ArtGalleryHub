package com.art_gallery_hub.controller;

import com.art_gallery_hub.model.Exhibition;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class CuratorControllerIT {

    @LocalServerPort
    private int port;

    @Autowired
    TestRestTemplate restTemplate;

    private String url(String path) {
        return "http://localhost:" + port + path;
    }

    @Test
    @DisplayName("GET /api/curator/exhibitions/my — пока возвращает пустой список, статус OK")
    void shouldReturnEmptyListForMyExhibitions() {
        ResponseEntity<Exhibition[]> response =
                restTemplate.getForEntity(url("/api/curator/exhibitions/my"), Exhibition[].class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        List<Exhibition> exhibitions = Arrays.asList(response.getBody());
        assertThat(exhibitions.size()).isEqualTo(0);
    }
}
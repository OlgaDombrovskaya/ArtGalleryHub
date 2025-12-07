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
import org.springframework.test.context.jdbc.Sql;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.List;

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
    @DisplayName("GET /api/curator/exhibitions/my — без авторизации → 401/403")
    void shouldReturnUnauthorizedWithoutAuth() {

        ResponseEntity<String> response =
                restTemplate.getForEntity(url("/api/curator/exhibitions/my"), String.class);

        assertThat(response.getStatusCode())
                .isIn(HttpStatus.UNAUTHORIZED, HttpStatus.FORBIDDEN);
    }

    @Test
    @DisplayName("GET /api/curator/exhibitions/my —  возвращает пустой список для куратора, статус OK")
    @Sql(scripts = {"classpath:sql/clear.sql", "classpath:sql/seed_users.sql"})
    void shouldReturnEmptyListForCurator() {

        // Клиент с ролью CURATOR (логин/пароль должны совпадать с seed_users.sql)
        TestRestTemplate curatorClient =
                restTemplate.withBasicAuth("curator1", "curator123");

        ResponseEntity<Exhibition[]> response =
                curatorClient.getForEntity(
                        url("/api/curator/exhibitions/my"),
                        Exhibition[].class
                );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        Exhibition[] body = response.getBody();
        assertThat(body).isNotNull();

        List<Exhibition> exhibitions = Arrays.asList(body);
        assertThat(exhibitions).isEmpty();
    }
}
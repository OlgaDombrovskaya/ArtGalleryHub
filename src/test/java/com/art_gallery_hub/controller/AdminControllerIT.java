package com.art_gallery_hub.controller;

import com.art_gallery_hub.model.User;
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
class AdminControllerIT {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    private String url(String path) {
        return "http://localhost:" + port + path;
    }

    @Test
    @DisplayName("GET /api/admin/user — возвращает список пользователей, статус OK")
    @Sql(scripts = {"classpath:sql/clear.sql", "classpath:sql/seed_users.sql"})
    void shouldReturnAllUsers() {
        ResponseEntity<User[]> response =
                restTemplate.getForEntity(url("/api/admin/user"), User[].class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        List<User> users = Arrays.asList(response.getBody());
        // тут просто пример проверки размера
        assertThat(users.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("GET /api/admin/user — нет пользователей, возвращается пустой список и статус OK")
    @Sql(scripts = {"classpath:sql/clear.sql"})
    void shouldReturnEmptyListWhenNoUsers() {
        ResponseEntity<User[]> response =
                restTemplate.getForEntity(url("/api/admin/user"), User[].class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        List<User> users = Arrays.asList(response.getBody());
        assertThat(users.size()).isEqualTo(0);
    }
}

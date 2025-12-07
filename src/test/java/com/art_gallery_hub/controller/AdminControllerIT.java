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

import static org.assertj.core.api.Assertions.assertThat;

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
    @DisplayName("GET /api/admin/users — возвращает список пользователей, статус OK для ADMIN")
    @Sql(scripts = {"classpath:sql/clear.sql", "classpath:sql/seed_users.sql"})
    void shouldReturnAllUsers() {
        // КЛИЕНТ ПОД АДМИНОМ (логин/пароль — как в seed_users.sql)
        TestRestTemplate adminClient =
                restTemplate.withBasicAuth("admin1", "admin123");

        ResponseEntity<User[]> response =
                adminClient.getForEntity(url("/api/admin/users"), User[].class);

        assertThat(response.getStatusCode())
                .isEqualTo(HttpStatus.OK);

        User[] body = response.getBody();
        assertThat(body).isNotNull();

        List<User> users = Arrays.asList(body);

        assertThat(users.size()).isEqualTo(4);
    }

    @Test
    @DisplayName("GET /api/admin/users — без авторизации: статус 401 UNAUTHORIZED")
    @Sql(scripts = {"classpath:sql/clear.sql"})
    void shouldReturnUnauthorizedWhenNoAuth() {

        ResponseEntity<String> response =
                restTemplate.getForEntity(url("/api/admin/users"), String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    @DisplayName("GET /api/admin/users — VISITOR не имеет доступа, статус 401 (пока пользователь не находится)")
    @Sql(scripts = {"classpath:sql/clear.sql", "classpath:sql/seed_users.sql"})
    void testForbiddenForVisitor() {

        TestRestTemplate visitorClient =
                restTemplate.withBasicAuth("visitor1", "visitor123");

        ResponseEntity<String> response =
                visitorClient.getForEntity(url("/api/admin/users"), String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }
}
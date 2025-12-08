package com.art_gallery_hub.controller;

import com.art_gallery_hub.dto.user.UserAdminSummaryResponse;
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

        ResponseEntity<UserAdminSummaryResponse[]> response = restTemplate.withBasicAuth
                        ("admin1", "admin123")
                .getForEntity(url("/api/admin/users"), UserAdminSummaryResponse[].class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        List<UserAdminSummaryResponse> users = Arrays.asList(response.getBody());
        assertThat(users.size()).isEqualTo(4);
        assertThat(users.get(0).username()).isEqualTo("admin1");
    }
//TODO
    // этот вариант нужен только
    //если не отключаем в SecutityConfig   http.formLogin(Customizer.withDefaults());

//    @Test
//    @DisplayName("GET /api/admin/users — без авторизации происходит редирект на страницу логина")
//    @Sql(scripts = {"classpath:sql/clear.sql"})
//    void shouldReturnUnauthorizedWhenNoAuth() {
//
//        ResponseEntity<String> response =
//                restTemplate.getForEntity(url("/api/admin/users"), String.class);
//
//        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
//        assertThat(response.getBody())
//                .containsIgnoringCase("login");
//    }

    @Test
    @DisplayName("GET /api/admin/users — без авторизации возвращает статус 401/403")
    @Sql(scripts = {"classpath:sql/clear.sql"})
    void shouldReturnUnauthorizedWhenNoAuthWithAdmin() {

        ResponseEntity<String> response =
                restTemplate.getForEntity(
                        url("/api/admin/users"),
                        String.class
                );

        assertThat(response.getStatusCode())
                .isIn(HttpStatus.UNAUTHORIZED, HttpStatus.FORBIDDEN);
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
    @DisplayName("GET /api/admin/users — VISITOR не имеет доступа, статус 403 (пока пользователь не находится)")
    @Sql(scripts = {"classpath:sql/clear.sql", "classpath:sql/seed_users.sql"})
    void testForbiddenForVisitor() {

        ResponseEntity<String> response = restTemplate.withBasicAuth
                        ("visitor1", "visitor123")
                .getForEntity(url("/api/admin/users"), String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }
}

//package com.art_gallery_hub.controller;
//
//import com.art_gallery_hub.model.Artwork;
//import jakarta.transaction.Transactional;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.web.client.TestRestTemplate;
//import org.springframework.boot.test.web.server.LocalServerPort;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.test.context.jdbc.Sql;
//
//import java.util.Arrays;
//import java.util.List;
//
//import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
//
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//@ActiveProfiles("test")
//@Transactional
//class ArtistControllerIT {
//
//    @LocalServerPort
//    private int port;
//
//    @Autowired
//    TestRestTemplate restTemplate;
//
//    private String url(String path) {
//        return "http://localhost:" + port + path;
//    }
//
//    @Test
//    @DisplayName("GET /api/artist/artworks/my — без авторизации возвращает статус 401/403")
//    void myArtworks_shouldReturnUnauthorizedWithoutAuth() {
//        ResponseEntity<String> response =
//                restTemplate.getForEntity(
//                        url("/api/artist/artworks/my"),
//                        String.class
//                );
//
//        assertThat(response.getStatusCode())
//                .as("Ожидаем, что без авторизации доступ запрещён")
//                .isIn(HttpStatus.UNAUTHORIZED, HttpStatus.FORBIDDEN);
//    }
//
//    @Test
//    @DisplayName("GET /api/artist/artworks/my — с ролью ARTIST- 200 OK и JSON-список")
//    @Sql(scripts = {
//            "classpath:sql/clear.sql",
//            "classpath:sql/seed_users.sql",
//            "classpath:sql/seed_artworks.sql"
//    })
//    void myArtworks_shouldReturnArtworksForArtist() {
//        TestRestTemplate artistClient =
//                restTemplate.withBasicAuth("artist1", "artist123");
//
//        ResponseEntity<Artwork[]> response =
//                artistClient.getForEntity(
//                        url("/api/artist/artworks/my"),
//                        Artwork[].class
//                );
//
//        assertThat(response.getStatusCode())
//                .as("Ожидаем 200 OK для авторизованного ARTIST")
//                .isEqualTo(HttpStatus.OK);
//
//        Artwork[] body = response.getBody();
//        assertThat(body).isNotNull();
//
//        List<Artwork> artworks = Arrays.asList(body);
//        assertThat(artworks.size()).isEqualTo(2);
//        assertThat(artworks.get(0).getTitle()).isEqualTo("Sunset");
//    }
//}
package com.art_gallery_hub.controller;

import com.art_gallery_hub.dto.exhibition.ExhibitionCreateOrUpdateRequest;
import com.art_gallery_hub.dto.exhibition.ExhibitionCuratorSummaryResponse;
import com.art_gallery_hub.dto.exhibition.ExhibitionStatusUpdateRequest;
import com.art_gallery_hub.enums.ExhibitionStatus;
import com.art_gallery_hub.model.Exhibition;
import com.art_gallery_hub.repository.ExhibitionRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class CuratorControllerIT {

    @LocalServerPort
    private int port;

    @Autowired
    TestRestTemplate restTemplate;

    @Autowired
    ExhibitionRepository exhibitionRepository;

    private String url(String path) {
        return "http://localhost:" + port + path;
    }

    @Test
    @DisplayName("GET /api/curator/exhibitions/my — без авторизации- 401/403")
    void shouldReturnUnauthorizedWithoutAuthWithCurator() {

        ResponseEntity<String> response =
                restTemplate.getForEntity(url("/api/curator/exhibitions/my"), String.class);

        assertThat(response.getStatusCode())
                .isIn(HttpStatus.UNAUTHORIZED, HttpStatus.FORBIDDEN);
    }

    @Test
    @DisplayName("GET /api/curator/exhibitions/my —  возвращает пустой список для куратора, статус OK")
    @Sql(scripts = {"classpath:sql/clear.sql", "classpath:sql/seed_users.sql"})
    void shouldReturnEmptyListForCurator() {

        ResponseEntity<ExhibitionCuratorSummaryResponse[]> response =
                restTemplate.withBasicAuth
                                ("curator1", "curator123")
                        .getForEntity(url("/api/curator/exhibitions/my"),
                                ExhibitionCuratorSummaryResponse[].class
                        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        ExhibitionCuratorSummaryResponse[] body = response.getBody();
        assertThat(body).isNotNull();

        List<ExhibitionCuratorSummaryResponse> exhibitions = Arrays.asList(body);
        assertThat(exhibitions).isEmpty();
    }

//    @Test
//    @DisplayName("CURATOR создаёт выставку и меняет статус на OPEN")
//    @Sql(scripts = {"classpath:sql/clear.sql", "classpath:sql/seed_users.sql"})
//    void curatorCreatesAndOpensExhibition() {
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_JSON);
//
//        ExhibitionCreateOrUpdateRequest createRequest = new ExhibitionCreateOrUpdateRequest(
//                "New Exhibition",
//                "About modern art",
//                LocalDate.of(2025, 1, 1),
//                LocalDate.of(2025, 1, 31)
//        );
//        HttpEntity<ExhibitionCreateOrUpdateRequest> createHttpEntity =
//                new HttpEntity<>(createRequest, headers);
//
//        ResponseEntity<ExhibitionCuratorSummaryResponse> createResponse =
//                restTemplate.withBasicAuth("curator1", "curator123")
//                        .exchange(
//                                url("/api/curator/exhibitions"),
//                                HttpMethod.POST,
//                                createHttpEntity,
//                                ExhibitionCuratorSummaryResponse.class
//                        );
//
//        assertThat(createResponse.getStatusCode())
//                .as("Ожидаем 201 CREATED при создании выставки")
//                .isEqualTo(HttpStatus.CREATED);
//
//        ExhibitionCuratorSummaryResponse created = createResponse.getBody();
//        assertThat(created).isNotNull();
//        Long exhibitionId = created.id();
//        assertThat(exhibitionId).isNotNull();
//
//        // 2. Меняем статус на OPEN
//        ExhibitionStatusUpdateRequest statusRequest =
//                new ExhibitionStatusUpdateRequest(ExhibitionStatus.OPEN);
//
//        HttpEntity<ExhibitionStatusUpdateRequest> statusHttpEntity =
//                new HttpEntity<>(statusRequest, headers);
//
//        ResponseEntity<Void> statusResponse =
//                restTemplate.withBasicAuth("curator1", "curator123")
//                        .exchange(
//                                url("/api/curator/exhibitions/" + exhibitionId + "/status"),
//                                HttpMethod.PATCH,
//                                statusHttpEntity,
//                                Void.class
//                        );
//
//        assertThat(statusResponse.getStatusCode())
//                .as("Ожидаем 200 OK при смене статуса")
//                .isEqualTo(HttpStatus.OK);
//
//        // 3. Проверяем в БД, что статус реально стал OPEN
//        Exhibition exhibition = exhibitionRepository.findById(exhibitionId)
//                .orElseThrow(() -> new IllegalStateException("Exhibition not found in DB"));
//
//        assertThat(exhibition.getStatus())
//                .as("Статус выставки в БД должен быть OPEN")
//                .isEqualTo(ExhibitionStatus.OPEN);
//    }
}
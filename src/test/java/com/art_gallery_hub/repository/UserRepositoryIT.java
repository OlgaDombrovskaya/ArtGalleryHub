package com.art_gallery_hub.repository;

import com.art_gallery_hub.model.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Sql(
        scripts = {"classpath:sql/clear.sql", "classpath:sql/seed_users.sql"},
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
)
class UserRepositoryIT {

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("findByUsername должен находить пользователя, вставленного через SQL")
    void findByUsername_shouldReturnUserFromSql() {

        Optional<User> userOpt = userRepository.findByUsername("artist1");

        assertThat(userOpt)
                .as("Пользователь artist1 должен быть в БД после seed_users.sql")
                .isPresent();

        User user = userOpt.get();
        assertThat(user.getEmail()).isEqualTo("artist1@example.com");
        assertThat(user.isEnabled()).isTrue();
    }

    @Test
    @DisplayName("findByEmail должен находить пользователя по email")
    void findByEmail_shouldReturnUserFromSql() {

        Optional<User> userOpt = userRepository.findByEmail("visitor1@example.com");

        assertThat(userOpt).isPresent();
        assertThat(userOpt.get().getUsername()).isEqualTo("visitor1");
    }
}
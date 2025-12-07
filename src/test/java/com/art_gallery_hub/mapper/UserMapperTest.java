package com.art_gallery_hub.mapper;

import com.art_gallery_hub.dto.user.UserAdminSummaryResponse;
import com.art_gallery_hub.dto.user.UserRegistrationResponse;
import com.art_gallery_hub.enums.RoleStatus;
import com.art_gallery_hub.model.Role;
import com.art_gallery_hub.model.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@WebMvcTest(controllers = UserMapper.class)
class UserMapperTest {

    private final UserMapper mapper = new UserMapper();

    private Role buildRole(RoleStatus status) {
        Role role = new Role();
        role.setName(status);
        return role;
    }

    private User buildUserWithRoles() {
        User user = new User();
        user.setId(10L);
        user.setUsername("artist1");
        user.setEmail("artist1@example.com");
        user.setPassword("encoded-pass");

        Role adminRole = buildRole(RoleStatus.ROLE_ADMIN);
        Role visitorRole = buildRole(RoleStatus.ROLE_VISITOR);

        user.setRoles(Set.of(adminRole, visitorRole));
        return user;
    }

    @Test
    @DisplayName("toEntity — создаёт User с переданными полями и ролями")
    void toEntity_createsUserWithGivenFields() {
        String username = "newUser";
        String email = "newuser@example.com";
        String encodedPassword = "encoded-pass-123";

        Role artistRole = buildRole(RoleStatus.ROLE_ARTIST);
        Set<Role> roles = Set.of(artistRole);

        User user = mapper.toEntity(username, email, encodedPassword, roles);

        assertNull(user.getId(), "id нового пользователя должен быть null до сохранения");
        assertEquals(username, user.getUsername());
        assertEquals(email, user.getEmail());
        assertEquals(encodedPassword, user.getPassword());
        assertEquals(roles, user.getRoles());
    }

    @Test
    @DisplayName("toUserRegistrationResponse — мапит id, username и email")
    void toUserRegistrationResponse_mapsBasicFields() {
        User user = new User();
        user.setId(5L);
        user.setUsername("visitor1");
        user.setEmail("visitor1@example.com");
        user.setPassword("encoded");

        UserRegistrationResponse dto = mapper.toUserRegistrationResponse(user);

        assertEquals(5L, dto.id());
        assertEquals("visitor1", dto.username());
        assertEquals("visitor1@example.com", dto.email());
    }

    @Test
    @DisplayName("toUserAdminSummaryResponse — мапит роли в Set<String> и основные поля")
    void toUserAdminSummaryResponse_mapsRolesToStringSet() {
        User user = buildUserWithRoles();

        UserAdminSummaryResponse dto = mapper.toUserAdminSummaryResponse(user);

        assertEquals(10L, dto.id());
        assertEquals("artist1", dto.username());
        assertEquals("artist1@example.com", dto.email());

        Set<String> roleNames = dto.roles();

        assertEquals(2, roleNames.size());
        assertTrue(roleNames.contains("ROLE_ADMIN"));
        assertTrue(roleNames.contains("ROLE_VISITOR"));
    }
}

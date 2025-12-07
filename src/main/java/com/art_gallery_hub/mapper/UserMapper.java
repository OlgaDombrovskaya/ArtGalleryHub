package com.art_gallery_hub.mapper;

import com.art_gallery_hub.dto.user.UserRegistrationRequest;
import com.art_gallery_hub.dto.user.UserAdminSummaryResponse;
import com.art_gallery_hub.dto.user.UserRegistrationResponse;
import com.art_gallery_hub.model.Role;
import com.art_gallery_hub.model.User;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class UserMapper {

    public User toEntity(
            UserRegistrationRequest userRegistrationRequest,
            String encodedPassword,
            Role role
    ) {
        Set<Role> roles = new HashSet<>();
        roles.add(role);

        return new User(
                userRegistrationRequest.username(),
                userRegistrationRequest.email(),
                encodedPassword,
                roles
        );
    }

    public UserRegistrationResponse toUserRegistrationResponse(User user) {
        return new UserRegistrationResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail()
        );
    }

    public UserAdminSummaryResponse toUserAdminSummaryResponse(User user) {
        return new UserAdminSummaryResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getRoles()
                        .stream()
                        .map(role -> role.getName().name())
                        .collect(Collectors.toSet())
        );
    }
}

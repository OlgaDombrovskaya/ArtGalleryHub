package com.art_gallery_hub.service;

import com.art_gallery_hub.dto.user.UserAdminCreationRequest;
import com.art_gallery_hub.dto.user.UserRegistrationRequest;
import com.art_gallery_hub.dto.user.UserAdminSummaryResponse;
import com.art_gallery_hub.dto.user.UserRegistrationResponse;
import com.art_gallery_hub.dto.user.UserRoleUpdateRequest;
import com.art_gallery_hub.enums.RoleStatus;
import com.art_gallery_hub.mapper.UserMapper;
import com.art_gallery_hub.model.ArtistProfile;
import com.art_gallery_hub.model.Role;
import com.art_gallery_hub.model.User;
import com.art_gallery_hub.repository.ArtistProfileRepository;
import com.art_gallery_hub.repository.RoleRepository;
import com.art_gallery_hub.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final ArtistProfileRepository artistProfileRepository;
    private final UserMapper userMapper;

    private final PasswordEncoder passwordEncoder;

    private User findUserOrThrow(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "User not found with ID: " + userId));
    }

    private void validateUserExistence(String username) {
        if (userRepository.findByUsername(username).isPresent()) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "User already exists with name: " + username);
        }
    }

    private void validateEmailExistence(String email) {
        if (userRepository.findByEmail(email).isPresent()) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "User already exists with email: " + email);
        }
    }

    private void validateNewUser(String username, String email) {
        validateUserExistence(username);
        validateEmailExistence(email);
    }

    private Role findRoleOrThrow(RoleStatus roleName) {
        return roleRepository.findByName(roleName)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Role not found with name: " + roleName));
    }

    private Set<Role> findRolesOrThrow(Set<String> roleNames) {
        return roleNames.stream()
                .map(name -> {
                    try {
                        RoleStatus roleStatus = RoleStatus.valueOf(name);
                        return findRoleOrThrow(roleStatus);
                    } catch (IllegalArgumentException e) {
                        throw new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Role not found with name: " + name);
                    }
                })
                .collect(Collectors.toSet());
    }

    @Transactional
    public UserRegistrationResponse createUser(
            UserRegistrationRequest request,
            RoleStatus roleName
    ) {
        validateNewUser(request.username(), request.email());

        Role role = findRoleOrThrow(roleName);
        Set<Role> roles = Set.of(role);

        String encodedPassword = passwordEncoder.encode(request.password());

        User newUser = userRepository.save(
                userMapper.toEntity(
                        request.username(),
                        request.email(),
                        encodedPassword,
                        roles
                ));

        if (roleName == RoleStatus.ROLE_ARTIST) {
            log.info("Creating ArtistProfile for new user: {}", newUser.getUsername());

            ArtistProfile newProfile = new ArtistProfile();
            newProfile.setUser(newUser);
            newProfile.setDisplayName(newUser.getUsername());
            newProfile.setBio("Artist profile created automatically. Please update your bio and website.");

            newUser.setArtistProfile(newProfile);

            artistProfileRepository.save(newProfile);
            log.info("ArtistProfile created successfully for user: {}", newUser.getUsername());
        }

        return userMapper.toUserRegistrationResponse(newUser);
    }

    @Transactional
    public List<UserAdminSummaryResponse> getAllUsers() {
        log.info("Fetching all users for admin view");
        List<User> users = userRepository.findAll();
        log.info("Found {} users in system", users.size());
        return users.stream()
                .map(userMapper::toUserAdminSummaryResponse)
                .toList();
    }

    @Transactional
    public UserRegistrationResponse createAdminUser(
            UserAdminCreationRequest request
    ) {
        validateNewUser(request.username(), request.email());

        Set<Role> roles = findRolesOrThrow(request.roleNames());

        String encodedPassword = passwordEncoder.encode(request.password());

        User newUser = userRepository.save(
                userMapper.toEntity(
                        request.username(),
                        request.email(),
                        encodedPassword,
                        roles
                ));

        return userMapper.toUserRegistrationResponse(newUser);
    }

    @Transactional
    public UserAdminSummaryResponse updateUserRoles(
            Long userId,
            UserRoleUpdateRequest request
    ) {
        User user = findUserOrThrow(userId);
        Set<Role> roles = findRolesOrThrow(request.roleNames());

        user.setRoles(roles);

        User updatedUser = userRepository.save(user);

        return userMapper.toUserAdminSummaryResponse(updatedUser);
    }
}

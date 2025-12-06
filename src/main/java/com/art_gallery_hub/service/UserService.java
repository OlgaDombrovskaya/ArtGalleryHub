package com.art_gallery_hub.service;

import com.art_gallery_hub.dto.UserRegistrationRequest;
import com.art_gallery_hub.dto.UserAdminSummaryResponse;
import com.art_gallery_hub.dto.UserRegistrationResponse;
import com.art_gallery_hub.enums.RoleStatus;
import com.art_gallery_hub.mapper.UserMapper;
import com.art_gallery_hub.model.Role;
import com.art_gallery_hub.model.User;
import com.art_gallery_hub.repository.RoleRepository;
import com.art_gallery_hub.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;

    private final PasswordEncoder passwordEncoder;

    @Transactional
    public UserRegistrationResponse createUser(
            UserRegistrationRequest request,
            RoleStatus roleName
    ) {
        if (userRepository.findByUsername(request.username()).isPresent()) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "User already exists with name: " + request.username());
        }
        Role role = roleRepository.findByName(roleName)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Role not found with name: " + roleName));

        String encodedPassword = passwordEncoder.encode(request.password());

        User newUser = userRepository.save(
                userMapper.toEntity(request, encodedPassword, role));

        return userMapper.toUserRegistrationResponse(newUser);
    }

    @Transactional
    public List<UserAdminSummaryResponse> getAllUsers() {
        List<User> users = userRepository.findAll();

        return users.stream()
                .map(user -> userMapper
                        .toUserAdminSummaryResponse(user))
                .toList();
    }
}

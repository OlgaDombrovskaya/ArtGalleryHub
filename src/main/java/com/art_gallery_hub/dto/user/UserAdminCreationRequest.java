package com.art_gallery_hub.dto.user;

import java.util.Set;

public record UserAdminCreationRequest(
        String username,
        String email,
        String password,
        Set<String> roleNames
) {
}

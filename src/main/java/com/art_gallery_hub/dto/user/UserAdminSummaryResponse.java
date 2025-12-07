package com.art_gallery_hub.dto.user;

import java.util.Set;

public record UserAdminSummaryResponse(
        Long id,
        String username,
        String email,
        Set<String> roles
) {}

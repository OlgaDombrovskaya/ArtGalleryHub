package com.art_gallery_hub.dto;

import java.util.Set;

public record UserAdminSummaryResponse(
        Long id,
        String username,
        String email,
        Set<String> roles
) {}

package com.art_gallery_hub.dto.user;

import java.util.Set;

public record UserRoleUpdateRequest(
        Set<String> roleNames
) {
}

package com.art_gallery_hub.service;

import com.art_gallery_hub.model.Exhibition;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class CuratorService {
    @PreAuthorize("#exhibition.curator.username == authentication.name or hasRole('ADMIN')")
    public Exhibition updateExhibition(Exhibition exhibition) {
        log.info("Updating exhibition id={} by curator='{}'",
                exhibition.getId(),
                exhibition.getCurator() != null ? exhibition.getCurator().getUsername() : "UNKNOWN");

        return exhibition;
    }
}

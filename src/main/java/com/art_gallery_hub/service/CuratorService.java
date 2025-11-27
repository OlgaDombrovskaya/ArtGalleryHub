package com.art_gallery_hub.service;

import com.art_gallery_hub.model.Exhibition;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

@Service
public class CuratorService {
    @PreAuthorize("#exhibition.curator.username == authentication.name or hasRole('ADMIN')")
    public Exhibition updateExhibition(Exhibition exhibition) {
        return exhibition;
    }
}

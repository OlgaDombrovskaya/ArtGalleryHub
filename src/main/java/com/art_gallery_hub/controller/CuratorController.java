package com.art_gallery_hub.controller;

import com.art_gallery_hub.model.Exhibition;
import com.art_gallery_hub.repository.ExhibitionRepository;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "Curator Operations", description = "Exhibitions and invitations management. Available only for the CURATOR role")
@RestController
@RequestMapping("/api/curator")
public class CuratorController {

    private final ExhibitionRepository exhibitionRepository;

    public CuratorController(ExhibitionRepository exhibitionRepository) {
        this.exhibitionRepository = exhibitionRepository;
    }

    @GetMapping("/exhibitions/my")
    public List<Exhibition> getMyExhibitions(@AuthenticationPrincipal UserDetails userDetails) {
        return List.of();
    }
}

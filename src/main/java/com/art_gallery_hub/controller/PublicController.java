package com.art_gallery_hub.controller;


import com.art_gallery_hub.model.Artwork;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "Public Access / Authentication", description = "Login, registration," +
        " and password recovery operations. Available without authentication")
@RestController
@RequestMapping("/api/public")
public class PublicController {

    @GetMapping("/info")
    public String publicInfo() {
        return "Welcome to Art Gallery Hub";
    }

@GetMapping("/artworks")
    public List<Artwork> getArtworksByIsPublic() {

}
}

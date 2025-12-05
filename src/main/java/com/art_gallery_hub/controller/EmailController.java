package com.art_gallery_hub.controller;

import com.art_gallery_hub.service.EmailService;
import jakarta.validation.constraints.Email;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/email")
@RequiredArgsConstructor
public class EmailController {

    private final EmailService emailService;

    @PostMapping("/invitation")
    public ResponseEntity<String> sendInvitationToArtist(
            @RequestParam @Email String emailTo,
            @RequestParam String artistName,
            @RequestParam String exhibitionTitle,
            @RequestParam String dates,
            @RequestParam String description
    ) {

        Map<String, Object> vars = Map.of(
                "artistName", artistName,
                "exhibitionTitle", exhibitionTitle,
                "dates", dates,
                "description", description,
                "url", "https://artist.gallery.com"
        );

        emailService.sendTemplateEmail(
                emailTo,
                "Invitation to exhibition",
                "email/exhibition-invitation",
                vars
        );

        return ResponseEntity.ok("Invitation email sent to " + emailTo);
    }
    @PostMapping("/confirmation")
    public ResponseEntity<String> sendParticipationConfirmation(
            @RequestParam @Email String emailTo,
            @RequestParam String artistName,
            @RequestParam String exhibitionTitle,
            @RequestParam String artistBio
    ) {

        Map<String, Object> vars = Map.of(
                "artistName", artistName,
                "exhibitionTitle", exhibitionTitle,
                "artistBio", artistBio
        );

        emailService.sendTemplateEmail(
                emailTo,
                "Artist participation confirmed",
                "email/participation-confirmation",
                vars
        );

        return ResponseEntity.ok("Confirmation email sent to " + emailTo);
    }
}
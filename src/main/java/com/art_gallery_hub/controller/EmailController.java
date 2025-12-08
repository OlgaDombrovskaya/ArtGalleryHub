package com.art_gallery_hub.controller;

import com.art_gallery_hub.service.EmailService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Email;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Tag(
        name = "Notifications and Email Operations",
        description = "API for queuing and sending various system emails " +
                "(invitations, confirmations, etc.)"
)
@Slf4j
@RestController
@RequestMapping("/api/email")
@RequiredArgsConstructor
public class EmailController {

    private final EmailService emailService;

    /**
     * POST /api/email/invitation
     * Sends a templated exhibition invitation email to a specified artist.
     *
     * @param emailTo The recipient's email address
     * @param artistName The name of the artist being invited
     * @param exhibitionTitle The title of the exhibition
     * @param dates Date range of the exhibition
     * @param description Brief description of the exhibition
     * @param message Personalized message from the curator
     * @param times Specific times or opening hours
     * @return Confirmation message
     */
    @Operation(
            summary = "Send Exhibition Invitation Email",
            description = "Queues a templated email invitation to a specified artist for an exhibition.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Email successfully queued/sent"),
                    @ApiResponse(responseCode = "400", description = "Bad Request (e.g., Invalid email format)"),
                    @ApiResponse(responseCode = "500", description = "Internal Server Error (Email sending failed)")
            }
    )
    @PostMapping("/invitation")
    public ResponseEntity<String> sendInvitationToArtist(
            @RequestParam @Email String emailTo,
            @RequestParam String artistName,
            @RequestParam String exhibitionTitle,
            @RequestParam String dates,
            @RequestParam String description,
            @RequestParam String message,
            @RequestParam String times
    ) {
        log.info("EmailController: sending exhibition invitation to '{}' for exhibition '{}'",
                emailTo, exhibitionTitle);

        Map<String, Object> vars = Map.of(
                "artistName", artistName,
                "exhibitionTitle", exhibitionTitle,
                "dates", dates,
                "description", description,
                "message", message,
                "times", times,
                "url", "https://artist.gallery.com"

        );

        emailService.sendTemplateEmail(
                emailTo,
                "Invitation to exhibition",
                "email/exhibition-invitation",
                vars
        );
        log.info("EmailController: invitation email successfully queued for '{}'", emailTo);
        return ResponseEntity.ok("Invitation email sent to " + emailTo);
    }

    /**
     * POST /api/email/confirmation
     * Sends a participation confirmation email to an artist.
     *
     * @param emailTo The recipient's email address
     * @param artistName The name of the artist
     * @param exhibitionTitle The title of the exhibition the artist is participating in
     * @param artistBio Short biography snippet for confirmation email
     * @return Confirmation message
     */
    @Operation(
            summary = "Send Participation Confirmation Email",
            description = "Queues a templated email confirming an artist's participation in an exhibition.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Email successfully queued/sent"),
                    @ApiResponse(responseCode = "400", description = "Bad Request (e.g., Invalid email format)"),
                    @ApiResponse(responseCode = "500", description = "Internal Server Error (Email sending failed)")
            }
    )
    @PostMapping("/confirmation")
    public ResponseEntity<String> sendParticipationConfirmation(
            @RequestParam @Email String emailTo,
            @RequestParam String artistName,
            @RequestParam String exhibitionTitle,
            @RequestParam String artistBio
    ) {

        log.info("EmailController: sending participation confirmation to '{}' for exhibition '{}'",
                emailTo, exhibitionTitle);
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
        log.info("EmailController: confirmation email successfully queued for '{}'", emailTo);
        return ResponseEntity.ok("Confirmation email sent to " + emailTo);
    }
}
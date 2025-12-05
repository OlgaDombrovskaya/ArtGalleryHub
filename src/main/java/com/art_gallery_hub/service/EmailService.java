package com.art_gallery_hub.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.nio.charset.StandardCharsets;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;

    @Value("${app.mail.from.address}")
    private String fromAddress;

    @Value("ArtGalleryHub")
    private String fromName;

    public void sendTemplateEmail(String to, String subject, String template, Map<String, Object> variables) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                    StandardCharsets.UTF_8.name());

            helper.setFrom(fromAddress);
            helper.setTo(to);
            helper.setSubject(subject);

            Context context = new Context();
            context.setVariables(variables);

            String html = templateEngine.process(template, context);
            helper.setText(html, true);

            mailSender.send(mimeMessage);

            log.info("Email sent to {} with subject {}", to, subject);

        } catch (MessagingException exception) {

            log.error(exception.getMessage(), exception);
            throw new RuntimeException("Email sending failed " + exception);

        } catch (Exception exception) {

            log.error(exception.getMessage(), exception);
            throw new RuntimeException("Unexpected error " + exception);
        }
    }
}



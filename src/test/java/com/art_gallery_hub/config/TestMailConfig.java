package com.art_gallery_hub.config;

import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.mail.javamail.JavaMailSender;

@Configuration          // обычная конфигурация
@Profile("test")       // будет активна только при профиле test
public class TestMailConfig {

    @Bean
    public JavaMailSender javaMailSender() {
        // мок, чтобы EmailService создался,
        // но настоящих писем не отправлял
        return Mockito.mock(JavaMailSender.class);
    }
}
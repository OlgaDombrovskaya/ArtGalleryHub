package com.art_gallery_hub.config;

import com.art_gallery_hub.service.ArtUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider(ArtUserDetailsService appUserDetailsService) {
        DaoAuthenticationProvider authenticationProvider =
                new DaoAuthenticationProvider(appUserDetailsService);
//        authenticationProvider.setUserDetailsPasswordService(appUserDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http,DaoAuthenticationProvider authenticationProvider
    ) throws Exception {
        http.authenticationProvider(authenticationProvider);

        http.csrf(csrf -> csrf.disable());

        http.headers(headers -> headers
                .frameOptions(frame -> frame.disable())
        );

        http.authorizeHttpRequests(auth -> auth
                .requestMatchers(
                        "/api/public/**",
                        "/api/auth/**",
                        "/v3/api-docs/**",
                        "/swagger-ui/**",
                        "/swagger-ui.html",
                        "/api/admin/user" //только для теста
                ).permitAll()
                .requestMatchers("/api/artist/**").hasRole("ARTIST")
                .requestMatchers("/api/visitor/**").hasRole("VISITOR")
                .requestMatchers("/api/curator/**").hasRole("CURATOR")
                .requestMatchers("/api/admin/**").hasRole("ADMIN")
                .requestMatchers("/h2-console/**").permitAll()
                .anyRequest().authenticated()
        );
//        http.httpBasic(Customizer.withDefaults());

        // ОТКЛЮЧАЕМ basic-auth, чтобы не было всплывающего окна браузера
        http.httpBasic(AbstractHttpConfigurer::disable);

        http.formLogin(Customizer.withDefaults());
        http.logout(Customizer.withDefaults());
        return http.build();
    }
}
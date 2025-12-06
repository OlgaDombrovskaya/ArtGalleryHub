package com.art_gallery_hub.service;

import com.art_gallery_hub.model.User;
import com.art_gallery_hub.repository.UserRepository;
import com.art_gallery_hub.security.ArtSecurityUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ArtUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public ArtUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("Trying to load user by username='{}'", username);
        User user = userRepository.findByUsername(username).orElseThrow(
                () -> {
                    log.warn("User with username='{}' not found", username);
                    return new UsernameNotFoundException("User not found " + username);
                });
        return new ArtSecurityUser(user);
    }
}

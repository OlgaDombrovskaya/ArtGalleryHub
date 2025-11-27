package com.art_gallery_hub.service;

import com.art_gallery_hub.model.User;
import com.art_gallery_hub.repository.UserRepository;
import com.art_gallery_hub.security.ArtSecurityUser;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class ArtUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public ArtUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username).orElseThrow(
                () -> new UsernameNotFoundException("User not found " + username));
        return new ArtSecurityUser(user);
    }
}

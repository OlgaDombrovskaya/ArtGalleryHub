//package com.art_gallery_hub.config;
//
//import com.art_gallery_hub.model.User;
//import com.art_gallery_hub.repository.UserRepository;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.crypto.password.PasswordEncoder;
//
//@Configuration
//public class InitData {
//    @Bean
//    CommandLineRunner initUsers(UserRepository userRepository,
//                                PasswordEncoder passwordEncoder) {
//        return args -> {
//            if(userRepository.count() == 0) {
//                String artistPassword = passwordEncoder.encode("artist123");
//                String visitorPassword = passwordEncoder.encode("visitor123");
//                String curatorPassword = passwordEncoder.encode("curator123");
//                String adminPassword = passwordEncoder.encode("admin123");
//
//
//                User artist= new User("user",artistPassword, "ROLE_ARTIST");
//                User visitor = new User("visitor",visitorPassword, "ROLE_VISITOR");
//                User curator = new User("curator",curatorPassword, "ROLE_CURATOR");
//                User admin = new User("admin",adminPassword, "ROLE_ADMIN");
//
//                userRepository.save(artist);
//                userRepository.save(admin);
//                userRepository.save(visitor);
//                userRepository.save(curator);
//            }
//        };
//    }
//}

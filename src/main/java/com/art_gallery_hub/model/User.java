package com.art_gallery_hub.model;

import java.util.Set;

public class User {
    private Long id;
    private String username;
    private String email;
    private String password;
    private boolean enabled;
    private Set<Role> roles;
}

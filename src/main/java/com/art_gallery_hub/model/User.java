package com.art_gallery_hub.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PreRemove;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
@NoArgsConstructor
@Getter
@Setter
@ToString(exclude = {"roles", "artistProfile", "curatedExhibitions", "reviews"})
@EqualsAndHashCode(exclude = {"roles", "artistProfile", "curatedExhibitions", "reviews"})
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private boolean enabled = true;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();

    //---------------Inverse relationship------------------
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private ArtistProfile artistProfile;

    @OneToMany(mappedBy = "curator", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<Exhibition> curatedExhibitions = new HashSet<>();

    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<Review> reviews = new HashSet<>();

    //-----------------------------------------------------

    public User(
            String username,
            String password,
            Set<Role> roles) {
        this.username = username;
        this.password = password;
        this.roles = roles;
    }

    public User(
            String username,
            String email,
            String password,
            Set<Role> roles) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.roles = roles;
    }

    // TODO
    public User(
            String user,
            String artistPassword,
            String roleArtist) {
    }

    //-----------------------------------------------------
    @PreRemove
    private void preRemove() {
        if (artistProfile != null) {
            for (Artwork artwork : artistProfile.getArtworks()) {
                // Разрываем связи Many-to-Many перед удалением
                for (Exhibition exhibition : new HashSet<>(artwork.getExhibitions())) {
                    exhibition.getArtworks().remove(artwork);
                }
                artwork.getExhibitions().clear();
            }
        }
    }
}

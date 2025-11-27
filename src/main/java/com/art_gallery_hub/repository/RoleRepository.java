package com.art_gallery_hub.repository;

import com.art_gallery_hub.enums.RoleStatus;
import com.art_gallery_hub.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role,Long> {

    Optional<Role> findByName(RoleStatus name);
}

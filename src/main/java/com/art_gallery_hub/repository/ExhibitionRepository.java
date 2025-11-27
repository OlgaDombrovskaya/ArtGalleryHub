package com.art_gallery_hub.repository;

import com.art_gallery_hub.enums.ExhibitionStatus;
import com.art_gallery_hub.model.Exhibition;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ExhibitionRepository extends JpaRepository<Exhibition, Long> {

    List<Exhibition> findByExhibitionStatus(ExhibitionStatus status);
}

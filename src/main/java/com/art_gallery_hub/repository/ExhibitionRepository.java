package com.art_gallery_hub.repository;

import com.art_gallery_hub.enums.ExhibitionStatus;
import com.art_gallery_hub.model.Exhibition;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ExhibitionRepository extends JpaRepository<Exhibition, Long> {
    // Публичный список выставок (PLANNED / OPEN)
    List<Exhibition> findByStatus(ExhibitionStatus status);
    // Выставки, за которые отвечает куратор
    List<Exhibition> findByCurator_Id(Long curatorId);
//    List<Exhibition> findByExhibitionStatus(ExhibitionStatus status);
}

package com.art_gallery_hub.repository;

import com.art_gallery_hub.enums.ExhibitionStatus;
import com.art_gallery_hub.model.Exhibition;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface ExhibitionRepository extends JpaRepository<Exhibition, Long> {

    List<Exhibition> findByStatusIn(Collection<ExhibitionStatus> statuses);
    // Публичный список выставок (PLANNED / OPEN)
//    List<Exhibition> findByStatus(ExhibitionStatus status);
    // Выставки, за которые отвечает куратор
//    List<Exhibition> findByCurator_Id(Long curatorId);
//    List<Exhibition> findByExhibitionStatus(ExhibitionStatus status);
}

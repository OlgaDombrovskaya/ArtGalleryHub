package com.art_gallery_hub.service;

import com.art_gallery_hub.dto.ExhibitionSummaryResponse;
import com.art_gallery_hub.enums.ExhibitionStatus;
import com.art_gallery_hub.mapper.ExhibitionMapper;
import com.art_gallery_hub.model.Exhibition;
import com.art_gallery_hub.repository.ExhibitionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ExhibitionService {

    private final ExhibitionRepository exhibitionRepository;
    private final ExhibitionMapper exhibitionMapper;

    public List<ExhibitionSummaryResponse> getOpenOrPlannedExhibitions() {
        List<Exhibition> exhibitions = exhibitionRepository.findByStatusIn(
                List.of(ExhibitionStatus.OPEN, ExhibitionStatus.PLANNED));

        return exhibitions.stream()
                .map(exhibition -> exhibitionMapper
                        .toExhibitionSummaryResponse(exhibition))
                .toList();
    }
}

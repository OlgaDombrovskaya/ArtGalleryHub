package com.art_gallery_hub.mapper;

import com.art_gallery_hub.dto.exhibition.ExhibitionPublicSummaryResponse;
import com.art_gallery_hub.model.Exhibition;
import org.springframework.stereotype.Component;

@Component
public class ExhibitionMapper {

    public ExhibitionPublicSummaryResponse toExhibitionSummaryResponse(Exhibition exhibition) {
        return new ExhibitionPublicSummaryResponse(
                exhibition.getId(),
                exhibition.getTitle(),
                exhibition.getStartDate(),
                exhibition.getEndDate(),
                exhibition.getCurator().getUsername()
        );
    }
}

package com.art_gallery_hub.mapper;

import com.art_gallery_hub.dto.ExhibitionSummaryResponse;
import com.art_gallery_hub.model.Exhibition;
import org.springframework.stereotype.Component;

@Component
public class ExhibitionMapper {

    public ExhibitionSummaryResponse toExhibitionSummaryResponse(Exhibition exhibition) {
        return new ExhibitionSummaryResponse(
                exhibition.getId(),
                exhibition.getTitle(),
                exhibition.getStartDate(),
                exhibition.getEndDate(),
                exhibition.getCurator().getUsername()
        );
    }
}

package com.app.property.mapper;

import com.app.property.entity.Property;
import com.app.property.entity.PropertyMedia;
import com.app.property.model.PropertyDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
@Slf4j
public class PropertyMapper {

    /**
     * Convert PropertyDTO to Property Entity
     * Creates PropertyMedia entities from mediaUrls
     */
    public Property toProperty(PropertyDTO dto, List<String> mediaUrls) {
        Property property = Property.builder()
                .id(UUID.randomUUID())
                .name(dto.getName())
                .description(dto.getDescription())
                .location(dto.getLocation())
                .price(dto.getPrice())
                .isSold(dto.getIsSold())
                .videoUrl(dto.getVideoUrl())
                .build();

        // Create PropertyMedia entities from URLs
        if (mediaUrls != null && !mediaUrls.isEmpty()) {
            List<PropertyMedia> mediaList = new ArrayList<>();
            for (String url : mediaUrls) {
                PropertyMedia media = new PropertyMedia();
                media.setUrl(url);
                media.setProperty(property);
                // publicId and type will be set separately if needed
                mediaList.add(media);
            }
            property.setMedia(mediaList);
        }

        return property;
    }

    /**
     * Convert Property Entity to PropertyDTO
     * Extracts URLs from PropertyMedia entities
     */
    public PropertyDTO toPropertyDTO(Property property) {
        List<String> mediaUrls = new ArrayList<>();

        if (property.getMedia() != null && !property.getMedia().isEmpty()) {
            for (PropertyMedia media : property.getMedia()) {
                if (media.getUrl() != null) {
                    mediaUrls.add(media.getUrl());
                }
            }
        }
        return PropertyDTO.builder()
                .id(property.getId())
                .name(property.getName())
                .description(property.getDescription())
                .location(property.getLocation())
                .isSold(property.isSold)
                .videoUrl(property.getVideoUrl())
                .price(property.getPrice())
                .mediaUrls(mediaUrls)
                .build();
    }
}

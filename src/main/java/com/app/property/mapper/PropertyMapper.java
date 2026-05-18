package com.app.property.mapper;

import com.app.property.entity.Property;
import com.app.property.model.PropertyDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
@Slf4j
public class PropertyMapper {

    // DTO → Entity
    public  Property toProperty(PropertyDTO dto, List<String> mediaUrls) {
        return Property.builder()
                .id(UUID.randomUUID())
                .name(dto.getName())
                .description(dto.getDescription())
                .location(dto.getLocation())
                .price(dto.getPrice())
                .mediaUrls(mediaUrls)
                .build();
    }

    // Entity → DTO
    public  PropertyDTO toPropertyDTO(Property property, List<String> mediaUrls) {
        return PropertyDTO.builder()
                .id(property.getId())
                .name(property.getName())
                .description(property.getDescription())
                .location(property.getLocation())
                .price(property.getPrice())
                .mediaUrls(mediaUrls)
                .build();
    }
}

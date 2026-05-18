
package com.app.property.service;

import com.app.property.entity.Property;
import com.app.property.mapper.PropertyMapper;
import com.app.property.model.PropertyDTO;
import com.app.property.repository.PropertyRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class PropertyService {

    @Value("${property.storage}")
    private String uploadDir;

    private final PropertyRepository propertyRepository;
    private final PropertyMapper propertyMapper;

    public List<PropertyDTO> getAll() {

        List<Property> properties = propertyRepository.findAll();
        List<PropertyDTO> propertyDTOs = new ArrayList<>();
        for (Property property : properties) {
            List<String> mediaUrls = property.getMediaUrls();
            PropertyDTO dto = propertyMapper.toPropertyDTO(property, mediaUrls);
            propertyDTOs.add(dto);
        }
        return propertyDTOs;
    }

    public void addProperty(PropertyDTO propertyDTO) throws IOException {

        List<String> uploadedFiles = saveMediaFile( propertyDTO.getMedia());
        Property property = propertyMapper.toProperty(propertyDTO, uploadedFiles);
        propertyRepository.save(property);
    }

    public void delete(String id) {
        List<String> mediaFileNames = propertyRepository.findMediaUrlsByPropertyId(UUID.fromString(id));

        for (String mediaFileName : mediaFileNames) {
            Path filePath = Paths.get(uploadDir, mediaFileName);
            try {
                Files.deleteIfExists(filePath);
            } catch (IOException e) {
                log.error("Failed to delete media file: {}", mediaFileName, e);
            }
        }
        propertyRepository.deleteById(UUID.fromString(id));
    }

    private List<String> saveMediaFile(MultipartFile[] mediaFiles) throws IOException {

        List<String> uploadedFiles = new ArrayList<>();
        for (MultipartFile file : mediaFiles) {
            if (!file.isEmpty()) {
                String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
                Path filePath = Paths.get(uploadDir, fileName);
                Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
                uploadedFiles.add(fileName);
            }
        }
        return uploadedFiles;
    }
}

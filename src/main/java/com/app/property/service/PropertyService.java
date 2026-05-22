
package com.app.property.service;

import com.app.property.entity.Property;
import com.app.property.mapper.PropertyMapper;
import com.app.property.model.PropertyDTO;
import com.app.property.model.UpdatePropertyRequest;
import com.app.property.repository.PropertyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class PropertyService {

    private final PropertyRepository propertyRepository;
    private final PropertyMapper propertyMapper;
    private final CloudinaryService cloudinaryService;
    private final PropertyMediaService propertyMediaService;

    public List<PropertyDTO> getAll() {

        List<Property> properties = propertyRepository.findAll();
        List<PropertyDTO> propertyDTOs = new ArrayList<>();
        for (Property property : properties) {
            PropertyDTO dto = propertyMapper.toPropertyDTO(property);
            propertyDTOs.add(dto);
        }
        return propertyDTOs;
    }

    public void addProperty(PropertyDTO propertyDTO) throws IOException {

        List<String> uploadedFiles = saveMediaFile( propertyDTO.getMedia());
        Property property = propertyMapper.toProperty(propertyDTO, uploadedFiles);
        propertyRepository.save(property);
    }

    public void updateProperty(String propertyId, UpdatePropertyRequest req) {
        var property = propertyRepository.findById(UUID.fromString(propertyId))
                .orElseThrow(() -> new IllegalArgumentException("Property not found: " + propertyId));
        if (req.name() != null) property.setName(req.name());
        if (req.description() != null) property.setDescription(req.description());
        if (req.price() != null) property.setPrice(req.price());
        if (req.location() != null) property.setLocation(req.location());
        if (req.isSold() != null) property.setIsSold(req.isSold());
        propertyRepository.save(property);
    }

    public void delete(String propertyId) {

        propertyMediaService.delete(propertyId);
        propertyRepository.deleteById(UUID.fromString(propertyId));
    }

    private List<String> saveMediaFile(MultipartFile[] mediaFiles) throws IOException {

        List<String> uploadedFiles = new ArrayList<>();
        for (MultipartFile file : mediaFiles) {
            if (!file.isEmpty()) {
                String url;
                String contentType = file.getContentType();
                if (contentType != null && contentType.startsWith("image/")) {
                    url = cloudinaryService.uploadImage(file);
                } else if (contentType != null && contentType.startsWith("video/")) {
                    url = cloudinaryService.uploadVideo(file);
                } else {
                    log.warn("Unsupported media type: {}", contentType);
                    continue;
                }
                uploadedFiles.add(url);
            }

        }
        return uploadedFiles;
    }
}

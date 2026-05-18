
package com.app.property.service;

import com.app.property.entity.PropertyMedia;
import com.app.property.repository.PropertyMediaRepository;
import com.app.property.repository.PropertyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class PropertyMediaService {

    private final PropertyMediaRepository propertyMediaRepository;
    private final PropertyRepository propertyRepository;
    private final CloudinaryService cloudinaryService;

    /**
     * Delete all media associated with a property from Cloudinary and database
     * @param propertyId the ID of the property
     */
    public void delete(String propertyId) {
        try {
            UUID propId = UUID.fromString(propertyId);

            // Find the property
            var property = propertyRepository.findById(propId);
            if (property.isEmpty()) {
                log.warn("Property not found with id: {}", propertyId);
                return;
            }

            // Get all media associated with this property
            List<PropertyMedia> mediaList = propertyMediaRepository.findAll().stream()
                    .filter(m -> m.getProperty() != null && m.getProperty().getId().equals(propId))
                    .toList();

            // Delete each media from Cloudinary
            for (PropertyMedia media : mediaList) {
                try {
                    if (media.getPublicId() != null && !media.getPublicId().isEmpty()) {
                        String resourceType = media.getType() != null ?
                                media.getType().toString().toLowerCase() : "image";

                        cloudinaryService.deleteMedia(media.getPublicId(), resourceType);
                        log.info("Deleted media from Cloudinary: {}", media.getPublicId());
                    }

                    // Delete from database
                    propertyMediaRepository.delete(media);
                    log.info("Deleted media record from database: {}", media.getId());

                } catch (Exception e) {
                    log.error("Error deleting media {}: {}", media.getId(), e.getMessage());
                }
            }

            log.info("Successfully deleted all media for property: {}", propertyId);

        } catch (IllegalArgumentException e) {
            log.error("Invalid property ID format: {}", propertyId);
        } catch (Exception e) {
            log.error("Error deleting property media: {}", e.getMessage());
        }
    }

}

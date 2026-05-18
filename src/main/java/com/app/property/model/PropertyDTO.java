
package com.app.property.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Builder
@Data
public class PropertyDTO {
    private  UUID id;
    private String name;
    private String description;
    private String location;
    private String price;
    private MultipartFile[] media;
    private java.util.List<String> mediaUrls;
}

package com.app.property.entity;

import com.app.property.enums.MediaType;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Entity
@Table(name = "PropertyMedia")
@NoArgsConstructor
public class PropertyMedia {

    @Id
    @GeneratedValue
    private UUID id;

    private String url;
    private String publicId;

    @Enumerated(EnumType.STRING)
    private MediaType type;

    @ManyToOne
    private Property property;
}

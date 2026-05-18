package com.app.property.entity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "Property")
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Property {

    @Id
    public UUID id;
    public String name;
    public String description;
    public String location;
    public String price;
    @Builder.Default
    public Boolean isSold = false;
    public String videoUrl;

    @OneToMany(mappedBy = "property", cascade = CascadeType.ALL)
    private List<PropertyMedia> media;
}

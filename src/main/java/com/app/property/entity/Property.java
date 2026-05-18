package com.app.property.entity;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
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
    // comma separated file names
    @ElementCollection
    private List<String> mediaUrls;
}

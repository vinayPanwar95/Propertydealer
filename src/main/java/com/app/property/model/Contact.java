package com.app.property.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Contact form data model
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Contact {
    private String name;
    private String phone;
    private String email;
    private String propertyType;
    private String enquiry;
}


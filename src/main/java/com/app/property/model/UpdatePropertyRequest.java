package com.app.property.model;

public record UpdatePropertyRequest(
        String name,
        String description,
        String price,
        String location,
        Boolean isSold
) {}

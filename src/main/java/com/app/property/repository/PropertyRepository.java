package com.app.property.repository;

import com.app.property.entity.Property;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PropertyRepository extends JpaRepository<Property, UUID> {

    @Query("""
        SELECT m
        FROM Property p
        JOIN p.mediaUrls m
        WHERE p.id = :id
    """)
    List<String> findMediaUrlsByPropertyId(@Param("id") UUID id);
}

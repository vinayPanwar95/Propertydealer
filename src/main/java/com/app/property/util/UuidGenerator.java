package com.app.property.util;

import java.util.UUID;

/**
 * Utility class for generating UUIDs before persisting entities.
 */
public final class UuidGenerator {

    private UuidGenerator() {}

    public static UUID generate() {
        return UUID.randomUUID();
    }
}

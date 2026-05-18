package com.app.property.util;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("local")
@Slf4j
public class EnvDebugUtil {

    @PostConstruct
    public void printAllEnv() {

        log.info("Printing all environment variables for debugging purposes...");
        System.getenv().forEach((key, value) -> {
            log.info("{} = {}", key, value);
        });


    }
}

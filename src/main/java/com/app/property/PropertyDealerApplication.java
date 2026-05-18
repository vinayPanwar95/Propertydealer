
package com.app.property;

import jakarta.annotation.PreDestroy;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableJpaRepositories
@EnableAsync
public class PropertyDealerApplication {
    public static void main(String[] args) {

        SpringApplication.run(PropertyDealerApplication.class, args);
    }

    @PreDestroy
    public void onExit() {
        System.out.println("Application is shutting down...");
    }
}

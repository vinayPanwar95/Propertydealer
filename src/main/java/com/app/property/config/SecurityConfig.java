
package com.app.property.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.beans.factory.annotation.Value;

@Configuration
public class SecurityConfig {

    @Value("${app.admin.username}")
    String username;

    @Value("${app.admin.password}")
    String password;

    @Bean
    public InMemoryUserDetailsManager userDetails() {
        return new InMemoryUserDetailsManager(
                User.withUsername(username).password("{noop}" + password).roles("ADMIN").build()
        );
    }

    @Bean
    public SecurityFilterChain filter(HttpSecurity http) throws Exception {

        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/h2-console/**").permitAll()
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .anyRequest().permitAll()
                )

                .csrf(AbstractHttpConfigurer::disable)

                // IMPORTANT for H2 console (iframe support)
                .headers(headers -> headers
                        .frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin)
                )

                .formLogin(form -> form.defaultSuccessUrl("/admin", true))
                .logout(logout -> logout.logoutSuccessUrl("/"));

        return http.build();
    }
}

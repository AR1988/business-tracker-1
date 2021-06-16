package de.telran.businesstracker.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.UUID;
import java.util.function.Supplier;

@Configuration
public class AppConfig {

    @Bean
    public Supplier<String> uuidGenerator(){
        return () -> UUID.randomUUID().toString();
    }
}

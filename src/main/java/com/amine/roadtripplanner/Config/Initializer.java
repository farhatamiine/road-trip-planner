package com.amine.roadtripplanner.Config;


import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Initializer {
    @Bean
    public CommandLineRunner initializeEvents() {
        return args -> {

        };
    }
}

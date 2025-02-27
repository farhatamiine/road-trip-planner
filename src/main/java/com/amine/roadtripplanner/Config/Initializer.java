package com.amine.roadtripplanner.Config;


import com.amine.roadtripplanner.Entities.Role;
import com.amine.roadtripplanner.Repositories.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;

@Configuration
public class Initializer {

    @Bean
    public CommandLineRunner initializeRoles(RoleRepository roleRepository) {
        return args -> {
            // Check if roles already exist
            if (roleRepository.count() == 0) {
                List<String> roleNames = Arrays.asList("ROLE_USER", "ROLE_ADMIN");

                for (String roleName : roleNames) {
                    Role role = new Role();
                    role.setName(roleName);
                    roleRepository.save(role);
                }

                System.out.println("Roles initialized successfully!");
            }
        };
    }
}

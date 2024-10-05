package com.paras.FinMate;

import com.paras.FinMate.entities.Role;
import com.paras.FinMate.repositories.RoleRepo;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class FinMateApplication {

    public static void main (String[] args) {
        SpringApplication.run (FinMateApplication.class, args);
    }

    @Bean
    public CommandLineRunner runner (RoleRepo roleRepository) {
        return args -> {
            if (roleRepository.findByName ("USER").isEmpty ()) {
                roleRepository.save (Role.builder ().name ("USER").build ());
            }
        };
    }

}

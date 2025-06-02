package org.yiqixue.kbbackend.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.yiqixue.kbbackend.entity.Role;
import org.yiqixue.kbbackend.entity.User;
import org.yiqixue.kbbackend.repository.RoleRepository;
import org.yiqixue.kbbackend.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
@EnableJpaRepositories(basePackages = "org.yiqixue.kbbackend.repository")
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(String... args) {
        log.info("Initializing test data...");
        
        // Create roles if they don't exist
        createRoleIfNotFound("ROLE_ADMIN", "Administrator role with full access");
        createRoleIfNotFound("ROLE_USER", "Regular user with limited access");
        
        // Create admin user if it doesn't exist
        createAdminUserIfNotFound();
        
        // Update admin password if it exists but can't authenticate
        updateAdminPasswordIfNeeded();
        
        // Verify the user was created correctly
        userRepository.findByUsername("admin").ifPresent(user -> {
            log.info("Admin user exists with ID: {}", user.getId());
            log.info("Admin user roles: {}", user.getRoles().stream()
                    .map(Role::getName)
                    .collect(Collectors.joining(", ")));
        });
        
        log.info("Test data initialization completed");
    }
    
    private void createRoleIfNotFound(String name, String description) {
        Optional<Role> roleOpt = roleRepository.findByName(name);
        if (roleOpt.isEmpty()) {
            Role role = new Role();
            role.setName(name);
            role.setDescription(description);
            roleRepository.save(role);
            log.info("Created role: {}", name);
        }
    }
    
    private void createAdminUserIfNotFound() {
        Optional<User> userOpt = userRepository.findByUsername("admin");
        if (userOpt.isEmpty()) {
            Role adminRole = roleRepository.findByName("ROLE_ADMIN")
                    .orElseThrow(() -> new RuntimeException("Admin role not found"));
            
            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setEmail("admin@example.com");
            admin.setDisplayName("Admin User");
            admin.setCreatedAt(LocalDateTime.now());
            admin.setIsActive(true);
            admin.setRoles(Collections.singletonList(adminRole));
            
            userRepository.save(admin);
            log.info("Created admin user with username: admin and password: admin123");
        }
    }
    
    private void updateAdminPasswordIfNeeded() {
        Optional<User> userOpt = userRepository.findByUsername("admin");
        if (userOpt.isPresent()) {
            User admin = userOpt.get();
            // Always update the password to ensure it's encoded with the current encoder
            admin.setPassword(passwordEncoder.encode("admin123"));
            userRepository.save(admin);
            log.info("Updated admin password to: admin123");
        }
    }
}
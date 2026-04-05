package edu.tmu.group67.scrum_development.auth.config;

import edu.tmu.group67.scrum_development.auth.model.entity.Role;
import edu.tmu.group67.scrum_development.auth.model.entity.User;
import edu.tmu.group67.scrum_development.auth.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        if (userRepository.count() == 0) {
            //Seed a Developer
            userRepository.save(User.builder()
                    .email("dev@example.com")
                    .password(passwordEncoder.encode("dev123"))
                    .role(Role.DEVELOPER)
                    .accountLock(false)
                    .build());

            //Seed a Customer
            userRepository.save(User.builder()
                    .email("client@example.com")
                    .password(passwordEncoder.encode("client123"))
                    .role(Role.CUSTOMER)
                    .accountLock(false)
                    .build());

            //Seed a Representative
            userRepository.save(User.builder()
                    .email("rep@example.com")
                    .password(passwordEncoder.encode("rep123"))
                    .role(Role.REPRESENTATIVE)
                    .accountLock(false)
                    .build());

            System.out.println("Neon Database populated with initial Scrum users.");
        }
    }
}
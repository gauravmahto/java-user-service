package com.ecommerce.user_service.component;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.ecommerce.user_service.entity.AppUser;
import com.ecommerce.user_service.repository.AppUserRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

  private final AppUserRepository repository;
  private final PasswordEncoder encoder;

  @Override
  public void run(String... args) {
    if (repository.findByUsername("user").isEmpty()) {
      repository.save(AppUser.builder()
          .username("user")
          .password(encoder.encode("password"))
          .role("ROLE_USER")
          .build());
    }
  }
}

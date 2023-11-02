package com.skydan.user;

import com.skydan.AbstractTestcontainers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class AppUserRepositoryTest extends AbstractTestcontainers {

    @Autowired
    private AppUserRepository underTest;

    @BeforeEach
    void setUp() {
        underTest.deleteAll();
    }

    @Test
    void existsAppUserByEmail() {
        // Given
        String email = "email" + UUID.randomUUID() + "@gmail.com";
        AppUser appUser = new AppUser("Alex", email, 20);

        underTest.save(appUser);

        // When
        var actual = underTest.existsAppUserByEmail(email);

        // Then
        assertThat(actual).isTrue();
    }

    @Test
    void existsAppUserByEmailFailsWhenEmailNotPresent() {
        // Given
        String email = "email" + UUID.randomUUID() + "@gmail.com";

        // When
        var actual = underTest.existsAppUserByEmail(email);

        // Then
        assertThat(actual).isFalse();
    }

    @Test
    void existsAppUserById() {
        // Given
        String email = "email" + UUID.randomUUID() + "@gmail.com";
        AppUser appUser = new AppUser("Alex", email, 20);

        underTest.save(appUser);

        int id = underTest.findAll()
                .stream()
                .filter(c -> c.getEmail().equals(email))
                .map(AppUser::getId)
                .findFirst()
                .orElseThrow();

        // When
        var actual = underTest.existsAppUserById(id);

        // Then
        assertThat(actual).isTrue();
    }

    @Test
    void existsAppUserByIdWhenIdNotPresent() {
        // Given

        int id = -1;

        // When
        var actual = underTest.existsAppUserById(id);

        // Then
        assertThat(actual).isFalse();
    }
}
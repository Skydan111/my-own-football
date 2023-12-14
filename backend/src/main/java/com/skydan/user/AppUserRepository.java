package com.skydan.user;

import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AppUserRepository extends JpaRepository<AppUser, Integer> {

    boolean existsAppUserByEmail(String email);
    boolean existsAppUserById(Integer appUserId);
    Optional<AppUser> findAppUserByEmail(String email);
}

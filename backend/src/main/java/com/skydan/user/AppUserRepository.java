package com.skydan.user;

import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppUserRepository extends JpaRepository<AppUser, Integer> {

    boolean existsAppUserByEmail(String email);
    boolean existsAppUserById(Integer appUserId);
}

package com.skydan.user;

public record AppUserRegistrationRequest(
        String name,
        String email,
        String password,
        Team team
) {
}

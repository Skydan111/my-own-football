package com.skydan.user;

public record AppUserRegistrationRequest(
        String name,
        String email,
        String password,
        Integer age,
        Team team
) {
}

package com.skydan.user;

public record AppUserRegistrationRequest(
        String name,
        String email,
        Integer age
) {
}

package com.skydan.user;

public record AppUserUpdateRequest(
        String name,
        String email,
        Team team
) {
}

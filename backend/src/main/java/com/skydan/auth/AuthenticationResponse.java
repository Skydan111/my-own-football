package com.skydan.auth;

import com.skydan.user.AppUserDTO;

public record AuthenticationResponse (
        String token,
        AppUserDTO appUserDTO) {
}

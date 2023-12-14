package com.skydan.auth;

import com.skydan.jwt.JWTUtil;
import com.skydan.user.AppUser;
import com.skydan.user.AppUserDTO;
import com.skydan.user.AppUserDTOMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final AppUserDTOMapper appUserDTOMapper;
    private final JWTUtil jwtUtil;

    public AuthenticationService(AuthenticationManager authenticationManager,
                                 AppUserDTOMapper appUserDTOMapper,
                                 JWTUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.appUserDTOMapper = appUserDTOMapper;
        this.jwtUtil = jwtUtil;
    }

    public AuthenticationResponse login(AuthenticationRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.username(),
                        request.password()
                )
        );
        AppUser principal = (AppUser) authentication.getPrincipal();
        AppUserDTO appUserDTO = appUserDTOMapper.apply(principal);
        String token = jwtUtil.issueToken(appUserDTO.username(), appUserDTO.roles());

        // it's not a good idea return DTO -> you should never do this
        return new AuthenticationResponse(token, appUserDTO);
    }

}

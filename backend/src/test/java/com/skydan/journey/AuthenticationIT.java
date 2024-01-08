package com.skydan.journey;

import com.skydan.auth.AuthenticationRequest;
import com.skydan.auth.AuthenticationResponse;
import com.skydan.jwt.JWTUtil;
import com.skydan.user.AppUserDTO;
import com.skydan.user.AppUserRegistrationRequest;
import com.skydan.user.Team;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@SpringBootTest(webEnvironment = RANDOM_PORT)
public class AuthenticationIT {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private JWTUtil jwtUtil;

    private static final String AUTHENTICATION_PATH = "/api/v1/auth/login";
    private static final String USER_PATH = "/api/v1/users";

    @Test
    void canLogin() {

        String name = "Foo";
        String email = name.toLowerCase() + UUID.randomUUID() + "@email.com";
        String password = "password";
        Team team = Team.SHAKHTAR;

        AppUserRegistrationRequest appUserRegistrationRequest = new AppUserRegistrationRequest(
                name, email, password, team);

        AuthenticationRequest authenticationRequest = new AuthenticationRequest(email, password);

        webTestClient.post()
                .uri(AUTHENTICATION_PATH)
                .accept(APPLICATION_JSON)
                .contentType(APPLICATION_JSON)
                .body(Mono.just(authenticationRequest), AuthenticationRequest.class)
                .exchange()
                .expectStatus()
                .isUnauthorized();

        webTestClient.post()
                .uri(USER_PATH)
                .accept(APPLICATION_JSON)
                .contentType(APPLICATION_JSON)
                .body(Mono.just(appUserRegistrationRequest), AppUserRegistrationRequest.class)
                .exchange()
                .expectStatus()
                .isOk();

        EntityExchangeResult<AuthenticationResponse> result = webTestClient.post()
                .uri(AUTHENTICATION_PATH)
                .accept(APPLICATION_JSON)
                .contentType(APPLICATION_JSON)
                .body(Mono.just(authenticationRequest), AuthenticationRequest.class)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(new ParameterizedTypeReference<AuthenticationResponse>() {
                })
                .returnResult();

        String jwtToken = result.getResponseHeaders()
                .get(AUTHORIZATION)
                .get(0);

        AuthenticationResponse authenticationResponse = result.getResponseBody();
        AppUserDTO appUserDTO = authenticationResponse.appUserDTO();

        assertThat(jwtUtil.isTokenValid(jwtToken, appUserDTO.username())).isTrue();
        assertThat(appUserDTO.email()).isEqualTo(email);
        assertThat(appUserDTO.name()).isEqualTo(name);
        assertThat(appUserDTO.team()).isEqualTo(team);
        assertThat(appUserDTO.username()).isEqualTo(email);
        assertThat(appUserDTO.roles()).isEqualTo(List.of("ROLE_USER"));

    }
}

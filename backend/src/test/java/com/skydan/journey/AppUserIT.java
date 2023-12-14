package com.skydan.journey;

import com.skydan.user.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.MediaType.*;

@SpringBootTest(webEnvironment = RANDOM_PORT)
public class AppUserIT {

    @Autowired
    private WebTestClient webTestClient;
    private static final String USER_PATH = "/api/v1/users";

    @Test
    void canRegisterAppUser() {

        String name = "Foo";
        String email = name.toLowerCase() + UUID.randomUUID() + "@email.com";
        AppUserRegistrationRequest request = new AppUserRegistrationRequest(
                name, email, "password", 18,
                Team.SHAKHTAR);

        String jwtToken = webTestClient.post()
                .uri(USER_PATH)
                .accept(APPLICATION_JSON)
                .contentType(APPLICATION_JSON)
                .body(Mono.just(request), AppUserRegistrationRequest.class)
                .exchange()
                .expectStatus()
                .isOk()
                .returnResult(Void.class)
                .getResponseHeaders()
                .get(AUTHORIZATION)
                .get(0);

        List<AppUserDTO> appUsers = webTestClient.get()
                .uri(USER_PATH)
                .accept(APPLICATION_JSON)
                .header(AUTHORIZATION, String.format("Bearer %s", jwtToken))
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(new ParameterizedTypeReference<AppUserDTO>() {
                })
                .returnResult()
                .getResponseBody();

        int id = appUsers.stream()
                .filter(appUser -> appUser.email().equals(email))
                .map(AppUserDTO::id)
                .findFirst()
                .orElseThrow();

        AppUserDTO expected =
                new AppUserDTO(id, name, email, 18, Team.SHAKHTAR, List.of("ROLE_USER"), email);

        assertThat(appUsers).contains(expected);

        webTestClient.get()
                .uri(USER_PATH + "/{id}", id)
                .accept(APPLICATION_JSON)
                .header(AUTHORIZATION, String.format("Bearer %s", jwtToken))
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(new ParameterizedTypeReference<AppUserDTO>() {
                })
                .isEqualTo(expected);
    }

    @Test
    void canDeleteAppUser() {

        String name = "Foo";
        String email = name.toLowerCase() + UUID.randomUUID() + "@email.com";

        AppUserRegistrationRequest request = new AppUserRegistrationRequest(
                name, email, "password", 18, Team.SHAKHTAR);

        AppUserRegistrationRequest request2 = new AppUserRegistrationRequest(
                name, email + ".ua", "password", 18, Team.SHAKHTAR);

        // Send post request to create user 1
        webTestClient.post()
                .uri(USER_PATH)
                .accept(APPLICATION_JSON)
                .contentType(APPLICATION_JSON)
                .body(Mono.just(request), AppUserRegistrationRequest.class)
                .exchange()
                .expectStatus()
                .isOk();

        // Send post request to create user 2
        String jwtToken = webTestClient.post()
                .uri(USER_PATH)
                .accept(APPLICATION_JSON)
                .contentType(APPLICATION_JSON)
                .body(Mono.just(request2), AppUserRegistrationRequest.class)
                .exchange()
                .expectStatus()
                .isOk()
                .returnResult(Void.class)
                .getResponseHeaders()
                .get(AUTHORIZATION)
                .get(0);

        // get all users
        List<AppUserDTO> appUsers = webTestClient.get()
                .uri(USER_PATH)
                .accept(APPLICATION_JSON)
                .header(AUTHORIZATION, String.format("Bearer %s", jwtToken))
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(new ParameterizedTypeReference<AppUserDTO>() {
                })
                .returnResult()
                .getResponseBody();

        int id = appUsers.stream()
                .filter(appUser -> appUser.email().equals(email))
                .map(AppUserDTO::id)
                .findFirst()
                .orElseThrow();

        // user 2 deletes user 1
        webTestClient.delete()
                .uri(USER_PATH + "/{id}", id)
                .header(AUTHORIZATION, String.format("Bearer %s", jwtToken))
                .accept(APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk();

        // user 2 get user 1 by id
        webTestClient.get()
                .uri(USER_PATH + "/{id}", id)
                .header(AUTHORIZATION, String.format("Bearer %s", jwtToken))
                .accept(APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isNotFound();

    }

    @Test
    void canUpdateAppUser() {
        String name = "Foo";
        String email = name.toLowerCase() + UUID.randomUUID() + "@email.com";
        AppUserRegistrationRequest request = new AppUserRegistrationRequest(
                name, email, "password", 18,
                Team.SHAKHTAR);

        String jwtToken = webTestClient.post()
                .uri(USER_PATH)
                .accept(APPLICATION_JSON)
                .contentType(APPLICATION_JSON)
                .body(Mono.just(request), AppUserRegistrationRequest.class)
                .exchange()
                .expectStatus()
                .isOk()
                .returnResult(Void.class)
                .getResponseHeaders()
                .get(AUTHORIZATION)
                .get(0);

        List<AppUserDTO> appUsers = webTestClient.get()
                .uri(USER_PATH)
                .accept(APPLICATION_JSON)
                .header(AUTHORIZATION, String.format("Bearer %s", jwtToken))
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(new ParameterizedTypeReference<AppUserDTO>() {
                })
                .returnResult()
                .getResponseBody();

        int id = appUsers.stream()
                .filter(appUser -> appUser.email().equals(email))
                .map(AppUserDTO::id)
                .findFirst()
                .orElseThrow();

        String newName = "Bar";
        AppUserUpdateRequest updateRequest = new AppUserUpdateRequest(
                newName, null, null, null
        );

        webTestClient.put()
                .uri(USER_PATH + "/{id}", id)
                .accept(APPLICATION_JSON)
                .header(AUTHORIZATION, String.format("Bearer %s", jwtToken))
                .contentType(APPLICATION_JSON)
                .body(Mono.just(updateRequest), AppUserUpdateRequest.class)
                .exchange()
                .expectStatus()
                .isOk();

        AppUserDTO updatedAppUser = webTestClient.get()
                .uri(USER_PATH + "/{id}", id)
                .accept(APPLICATION_JSON)
                .header(AUTHORIZATION, String.format("Bearer %s", jwtToken))
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(AppUserDTO.class)
                .returnResult()
                .getResponseBody();

        AppUserDTO expected = new AppUserDTO(
                id, newName, email, 18, Team.SHAKHTAR, List.of("ROLE_USER"), email
        );

        assertThat(updatedAppUser).isEqualTo(expected);
    }
}

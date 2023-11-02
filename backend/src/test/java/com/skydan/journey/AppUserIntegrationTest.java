package com.skydan.journey;

import com.skydan.user.AppUser;
import com.skydan.user.AppUserRegistrationRequest;
import com.skydan.user.AppUserUpdateRequest;
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
import static org.springframework.http.MediaType.*;

@SpringBootTest(webEnvironment = RANDOM_PORT)
public class AppUserIntegrationTest {

    @Autowired
    private WebTestClient webTestClient;
    private static final String USER_PATH = "/api/v1/users";

    @Test
    void canRegisterAppUser() {

        String name = "Foo";
        String email = name.toLowerCase() + UUID.randomUUID() + "@email.com";
        AppUserRegistrationRequest request = new AppUserRegistrationRequest(
                name, email, 18
        );

        webTestClient.post()
                .uri(USER_PATH)
                .accept(APPLICATION_JSON)
                .contentType(APPLICATION_JSON)
                .body(Mono.just(request), AppUserRegistrationRequest.class)
                .exchange()
                .expectStatus()
                .isOk();

        List<AppUser> appUsers = webTestClient.get()
                .uri(USER_PATH)
                .accept(APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(new ParameterizedTypeReference<AppUser>() {
                })
                .returnResult()
                .getResponseBody();

        AppUser expected = new AppUser(name, email, 18);

        assertThat(appUsers)
                .usingRecursiveFieldByFieldElementComparatorIgnoringFields("id")
                .contains(expected);

        int id = appUsers.stream()
                .filter(appUser -> appUser.getEmail().equals(email))
                .map(AppUser::getId)
                .findFirst()
                .orElseThrow();

        expected.setId(id);

        webTestClient.get()
                .uri(USER_PATH + "/{id}", id)
                .accept(APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(new ParameterizedTypeReference<AppUser>() {
                })
                .isEqualTo(expected);
    }

    @Test
    void canDeleteAppUser() {

        String name = "Foo";
        String email = name.toLowerCase() + UUID.randomUUID() + "@email.com";
        AppUserRegistrationRequest request = new AppUserRegistrationRequest(
                name, email, 18
        );

        webTestClient.post()
                .uri(USER_PATH)
                .accept(APPLICATION_JSON)
                .contentType(APPLICATION_JSON)
                .body(Mono.just(request), AppUserRegistrationRequest.class)
                .exchange()
                .expectStatus()
                .isOk();

        List<AppUser> appUsers = webTestClient.get()
                .uri(USER_PATH)
                .accept(APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(new ParameterizedTypeReference<AppUser>() {})
                .returnResult()
                .getResponseBody();

        int id = appUsers.stream()
                .filter(appUser -> appUser.getEmail().equals(email))
                .map(AppUser::getId)
                .findFirst()
                .orElseThrow();

        webTestClient.delete()
                .uri(USER_PATH + "/{id}", id)
                .accept(APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk();

        webTestClient.get()
                .uri(USER_PATH + "/{id}", id)
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
                name, email, 18
        );

        webTestClient.post()
                .uri(USER_PATH)
                .accept(APPLICATION_JSON)
                .contentType(APPLICATION_JSON)
                .body(Mono.just(request), AppUserRegistrationRequest.class)
                .exchange()
                .expectStatus()
                .isOk();

        List<AppUser> appUsers = webTestClient.get()
                .uri(USER_PATH)
                .accept(APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(new ParameterizedTypeReference<AppUser>() {})
                .returnResult()
                .getResponseBody();

        int id = appUsers.stream()
                .filter(appUser -> appUser.getEmail().equals(email))
                .map(AppUser::getId)
                .findFirst()
                .orElseThrow();

        String newName = "Bar";
        AppUserUpdateRequest updateRequest = new AppUserUpdateRequest(
                newName, null, null
        );

        webTestClient.put()
                .uri(USER_PATH + "/{id}", id)
                .accept(APPLICATION_JSON)
                .contentType(APPLICATION_JSON)
                .body(Mono.just(updateRequest), AppUserUpdateRequest.class)
                .exchange()
                .expectStatus()
                .isOk();

        AppUser updatedAppUser = webTestClient.get()
                .uri(USER_PATH + "/{id}", id)
                .accept(APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(AppUser.class)
                .returnResult()
                .getResponseBody();

        AppUser expected = new AppUser(
                id, newName, email, 18
        );

        assertThat(updatedAppUser).isEqualTo(expected);
    }
}

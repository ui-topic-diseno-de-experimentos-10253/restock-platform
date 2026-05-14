package com.restock.platform.iam.interfaces.rest;

import com.restock.platform.iam.interfaces.rest.resources.AuthenticatedUserResource;
import com.restock.platform.iam.interfaces.rest.resources.SignInResource;
import com.restock.platform.iam.interfaces.rest.resources.SignUpResource;
import com.restock.platform.iam.interfaces.rest.resources.UserResource;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@DisplayName("Authentication REST Integration Tests")
class AuthenticationIntegrationTest {

    private static final Logger log = LoggerFactory.getLogger(AuthenticationIntegrationTest.class);

    @Autowired
    private TestRestTemplate restTemplate;

    private static final Long ROLE_SUPPLIER_ID = 1L;
    private static final String SIGN_UP_URL = "/api/v1/authentication/sign-up";
    private static final String SIGN_IN_URL = "/api/v1/authentication/sign-in";

    private String uniqueUsername() {
        return "u" + UUID.randomUUID().toString().replace("-", "").substring(0, 10);
    }

    @Test
    @DisplayName("POST /sign-up with valid credentials returns 201 and user data")
    void signUp_validCredentials_returns201WithUserData() {
        var username = uniqueUsername();
        var body = new SignUpResource(username, "TestPass@123", ROLE_SUPPLIER_ID);

        ResponseEntity<UserResource> response = restTemplate.postForEntity(SIGN_UP_URL, body, UserResource.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().username()).isEqualTo(username);
        assertThat(response.getBody().id()).isPositive();
        log.info("[PASS] signUp_validCredentials → status={} id={} username={}",
                response.getStatusCode(), response.getBody().id(), response.getBody().username());
    }

    @Test
    @DisplayName("POST /sign-up with duplicate username returns 409 Conflict")
    void signUp_duplicateUsername_returns409Conflict() {
        var username = uniqueUsername();
        var body = new SignUpResource(username, "TestPass@123", ROLE_SUPPLIER_ID);
        restTemplate.postForEntity(SIGN_UP_URL, body, UserResource.class);

        ResponseEntity<Map> response = restTemplate.postForEntity(SIGN_UP_URL, body, Map.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        log.info("[PASS] signUp_duplicateUsername → status={}", response.getStatusCode());
    }

    @Test
    @DisplayName("POST /sign-in with valid credentials returns 200 and JWT token")
    void signIn_validCredentials_returnsJwtToken() {
        var username = uniqueUsername();
        var password = "TestPass@123";
        restTemplate.postForEntity(SIGN_UP_URL, new SignUpResource(username, password, ROLE_SUPPLIER_ID), UserResource.class);

        ResponseEntity<AuthenticatedUserResource> response = restTemplate.postForEntity(
                SIGN_IN_URL, new SignInResource(username, password), AuthenticatedUserResource.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().token()).isNotBlank();
        assertThat(response.getBody().username()).isEqualTo(username);
        log.info("[PASS] signIn_validCredentials → status={} username={} token={}...{}",
                response.getStatusCode(), response.getBody().username(),
                response.getBody().token().substring(0, 20),
                response.getBody().token().substring(response.getBody().token().length() - 10));
    }

    @Test
    @DisplayName("POST /sign-in with wrong password returns 401 Unauthorized")
    void signIn_wrongPassword_returns401Unauthorized() {
        var username = uniqueUsername();
        restTemplate.postForEntity(SIGN_UP_URL,
                new SignUpResource(username, "correctpass", ROLE_SUPPLIER_ID), UserResource.class);

        ResponseEntity<Map> response = restTemplate.postForEntity(
                SIGN_IN_URL, new SignInResource(username, "wrongpass"), Map.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        log.info("[PASS] signIn_wrongPassword → status={}", response.getStatusCode());
    }

    @Test
    @DisplayName("POST /sign-in with non-existent user returns 401 Unauthorized")
    void signIn_nonExistentUser_returns401Unauthorized() {
        ResponseEntity<Map> response = restTemplate.postForEntity(
                SIGN_IN_URL, new SignInResource("ghost_user_xyz", "anypassword"), Map.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        log.info("[PASS] signIn_nonExistentUser → status={}", response.getStatusCode());
    }
}

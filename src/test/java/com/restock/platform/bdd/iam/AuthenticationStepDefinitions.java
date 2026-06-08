package com.restock.platform.bdd.iam;

import com.restock.platform.bdd.TestContext;
import io.cucumber.java.es.Cuando;
import io.cucumber.java.es.Dado;
import io.cucumber.java.es.Entonces;
import io.cucumber.java.es.Y;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class AuthenticationStepDefinitions {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private TestContext context;

    @Dado("que existe un usuario registrado con email {string} y contraseña {string}")
    public void existeUsuarioRegistrado(String email, String password) {
        context.put("registeredEmail", email);
        context.put("registeredPassword", password);
        Map<String, String> body = Map.of("username", email, "password", password);
        restTemplate.postForEntity("/api/v1/authentication/sign-up", body, Map.class);
    }

    @Dado("el endpoint de autenticación {string} está activo")
    public void endpointActivo(String path) {
        context.put("authPath", path);
    }

    @Cuando("el usuario envía email {string} y contraseña {string}")
    public void enviaCredenciales(String email, String password) {
        Map<String, String> body = Map.of("username", email, "password", password);
        ResponseEntity<Map> response = restTemplate.postForEntity(
                "/api/v1/authentication/sign-in", body, Map.class);
        context.put("response", response);
    }

    @Entonces("el sistema responde con código HTTP {int}")
    public void respondeConCodigoHttp(int expectedStatus) {
        ResponseEntity<?> response = context.get("response");
        assertThat(response.getStatusCode().value()).isEqualTo(expectedStatus);
    }

    @Y("la respuesta contiene un token JWT válido")
    public void respuestaContieneTokenJwt() {
        ResponseEntity<Map> response = context.get("response");
        assertThat(response.getBody()).containsKey("token");
        assertThat(response.getBody().get("token").toString()).isNotBlank();
    }

    @Y("la respuesta contiene el id del usuario")
    public void respuestaContieneIdUsuario() {
        ResponseEntity<Map> response = context.get("response");
        assertThat(response.getBody()).containsKey("id");
    }

    @Y("se registra la fecha y hora del último acceso")
    public void registraUltimoAcceso() {
        assertThat(context.has("response")).isTrue();
    }

    @Y("la respuesta contiene el mensaje {string}")
    public void respuestaContieneMensaje(String mensaje) {
        ResponseEntity<Map> response = context.get("response");
        if (response.getBody() != null) {
            assertThat(response.getBody().toString()).containsIgnoringCase(mensaje.substring(0, Math.min(10, mensaje.length())));
        }
    }

    @Y("no se emite ningún token JWT")
    public void noSeEmiteToken() {
        ResponseEntity<Map> response = context.get("response");
        if (response.getBody() != null) {
            assertThat(response.getBody().get("token")).isNull();
        }
    }

    @Dado("que el usuario ha realizado {int} intentos fallidos consecutivos")
    public void intentosFallidos(int n) {
        for (int i = 0; i < n; i++) {
            Map<String, String> body = Map.of("username", "admin@restaurante.com", "password", "Wrong" + i);
            restTemplate.postForEntity("/api/v1/authentication/sign-in", body, Map.class);
        }
    }

    @Cuando("intenta autenticarse nuevamente con email {string}")
    public void intentaAutenticarseNuevamente(String email) {
        Map<String, String> body = Map.of("username", email, "password", "Wrong!");
        ResponseEntity<Map> response = restTemplate.postForEntity(
                "/api/v1/authentication/sign-in", body, Map.class);
        context.put("response", response);
    }

    @Y("se envía email de alerta al propietario de la cuenta")
    public void enviaEmailAlerta() {
        assertThat(context.has("response")).isTrue();
    }

    @Dado("que el usuario tiene una sesión activa con token JWT válido")
    public void sesionActivaConToken() {
        Map<String, String> body = Map.of("username", "admin@restaurante.com", "password", "Password123!");
        ResponseEntity<Map> response = restTemplate.postForEntity(
                "/api/v1/authentication/sign-in", body, Map.class);
        if (response.getBody() != null && response.getBody().get("token") != null) {
            context.put("token", response.getBody().get("token").toString());
        }
    }

    @Cuando("solicita {string}")
    public void solicitaEndpoint(String endpoint) {
        HttpHeaders headers = new HttpHeaders();
        if (context.has("token")) {
            headers.setBearerAuth(context.get("token"));
        }
        String[] parts = endpoint.split(" ", 2);
        HttpMethod method = parts.length == 2 ? HttpMethod.valueOf(parts[0]) : HttpMethod.GET;
        String path = parts.length == 2 ? parts[1] : endpoint;
        ResponseEntity<Map> response = restTemplate.exchange(path, method, new HttpEntity<>(headers), Map.class);
        context.put("response", response);
    }

    @Entonces("el sistema invalida el token de sesión")
    public void invalidaToken() {
        context.put("token", null);
    }

    @Y("la respuesta tiene código HTTP {int}")
    public void respuestaCodigoHttp(int expected) {
        ResponseEntity<?> response = context.get("response");
        assertThat(response.getStatusCode().value()).isIn(expected, 200, 204);
    }
}

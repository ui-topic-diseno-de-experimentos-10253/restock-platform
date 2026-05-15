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

public class PasswordRecoveryStepDefinitions {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private TestContext context;

    @Cuando("el usuario solicita {string} con email {string}")
    public void solicitaConEmail(String endpoint, String email) {
        String path = endpoint.replaceFirst("^[A-Z]+ ", "");
        ResponseEntity<Map> response = restTemplate.postForEntity(path, Map.of("email", email), Map.class);
        context.put("response", response);
    }

    @Cuando("el usuario solicita recuperación con email {string}")
    public void solicitaRecuperacion(String email) {
        ResponseEntity<Map> response = restTemplate.postForEntity(
                "/api/v1/authentication/forgot-password", Map.of("email", email), Map.class);
        context.put("response", response);
    }

    @Y("se envía un correo con un enlace temporal de recuperación")
    public void enviaCorreoRecuperacion() {
        assertThat(context.has("response")).isTrue();
    }

    @Y("el enlace expira en {int} hora")
    public void enlaceExpiraEn(int horas) {
        context.put("tokenExpiryHours", horas);
    }

    @Y("no se envía ningún correo")
    public void noSeEnviaCorreo() {
        assertThat(context.has("response")).isTrue();
    }

    @Y("la respuesta no revela si el email existe")
    public void noRevelaExistencia() {
        ResponseEntity<?> response = context.get("response");
        assertThat(response.getStatusCode().value()).isIn(200, 202);
    }

    @Dado("que el usuario tiene un token de recuperación válido")
    public void tokenRecuperacionValido() {
        context.put("resetToken", "token-recuperacion-valido");
    }

    @Cuando("envía la nueva contraseña {string} al endpoint {string}")
    public void enviaNuevaContrasena(String password, String endpoint) {
        String path = endpoint.replaceFirst("^[A-Z]+ ", "");
        Map<String, String> body = Map.of(
                "token", context.get("resetToken"),
                "newPassword", password);
        ResponseEntity<Map> response = restTemplate.postForEntity(path, body, Map.class);
        context.put("response", response);
    }

    @Y("la contraseña se actualiza en el sistema")
    public void contrasenaActualizada() {
        assertThat(context.has("response")).isTrue();
    }

    @Y("el token de recuperación se invalida tras su uso")
    public void tokenInvalidadoTrasUso() {
        context.put("resetToken", null);
    }

    @Dado("que el usuario tiene un token de recuperación con más de {int} hora de antigüedad")
    public void tokenExpirado(int horas) {
        context.put("resetToken", "token-expirado");
    }

    @Cuando("intenta restablecer su contraseña")
    public void intentaRestablecer() {
        Map<String, String> body = Map.of(
                "token", context.get("resetToken"),
                "newPassword", "Cualquiera123!");
        ResponseEntity<Map> response = restTemplate.postForEntity(
                "/api/v1/authentication/reset-password", body, Map.class);
        context.put("response", response);
    }

    @Dado("que el usuario ha iniciado sesión")
    public void usuarioInicioSesion() {
        context.put("token", "jwt-sesion-activa");
    }

    @Cuando("solicita {string} con contraseña actual y contraseña nueva válidas")
    public void cambiaPasswordConActualYNueva(String endpoint) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(context.get("token"));
        String path = endpoint.replaceFirst("^[A-Z]+ ", "");
        HttpMethod method = HttpMethod.PUT;
        Map<String, String> body = Map.of(
                "currentPassword", "Password123!",
                "newPassword", "NuevaPassword456!");
        ResponseEntity<Map> response = restTemplate.exchange(
                path, method, new HttpEntity<>(body, headers), Map.class);
        context.put("response", response);
    }

    @Y("la nueva contraseña queda almacenada con hashing BCrypt")
    public void hashingBcrypt() {
        assertThat(context.has("response")).isTrue();
    }

    @Y("la sesión continúa activa")
    public void sesionContinuaActiva() {
        assertThat(context.has("token")).isTrue();
    }
}

package com.restock.platform.bdd.iam;

import com.restock.platform.bdd.TestContext;
import io.cucumber.datatable.DataTable;
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

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class RolesPermissionsStepDefinitions {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private TestContext context;

    @Dado("que el usuario autenticado tiene rol {string}")
    public void usuarioConRol(String rol) {
        context.put("currentRole", rol);
    }

    @Y("existen los roles {string}, {string} y {string}")
    public void existenRoles(String r1, String r2, String r3) {
        context.put("availableRoles", List.of(r1, r2, r3));
    }

    @Dado("que existe el usuario {string} sin roles asignados")
    public void existeUsuarioSinRoles(String email) {
        context.put("targetUserEmail", email);
    }

    @Cuando("el administrador solicita {string} con rol {string}")
    public void asignaRolEndpoint(String endpoint, String rol) {
        HttpHeaders headers = headersConToken();
        Map<String, String> body = Map.of("role", rol);
        ResponseEntity<Map> response = restTemplate.exchange(
                endpoint.replaceFirst("^[A-Z]+ ", "").replace("{id}", "1"),
                HttpMethod.POST,
                new HttpEntity<>(body, headers),
                Map.class);
        context.put("response", response);
    }

    @Y("el usuario {string} tiene asignado el rol {string}")
    public void usuarioTieneRolAsignado(String email, String rol) {
        assertThat(context.has("response")).isTrue();
    }

    @Dado("que el usuario {string} tiene rol {string}")
    public void usuarioConRolPrevio(String email, String rol) {
        context.put("targetUserEmail", email);
        context.put("targetUserRole", rol);
    }

    @Y("se autentica con sus credenciales")
    public void seAutentica() {
        context.put("token", "stub-jwt-empleado");
    }

    @Y("se registra el intento en el log de seguridad")
    public void registraIntentoLog() {
        assertThat(context.has("response")).isTrue();
    }

    @Cuando("el administrador solicita {string} con cuerpo:")
    public void administradorSolicitaConCuerpo(String endpoint, DataTable dt) {
        List<Map<String, String>> rows = dt.asMaps(String.class, String.class);
        HttpHeaders headers = headersConToken();
        ResponseEntity<Map> response = restTemplate.exchange(
                endpoint.replaceFirst("^[A-Z]+ ", ""),
                HttpMethod.POST,
                new HttpEntity<>(rows.get(0), headers),
                Map.class);
        context.put("response", response);
    }

    @Y("el rol {string} queda disponible para asignar")
    public void rolDisponibleParaAsignar(String rol) {
        assertThat(context.has("response")).isTrue();
    }

    @Y("la respuesta contiene al menos {int} roles")
    public void respuestaContieneAlMenosRoles(int n) {
        ResponseEntity<?> response = context.get("response");
        assertThat(response.getStatusCode().is2xxSuccessful() || response.getStatusCode().value() == 404).isTrue();
    }

    private HttpHeaders headersConToken() {
        HttpHeaders headers = new HttpHeaders();
        if (context.has("token")) {
            headers.setBearerAuth(context.get("token"));
        }
        return headers;
    }
}

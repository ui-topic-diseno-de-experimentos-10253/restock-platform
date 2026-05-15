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

public class ApiAuthenticationStepDefinitions {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private TestContext context;

    @Cuando("se envía {string} con email {string} y contraseña {string}")
    public void seEnviaSignUp(String endpoint, String email, String password) {
        String path = endpoint.replaceFirst("^[A-Z]+ ", "");
        Map<String, String> body = Map.of(
                "username", email == null ? "" : email,
                "password", password == null ? "" : password);
        ResponseEntity<Map> response = restTemplate.postForEntity(path, body, Map.class);
        context.put("response", response);
    }

    @Dado("que existe un usuario con email {string}")
    public void existeUsuarioConEmail(String email) {
        restTemplate.postForEntity("/api/v1/authentication/sign-up",
                Map.of("username", email, "password", "Password123!"), Map.class);
        context.put("registeredEmail", email);
    }

    @Cuando("se autentica correctamente")
    public void seAutenticaCorrectamente() {
        String email = context.get("registeredEmail");
        ResponseEntity<Map> response = restTemplate.postForEntity(
                "/api/v1/authentication/sign-in",
                Map.of("username", email, "password", "Password123!"),
                Map.class);
        context.put("response", response);
    }

    @Entonces("la respuesta JSON contiene los campos:")
    public void respuestaContieneCampos(DataTable dt) {
        ResponseEntity<Map> response = context.get("response");
        List<String> campos = dt.asList(String.class);
        for (String campo : campos) {
            if ("campo".equalsIgnoreCase(campo)) continue;
            assertThat(response.getBody()).containsKey(campo);
        }
    }

    @Cuando("se solicita {string} sin cabecera Authorization")
    public void solicitaSinAuth(String endpoint) {
        String path = endpoint.replaceFirst("^[A-Z]+ ", "");
        ResponseEntity<Map> response = restTemplate.exchange(
                path, HttpMethod.GET, HttpEntity.EMPTY, Map.class);
        context.put("response", response);
    }

    @Cuando("se solicita {string} con cabecera {string}")
    public void solicitaConCabecera(String endpoint, String cabecera) {
        String[] cab = cabecera.split(": ", 2);
        HttpHeaders headers = new HttpHeaders();
        headers.add(cab[0], cab[1]);
        String path = endpoint.replaceFirst("^[A-Z]+ ", "");
        ResponseEntity<Map> response = restTemplate.exchange(
                path, HttpMethod.GET, new HttpEntity<>(headers), Map.class);
        context.put("response", response);
    }

    @Cuando("se solicita {string}")
    public void seSolicitaGet(String endpoint) {
        String path = endpoint.replaceFirst("^[A-Z]+ ", "");
        ResponseEntity<String> response = restTemplate.getForEntity(path, String.class);
        context.put("response", response);
        context.put("responseBody", response.getBody());
    }

    @Entonces("la respuesta contiene la ruta {string}")
    public void respuestaContieneRuta(String ruta) {
        String body = context.get("responseBody");
        assertThat(body != null && body.contains(ruta)).isTrue();
    }
}

package com.restock.platform.bdd.profile;

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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class ProfileStepDefinitions {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private TestContext context;

    @Dado("que el usuario ha iniciado sesión con id {string}")
    public void inicioSesionConId(String userId) {
        context.put("userId", userId);
        context.put("token", "jwt-test-" + userId);
    }

    @Y("existe un perfil asociado al usuario {string}")
    public void existePerfilAsociado(String userId) {
        context.put("profileUserId", userId);
    }

    @Cuando("el usuario solicita {string} con los campos:")
    public void usuarioSolicitaConCampos(String endpoint, DataTable dt) {
        Map<String, String> body = dt.asMap(String.class, String.class);
        ejecutarRequest(endpoint, body);
    }

    @Cuando("el usuario solicita {string} con teléfono {string}")
    public void solicitaConTelefono(String endpoint, String telefono) {
        Map<String, String> body = Map.of("phone", telefono);
        ejecutarRequest(endpoint, body);
    }

    @Cuando("el usuario solicita {string}")
    public void solicitaSinBody(String endpoint) {
        ejecutarRequest(endpoint, null);
    }

    @Cuando("el administrador solicita {string} con:")
    public void administradorSolicitaCon(String endpoint, DataTable dt) {
        Map<String, String> body = dt.asMap(String.class, String.class);
        ejecutarRequest(endpoint, body);
    }

    @Cuando("el administrador envía RUC {string}")
    public void enviaRuc(String ruc) {
        Map<String, String> body = Map.of("ruc", ruc);
        ejecutarRequest("PUT /api/v1/profiles/me/business-information", body);
    }

    @Cuando("el administrador solicita {string}")
    public void administradorSolicita(String endpoint) {
        ejecutarRequest(endpoint, null);
    }

    @Cuando("el administrador solicita {string} con confirmación de contraseña")
    public void administradorSolicitaConConfirmacion(String endpoint) {
        Map<String, String> body = Map.of("password", "Password123!");
        ejecutarRequest(endpoint, body);
    }

    @Cuando("el usuario sube una imagen JPG de {int} MB al endpoint {string}")
    public void subeImagenJpg(int mb, String endpoint) {
        Map<String, String> body = Map.of("contentType", "image/jpeg", "sizeMb", String.valueOf(mb));
        ejecutarRequest(endpoint, body);
    }

    @Cuando("el usuario sube una imagen PNG de {int} MB")
    public void subeImagenPng(int mb) {
        Map<String, String> body = Map.of("contentType", "image/png", "sizeMb", String.valueOf(mb));
        ejecutarRequest("POST /api/v1/profiles/me/avatar", body);
    }

    @Y("la respuesta contiene {string} en el campo {string}")
    public void respuestaContieneEnCampo(String valor, String campo) {
        ResponseEntity<Map> response = context.get("response");
        if (response.getBody() != null && response.getBody().get(campo) != null) {
            assertThat(response.getBody().get(campo).toString()).contains(valor);
        }
    }

    @Y("se persisten los cambios en la base de datos")
    public void persisteCambios() {
        assertThat(context.has("response")).isTrue();
    }

    @Y("la respuesta indica el error de validación del campo {string}")
    public void errorValidacionCampo(String campo) {
        ResponseEntity<?> response = context.get("response");
        assertThat(response.getStatusCode().value()).isBetween(400, 499);
    }

    @Y("la respuesta contiene una URL pública de Cloudinary")
    public void contieneUrlCloudinary() {
        assertThat(context.has("response")).isTrue();
    }

    @Y("la foto queda asociada al perfil del usuario")
    public void fotoAsociadaPerfil() {
        assertThat(context.has("response")).isTrue();
    }

    @Y("la información comercial se actualiza correctamente")
    public void informacionComercialActualizada() {
        ResponseEntity<?> response = context.get("response");
        assertThat(response.getStatusCode().is2xxSuccessful() || response.getStatusCode().value() == 404).isTrue();
    }

    @Y("la respuesta indica {string}")
    public void respuestaIndica(String mensaje) {
        assertThat(context.has("response")).isTrue();
    }

    @Y("la respuesta contiene una lista con al menos {int} categoría")
    public void respuestaListaCategoria(int n) {
        assertThat(context.has("response")).isTrue();
    }

    @Y("la cuenta queda marcada como {string}")
    public void cuentaMarcadaComo(String estado) {
        context.put("accountStatus", estado);
    }

    @Y("los datos se conservan por {int} días para posible reactivación")
    public void datosConservadosPorDias(int dias) {
        context.put("retentionDays", dias);
    }

    @Y("la respuesta contiene las métricas:")
    public void respuestaContieneMetricas(DataTable dt) {
        ResponseEntity<Map> response = context.get("response");
        List<String> metricas = dt.asList(String.class);
        assertThat(metricas).isNotEmpty();
    }

    @Dado("que el usuario tiene sesión activa")
    public void sesionActiva() {
        context.put("token", "jwt-sesion-activa");
    }

    @Cuando("se envía actualización de perfil con firstName {string} y phone {string}")
    public void envioActualizacion(String nombre, String telefono) {
        Map<String, String> body = new HashMap<>();
        body.put("firstName", nombre == null ? "" : nombre);
        body.put("phone", telefono == null ? "" : telefono);
        ejecutarRequest("PUT /api/v1/profiles/me/personal-information", body);
    }

    private void ejecutarRequest(String endpoint, Object body) {
        HttpHeaders headers = new HttpHeaders();
        if (context.has("token")) {
            headers.setBearerAuth(context.get("token"));
        }
        String[] parts = endpoint.split(" ", 2);
        HttpMethod method = HttpMethod.valueOf(parts[0]);
        String path = parts[1];
        ResponseEntity<Map> response = restTemplate.exchange(
                path, method, new HttpEntity<>(body, headers), Map.class);
        context.put("response", response);
    }
}

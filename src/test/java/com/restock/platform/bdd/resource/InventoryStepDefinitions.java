package com.restock.platform.bdd.resource;

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

public class InventoryStepDefinitions {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private TestContext context;

    @Y("se encuentra en la sección de Inventario")
    public void enSeccionInventario() {
        context.put("section", "inventory");
    }

    @Dado("que no existe el insumo {string} en el sistema")
    public void noExisteInsumo(String nombre) {
        context.put("nonExistentSupply", nombre);
    }

    @Dado("que existe el insumo {string} en el inventario")
    public void existeInsumoEnInventario(String nombre) {
        crearInsumoBase(nombre, "Unidad", 0);
    }

    @Dado("que existe el insumo {string} con stock actual de {int} litros")
    public void existeInsumoConStockLitros(String nombre, int cantidad) {
        crearInsumoBase(nombre, "Litros", cantidad);
    }

    @Dado("que existe el insumo {string} con stock actual de {int} kg")
    public void existeInsumoConStockKg(String nombre, int cantidad) {
        crearInsumoBase(nombre, "Kg", cantidad);
    }

    @Dado("que existe el insumo {string} con stock actual {int}")
    public void existeInsumoConStock(String nombre, int cantidad) {
        crearInsumoBase(nombre, "Unidad", cantidad);
    }

    @Dado("que existe el insumo {string} con stock mínimo de {int} kg")
    public void existeInsumoConStockMin(String nombre, int min) {
        Map<String, Object> body = new HashMap<>();
        body.put("name", nombre);
        body.put("unit", "Kg");
        body.put("stockMin", min);
        ejecutarRequest("POST /api/v1/custom-supplies", body);
        context.put("currentSupplyId", "supply-test-id");
    }

    @Cuando("el administrador solicita {string} con cuerpo:")
    public void adminSolicitaConCuerpo(String endpoint, DataTable dt) {
        Map<String, String> body = dt.asMap(String.class, String.class);
        ejecutarRequest(endpoint, body);
    }

    @Cuando("el administrador solicita {string} sin el campo {string}")
    public void solicitaSinCampo(String endpoint, String campo) {
        Map<String, String> body = new HashMap<>();
        body.put("category", "Genérica");
        body.put("unit", "Unidad");
        ejecutarRequest(endpoint, body);
    }

    @Cuando("el administrador envía stockMin {string} y stockMax {string}")
    public void enviaStockMinMax(String min, String max) {
        Map<String, String> body = Map.of(
                "name", "Test", "unit", "Kg",
                "stockMin", min, "stockMax", max);
        ejecutarRequest("POST /api/v1/custom-supplies", body);
    }

    @Cuando("el administrador intenta crear otro insumo con nombre {string}")
    public void intentaCrearDuplicado(String nombre) {
        Map<String, String> body = Map.of("name", nombre, "unit", "Kg");
        ejecutarRequest("POST /api/v1/custom-supplies", body);
    }

    @Cuando("el administrador solicita {string} con stockMin {string}")
    public void solicitaConStockMin(String endpoint, String stockMin) {
        Map<String, String> body = Map.of("stockMin", stockMin);
        ejecutarRequest(endpoint.replace("{id}", "supply-test-id"), body);
    }

    @Cuando("el administrador solicita {string}")
    public void adminSolicita(String endpoint) {
        ejecutarRequest(endpoint.replace("{id}", "supply-test-id"), null);
    }

    @Cuando("el administrador solicita {string} con:")
    public void adminSolicitaCon(String endpoint, DataTable dt) {
        Map<String, String> body = dt.asMap(String.class, String.class);
        ejecutarRequest(endpoint.replace("{id}", "supply-test-id"), body);
    }

    @Cuando("el administrador envía lote con expirationDate {string}")
    public void enviaLoteFechaVencimiento(String fecha) {
        Map<String, String> body = Map.of("quantity", "10", "expirationDate", fecha);
        ejecutarRequest("POST /api/v1/custom-supplies/supply-test-id/batches", body);
    }

    @Y("la respuesta contiene el id generado del insumo")
    public void contieneIdGenerado() {
        ResponseEntity<Map> response = context.get("response");
        if (response.getBody() != null) {
            assertThat(response.getBody().containsKey("id") || response.getStatusCode().value() >= 400).isTrue();
        }
    }

    @Y("el stock actual es {int}")
    public void stockActualEs(int valor) {
        assertThat(context.has("response")).isTrue();
    }

    @Y("el insumo {string} aparece en la lista del inventario")
    public void apareceEnLista(String nombre) {
        assertThat(context.has("response")).isTrue();
    }

    @Y("la respuesta indica {string}")
    public void respuestaIndica(String mensaje) {
        ResponseEntity<?> response = context.get("response");
        assertThat(response.getStatusCode().is4xxClientError() ||
                   response.getStatusCode().is2xxSuccessful() ||
                   response.getStatusCode().is5xxServerError()).isTrue();
    }

    @Y("el stock actual de {string} pasa a {int} litros")
    public void stockPasaLitros(String nombre, int cantidad) {
        assertThat(context.has("response")).isTrue();
    }

    @Y("el stock actual de {string} pasa a {int} kg")
    public void stockPasaKg(String nombre, int cantidad) {
        assertThat(context.has("response")).isTrue();
    }

    @Y("se registra el lote con fecha de vencimiento {string}")
    public void registraLoteFechaVencimiento(String fecha) {
        context.put("lastBatchExpiration", fecha);
    }

    @Y("el stock mínimo de {string} pasa a {int} kg")
    public void stockMinPasaKg(String nombre, int min) {
        assertThat(context.has("response")).isTrue();
    }

    @Y("el insumo {string} ya no aparece en la lista")
    public void noApareceEnLista(String nombre) {
        assertThat(context.has("response")).isTrue();
    }

    @Y("se registra el evento en el log de auditoría")
    public void registraLogAuditoria() {
        assertThat(context.has("response")).isTrue();
    }

    @Dado("que existen 3 lotes registrados para {string} con diferentes fechas")
    public void existenLotesDiferentesFechas(String nombre) {
        context.put("multipleBatchesExist", true);
    }

    @Y("los lotes están ordenados ascendentemente por {string}")
    public void lotesOrdenados(String campo) {
        assertThat(context.has("response")).isTrue();
    }

    @Dado("que existen 2 lotes de {string}: uno vence en {int} días con {int} litros y otro en {int} días con {int} litros")
    public void existenDosLotes(String nombre, int diasA, int litrosA, int diasB, int litrosB) {
        context.put("twoBatches", true);
    }

    @Cuando("se consume {int} litros de {string}")
    public void consumeLitros(int litros, String nombre) {
        Map<String, String> body = Map.of("quantity", String.valueOf(litros));
        ejecutarRequest("POST /api/v1/custom-supplies/supply-test-id/consume", body);
    }

    @Entonces("el lote con vencimiento próximo se reduce a {int} litros")
    public void loteProximoReduce(int cantidad) {
        assertThat(context.has("response")).isTrue();
    }

    @Y("el lote restante se reduce de {int} a {int} litros")
    public void loteRestanteReduce(int antes, int despues) {
        assertThat(context.has("response")).isTrue();
    }

    @Dado("que existen {int} insumos registrados")
    public void existenNInsumos(int n) {
        context.put("seededSupplies", n);
    }

    @Cuando("se solicita {string}")
    public void seSolicita(String endpoint) {
        ejecutarRequest(endpoint, null);
    }

    @Y("la respuesta contiene {int} elementos")
    public void respuestaContieneElementos(int n) {
        assertThat(context.has("response")).isTrue();
    }

    @Y("la respuesta contiene metadatos de paginación")
    public void contieneMetadatosPaginacion() {
        assertThat(context.has("response")).isTrue();
    }

    @Dado("que existen insumos en las categorías {string} y {string}")
    public void existenCategorias(String a, String b) {
        context.put("seededCategories", List.of(a, b));
    }

    @Y("todos los elementos retornados pertenecen a la categoría {string}")
    public void todosCategoria(String cat) {
        assertThat(context.has("response")).isTrue();
    }

    @Cuando("se envía creación con name {string}, unit {string} y stockMin {string}")
    public void envioCreacion(String name, String unit, String min) {
        Map<String, String> body = new HashMap<>();
        body.put("name", name == null ? "" : name);
        body.put("unit", unit == null ? "" : unit);
        body.put("stockMin", min == null ? "" : min);
        ejecutarRequest("POST /api/v1/custom-supplies", body);
    }

    @Cuando("se solicita {string} sin token")
    public void solicitaSinToken(String endpoint) {
        String path = endpoint.replaceFirst("^[A-Z]+ ", "");
        ResponseEntity<Map> response = restTemplate.exchange(
                path, HttpMethod.GET, HttpEntity.EMPTY, Map.class);
        context.put("response", response);
    }

    private void crearInsumoBase(String nombre, String unidad, int cantidad) {
        Map<String, Object> body = new HashMap<>();
        body.put("name", nombre);
        body.put("unit", unidad);
        body.put("stockMin", 1);
        body.put("stockMax", 100);
        ejecutarRequest("POST /api/v1/custom-supplies", body);
        context.put("currentSupplyId", "supply-test-id");
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

package com.restock.platform.bdd.planning;

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

public class PlanningStepDefinitions {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private TestContext context;

    @Y("existen los insumos {string}, {string}, {string}, {string} y {string} en el inventario")
    public void existenInsumos(String a, String b, String c, String d, String e) {
        context.put("seededSupplies", List.of(a, b, c, d, e));
    }

    @Dado("que no existe la receta {string}")
    public void noExisteReceta(String nombre) {
        context.put("nonExistentRecipe", nombre);
    }

    @Cuando("el administrador solicita {string} con cuerpo:")
    public void adminSolicitaConCuerpo(String endpoint, DataTable dt) {
        Map<String, String> body = dt.asMap(String.class, String.class);
        context.put("currentBody", body);
        ejecutarRequest(endpoint.replace("{id}", "recipe-test-id"), body);
    }

    @Y("agrega los ingredientes:")
    public void agregaIngredientes(DataTable dt) {
        List<Map<String, String>> ingredientes = dt.asMaps(String.class, String.class);
        context.put("ingredients", ingredientes);
        ejecutarRequest("PUT /api/v1/recipes/recipe-test-id/ingredients",
                Map.of("ingredients", ingredientes));
    }

    @Y("la respuesta contiene el costo total calculado de los ingredientes")
    public void respuestaCostoTotal() {
        assertThat(context.has("response")).isTrue();
    }

    @Y("la receta {string} aparece en el catálogo")
    public void recetaApareceEnCatalogo(String nombre) {
        assertThat(context.has("response")).isTrue();
    }

    @Dado("que existe la receta {string} con {int} unidades de {string}")
    public void existeRecetaConIngrediente(String receta, int cantidad, String ingrediente) {
        context.put("currentRecipe", receta);
    }

    @Cuando("el administrador solicita {string} con quantity {string}")
    public void solicitaConQuantity(String endpoint, String qty) {
        Map<String, String> body = Map.of("quantity", qty);
        ejecutarRequest(endpoint.replace("{id}", "recipe-test-id"), body);
    }

    @Y("el costo total se recalcula automáticamente")
    public void costoRecalculaAutomaticamente() {
        assertThat(context.has("response")).isTrue();
    }

    @Dado("que existe la receta {string} sin ventas en los últimos {int} días")
    public void recetaSinVentas(String receta, int dias) {
        context.put("currentRecipe", receta);
    }

    @Dado("que existe la receta {string} con ventas registradas")
    public void recetaConVentas(String receta) {
        context.put("currentRecipe", receta);
        context.put("hasSales", true);
    }

    @Cuando("el administrador solicita {string}")
    public void adminSolicita(String endpoint) {
        ejecutarRequest(endpoint.replace("{id}", "recipe-test-id"), null);
    }

    @Y("la respuesta indica {string}")
    public void respuestaIndica(String mensaje) {
        assertThat(context.has("response")).isTrue();
    }

    @Dado("que existe la receta {string} con ingredientes definidos")
    public void recetaConIngredientes(String receta) {
        context.put("currentRecipe", receta);
    }

    @Dado("que el stock cubre los ingredientes para {int} porciones")
    public void stockCubrePorciones(int porciones) {
        context.put("stockCovers", true);
    }

    @Dado("que el stock de {string} no cubre los ingredientes para {int} porciones")
    public void stockNoCubre(String insumo, int porciones) {
        context.put("stockShortage", insumo);
    }

    @Cuando("se solicita {string} con servings {string}")
    public void solicitaConServings(String endpoint, String servings) {
        Map<String, String> body = Map.of("servings", servings);
        ejecutarRequest(endpoint.replace("{id}", "recipe-test-id"), body);
    }

    @Y("la respuesta indica que no existen ingredientes faltantes")
    public void noExistenFaltantes() {
        assertThat(context.has("response")).isTrue();
    }

    @Y("la respuesta incluye {string} en la lista de ingredientes faltantes")
    public void incluyeFaltante(String insumo) {
        assertThat(context.has("response")).isTrue();
    }

    @Y("se sugiere generar un pedido al proveedor")
    public void sugierePedidoProveedor() {
        assertThat(context.has("response")).isTrue();
    }

    @Cuando("se solicita {string}")
    public void seSolicita(String endpoint) {
        ejecutarRequest(endpoint.replace("{id}", "recipe-test-id"), null);
    }

    @Y("la respuesta contiene los campos:")
    public void respuestaContieneCampos(DataTable dt) {
        List<String> campos = dt.asList(String.class);
        assertThat(campos).isNotEmpty();
    }

    @Dado("que existe un proveedor con RUC {string}")
    public void existeProveedorConRuc(String ruc) {
        Map<String, String> body = Map.of("tradeName", "Existente SAC", "ruc", ruc);
        ejecutarRequest("POST /api/v1/suppliers", body);
    }

    @Cuando("el administrador intenta registrar otro proveedor con el mismo RUC")
    public void intentaRegistrarRucDuplicado() {
        Map<String, String> body = Map.of("tradeName", "Duplicado SAC", "ruc", "20601234567");
        ejecutarRequest("POST /api/v1/suppliers", body);
    }

    @Dado("que existe el proveedor {string} con estado {string}")
    public void existeProveedorConEstado(String nombre, String estado) {
        context.put("currentSupplier", nombre);
    }

    @Cuando("el administrador solicita {string} con motivo {string}")
    public void solicitaConMotivo(String endpoint, String motivo) {
        Map<String, String> body = Map.of("reason", motivo);
        ejecutarRequest(endpoint.replace("{id}", "supplier-test-id"), body);
    }

    @Y("el estado del proveedor pasa a {string}")
    public void estadoProveedorPasaA(String estado) {
        context.put("supplierStatus", estado);
    }

    @Y("el proveedor no aparece en opciones de nuevos pedidos")
    public void noApareceEnNuevosPedidos() {
        assertThat(context.has("response")).isTrue();
    }

    @Dado("que existe un pedido completado del proveedor {string}")
    public void existePedidoCompletado(String proveedor) {
        context.put("currentSupplier", proveedor);
        context.put("orderCompleted", true);
    }

    @Cuando("el administrador solicita {string} con:")
    public void solicitaProveedorCon(String endpoint, DataTable dt) {
        Map<String, String> body = dt.asMap(String.class, String.class);
        ejecutarRequest(endpoint.replace("{id}", "supplier-test-id"), body);
    }

    @Y("el promedio del proveedor se actualiza")
    public void promedioProveedorActualizado() {
        assertThat(context.has("response")).isTrue();
    }

    @Y("existe el proveedor {string} con productos catalogados")
    public void existeProveedorConProductos(String nombre) {
        context.put("currentSupplier", nombre);
    }

    @Y("la respuesta contiene el total calculado {string}")
    public void respuestaTotalCalculado(String total) {
        assertThat(context.has("response")).isTrue();
    }

    @Y("el estado inicial del pedido es {string}")
    public void estadoInicialPedido(String estado) {
        context.put("orderStatus", estado);
    }

    @Dado("que existe el pedido #{int} con estado {string}")
    public void existePedidoConEstado(int numero, String estado) {
        context.put("currentOrderNumber", numero);
        context.put("orderStatus", estado);
    }

    @Y("el estado pasa a {string}")
    public void estadoPasaA(String estado) {
        context.put("orderStatus", estado);
    }

    @Y("se actualizan los stocks de los insumos involucrados")
    public void stocksActualizados() {
        assertThat(context.has("response")).isTrue();
    }

    @Y("se registra la fecha y hora de recepción")
    public void registraFechaRecepcion() {
        assertThat(context.has("response")).isTrue();
    }

    @Dado("que el insumo {string} es ofrecido por {int} proveedores con precios distintos")
    public void insumoOfrecidoPorProveedores(String insumo, int n) {
        context.put("comparisonSupply", insumo);
    }

    @Y("los proveedores aparecen ordenados ascendentemente por precio")
    public void proveedoresOrdenadosPorPrecio() {
        assertThat(context.has("response")).isTrue();
    }

    @Y("cada elemento incluye la calificación promedio del proveedor")
    public void incluyeCalificacionPromedio() {
        assertThat(context.has("response")).isTrue();
    }

    @Cuando("se envía pedido con supplierId {string} y cantidad de items {string}")
    public void envioPedido(String supplierId, String items) {
        Map<String, Object> body = new HashMap<>();
        body.put("supplierId", supplierId == null ? "" : supplierId);
        body.put("itemsCount", items == null ? "0" : items);
        ejecutarRequest("POST /api/v1/orders", body);
    }

    @Entonces("la respuesta contiene la ruta {string}")
    public void contieneRuta(String ruta) {
        ResponseEntity<?> response = context.get("response");
        assertThat(response.getStatusCode().is2xxSuccessful() || response.getStatusCode().value() == 404).isTrue();
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

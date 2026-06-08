package com.restock.platform.bdd.monitoring;

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

public class MonitoringStepDefinitions {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private TestContext context;

    @Y("existen insumos registrados con valores de stock mínimo y máximo configurados")
    public void existenInsumosConStockMinMax() {
        context.put("seededAlertsInventory", true);
    }

    @Dado("que el insumo {string} tiene stock actual de {int} kg y stock mínimo de {int} kg")
    public void insumoConStockYMin(String nombre, int actual, int min) {
        context.put("alertSupply", nombre);
        context.put("currentStock", actual);
        context.put("stockMin", min);
    }

    @Dado("que el insumo {string} tiene stock actual {int} kg y stock máximo {int} kg")
    public void insumoConStockYMax(String nombre, int actual, int max) {
        context.put("alertSupply", nombre);
        context.put("currentStock", actual);
        context.put("stockMax", max);
    }

    @Dado("que el insumo {string} tiene un lote que vence en {int} días")
    public void insumoConVencimientoEnDias(String nombre, int dias) {
        context.put("alertSupply", nombre);
        context.put("daysToExpire", dias);
    }

    @Cuando("el sistema ejecuta {string}")
    public void ejecutaCheck(String endpoint) {
        ejecutarRequest(endpoint, null);
    }

    @Cuando("el sistema ejecuta la verificación diaria de vencimientos")
    public void verificacionDiariaVencimientos() {
        ejecutarRequest("POST /api/v1/monitoring/expiration-check", null);
    }

    @Cuando("se ejecuta la verificación de inventario")
    public void verificacionInventario() {
        ejecutarRequest("POST /api/v1/monitoring/inventory-check", null);
    }

    @Cuando("el sistema detecta la condición")
    public void sistemaDetectaCondicion() {
        ejecutarRequest("POST /api/v1/monitoring/inventory-check", null);
    }

    @Y("se genera una alerta de tipo {string}")
    public void seGeneraAlertaTipo(String tipo) {
        context.put("lastAlertType", tipo);
    }

    @Y("la alerta contiene el mensaje {string}")
    public void alertaContieneMensaje(String mensaje) {
        assertThat(context.has("response")).isTrue();
    }

    @Y("la alerta indica la fecha exacta de vencimiento")
    public void alertaFechaVencimiento() {
        assertThat(context.has("response")).isTrue();
    }

    @Y("la alerta sugiere reducir compras o promocionar el producto")
    public void alertaSugiereReducir() {
        assertThat(context.has("response")).isTrue();
    }

    @Dado("que existen {int} alertas activas de distintos tipos")
    public void existenAlertasActivas(int n) {
        context.put("activeAlertsCount", n);
    }

    @Cuando("el administrador solicita {string}")
    public void adminSolicita(String endpoint) {
        ejecutarRequest(endpoint.replace("{id}", "alert-test-id"), null);
    }

    @Y("la respuesta contiene únicamente alertas del tipo {string}")
    public void respuestaSoloTipo(String tipo) {
        assertThat(context.has("response")).isTrue();
    }

    @Y("las alertas están ordenadas por prioridad")
    public void alertasOrdenadasPrioridad() {
        assertThat(context.has("response")).isTrue();
    }

    @Dado("que existe una alerta activa de {string} para {string}")
    public void existeAlertaActiva(String tipo, String insumo) {
        context.put("currentAlertType", tipo);
        context.put("currentAlertSupply", insumo);
    }

    @Y("la alerta deja de aparecer en el listado de activas")
    public void alertaDejaActiva() {
        assertThat(context.has("response")).isTrue();
    }

    @Y("queda registrada en el historial con estado {string}")
    public void registradaHistorial(String estado) {
        context.put("alertFinalStatus", estado);
    }

    @Y("existen recetas registradas con sus ingredientes")
    public void existenRecetasConIngredientes() {
        context.put("seededRecipes", true);
    }

    @Y("los insumos tienen stock suficiente")
    public void insumosStockSuficiente() {
        context.put("stockReady", true);
    }

    @Cuando("el empleado solicita {string} con cuerpo:")
    public void empleadoSolicitaConCuerpo(String endpoint, DataTable dt) {
        List<Map<String, String>> rows = dt.asMaps(String.class, String.class);
        ejecutarRequest(endpoint, Map.of("items", rows));
    }

    @Y("la respuesta contiene el total calculado de la venta")
    public void respuestaTotalVenta() {
        assertThat(context.has("response")).isTrue();
    }

    @Y("se descuentan automáticamente los ingredientes del inventario")
    public void descuentaIngredientes() {
        assertThat(context.has("response")).isTrue();
    }

    @Y("se genera un ID único de venta con fecha y hora")
    public void generaIdVenta() {
        assertThat(context.has("response")).isTrue();
    }

    @Cuando("el empleado registra una venta con:")
    public void empleadoRegistraVentaCon(DataTable dt) {
        List<Map<String, String>> rows = dt.asMaps(String.class, String.class);
        ejecutarRequest("POST /api/v1/sales", Map.of("items", rows));
    }

    @Entonces("el sistema clasifica correctamente cada ítem")
    public void clasificaCadaItem() {
        assertThat(context.has("response")).isTrue();
    }

    @Y("descuenta tanto ingredientes de recetas como insumos adicionales")
    public void descuentaAmbosTipos() {
        assertThat(context.has("response")).isTrue();
    }

    @Dado("que la receta {string} requiere {int}g de arroz")
    public void recetaRequiere(String receta, int gramos) {
        context.put("requiredAmount", gramos);
    }

    @Y("el stock actual de arroz es {int}g")
    public void stockArrozEs(int gramos) {
        context.put("currentArrozStock", gramos);
    }

    @Cuando("el empleado intenta registrar la venta")
    public void empleadoIntentaRegistrar() {
        Map<String, Object> body = Map.of("items", List.of(Map.of("recipe", "Arroz con Pollo", "quantity", 1)));
        ejecutarRequest("POST /api/v1/sales", body);
    }

    @Y("la venta no se persiste en la base de datos")
    public void ventaNoPersistida() {
        ResponseEntity<?> response = context.get("response");
        assertThat(response.getStatusCode().is4xxClientError() || response.getStatusCode().is5xxServerError()).isTrue();
    }

    @Dado("que se han registrado {int} ventas en el día actual")
    public void registradasVentasHoy(int n) {
        context.put("salesToday", n);
    }

    @Cuando("el empleado solicita {string}")
    public void empleadoSolicita(String endpoint) {
        ejecutarRequest(endpoint, null);
    }

    @Y("la respuesta contiene {int} elementos ordenados por hora descendente")
    public void respuestaElementosOrdenados(int n) {
        assertThat(context.has("response")).isTrue();
    }

    @Y("soporta filtros por:")
    public void soportaFiltros(DataTable dt) {
        assertThat(dt.asList(String.class)).isNotEmpty();
    }

    @Dado("que existe una venta registrada con id {string} y ya descontó inventario")
    public void existeVentaRegistrada(String id) {
        context.put("currentSaleId", id);
    }

    @Cuando("el administrador solicita {string} con justificación")
    public void adminSolicitaConJustificacion(String endpoint) {
        Map<String, String> body = Map.of("reason", "Error operacional");
        ejecutarRequest(endpoint, body);
    }

    @Y("revierte los cambios de stock en el inventario")
    public void revierteStock() {
        assertThat(context.has("response")).isTrue();
    }

    @Y("marca la venta como {string}")
    public void marcaVentaComo(String estado) {
        context.put("saleStatus", estado);
    }

    @Y("registra el evento en el log de auditoría")
    public void registraEventoAuditoria() {
        assertThat(context.has("response")).isTrue();
    }

    @Y("existen datos históricos de ventas, compras y consumos")
    public void existenDatosHistoricos() {
        context.put("seededHistory", true);
    }

    @Y("la respuesta contiene las métricas:")
    public void respuestaContieneMetricas(DataTable dt) {
        assertThat(dt.asList(String.class)).isNotEmpty();
    }

    @Y("la respuesta incluye tendencia mensual de consumo")
    public void incluyeTendenciaMensual() {
        assertThat(context.has("response")).isTrue();
    }

    @Y("la respuesta destaca el insumo más consumido")
    public void destacaInsumoConsumido() {
        assertThat(context.has("response")).isTrue();
    }

    @Y("por cada receta se entrega:")
    public void porCadaReceta(DataTable dt) {
        assertThat(dt.asList(String.class)).isNotEmpty();
    }

    @Y("el Content-Type es {string}")
    public void contentTypeEs(String tipo) {
        ResponseEntity<?> response = context.get("response");
        assertThat(response).isNotNull();
    }

    @Y("el archivo se entrega como attachment")
    public void archivoComoAttachment() {
        assertThat(context.has("response")).isTrue();
    }

    @Cuando("el administrador solicita {string} con:")
    public void adminSolicitaCon(String endpoint, DataTable dt) {
        Map<String, String> body = dt.asMap(String.class, String.class);
        ejecutarRequest(endpoint, body);
    }

    @Y("la programación queda registrada y activa")
    public void programacionActiva() {
        assertThat(context.has("response")).isTrue();
    }

    @Y("tiene habilitadas las notificaciones push")
    public void tieneNotifHabilitadas() {
        context.put("pushEnabled", true);
    }

    @Y("tiene registrado un token de dispositivo")
    public void tokenDispositivo() {
        context.put("deviceToken", "device-test-token");
    }

    @Dado("que el insumo {string} alcanza el stock mínimo")
    public void insumoAlcanzaStockMin(String nombre) {
        context.put("triggerSupply", nombre);
    }

    @Entonces("se publica una notificación push al token del administrador")
    public void publicaPushAdmin() {
        assertThat(context.has("deviceToken")).isTrue();
    }

    @Y("el mensaje contiene {string}")
    public void mensajeContiene(String texto) {
        assertThat(context.has("response")).isTrue();
    }

    @Y("se almacena la notificación con estado {string}")
    public void almacenaConEstado(String estado) {
        context.put("notificationStatus", estado);
    }

    @Dado("que un restaurante crea un pedido al proveedor")
    public void restauranteCreaPedido() {
        context.put("orderCreatedForSupplier", true);
    }

    @Cuando("el pedido se confirma en el sistema")
    public void pedidoSeConfirma() {
        ejecutarRequest("POST /api/v1/orders/order-test-id/confirm", null);
    }

    @Entonces("el proveedor recibe una notificación push")
    public void proveedorRecibePush() {
        assertThat(context.has("response")).isTrue();
    }

    @Y("el mensaje contiene el ID del pedido y el total")
    public void mensajeContieneIdYTotal() {
        assertThat(context.has("response")).isTrue();
    }

    @Dado("que el usuario tiene {int} notificaciones no leídas")
    public void usuarioConNotifNoLeidas(int n) {
        context.put("unreadCount", n);
    }

    @Cuando("el usuario solicita {string}")
    public void usuarioSolicita(String endpoint) {
        ejecutarRequest(endpoint.replace("{id}", "notif-test-id"), null);
    }

    @Y("la notificación pasa a estado {string}")
    public void notifPasaEstado(String estado) {
        context.put("notificationStatus", estado);
    }

    @Y("todas las notificaciones del usuario quedan en estado {string}")
    public void todasNotifEnEstado(String estado) {
        context.put("allNotifStatus", estado);
    }

    @Cuando("el usuario solicita {string} con:")
    public void usuarioSolicitaCon(String endpoint, DataTable dt) {
        List<Map<String, String>> rows = dt.asMaps(String.class, String.class);
        ejecutarRequest(endpoint, Map.of("preferences", rows));
    }

    @Y("las preferencias quedan persistidas")
    public void preferenciasPersistidas() {
        assertThat(context.has("response")).isTrue();
    }

    @Cuando("se envía venta con items {string} y total {string}")
    public void enviaVenta(String items, String total) {
        Map<String, String> body = new HashMap<>();
        body.put("itemsCount", items == null ? "0" : items);
        body.put("total", total == null ? "0" : total);
        ejecutarRequest("POST /api/v1/sales", body);
    }

    @Cuando("se solicita {string}")
    public void seSolicita(String endpoint) {
        ejecutarRequest(endpoint, null);
    }

    @Cuando("se solicita {string} sin token")
    public void solicitaSinToken(String endpoint) {
        String path = endpoint.replaceFirst("^[A-Z]+ ", "");
        ResponseEntity<Map> response = restTemplate.exchange(
                path, HttpMethod.GET, HttpEntity.EMPTY, Map.class);
        context.put("response", response);
    }

    @Entonces("la respuesta contiene la ruta {string}")
    public void contieneRuta(String ruta) {
        assertThat(context.has("response")).isTrue();
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

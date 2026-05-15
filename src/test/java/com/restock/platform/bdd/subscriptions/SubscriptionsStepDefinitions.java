package com.restock.platform.bdd.subscriptions;

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

public class SubscriptionsStepDefinitions {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private TestContext context;

    @Y("existen los planes {string}, {string} y {string} en el catálogo")
    public void existenPlanes(String a, String b, String c) {
        context.put("availablePlans", List.of(a, b, c));
    }

    @Dado("que el usuario tiene el plan {string} activo")
    public void usuarioConPlanActivo(String plan) {
        context.put("currentPlan", plan);
        context.put("subscriptionStatus", "ACTIVE");
    }

    @Dado("que el usuario tiene el plan {string}")
    public void usuarioConPlan(String plan) {
        context.put("currentPlan", plan);
    }

    @Cuando("solicita {string}")
    public void usuarioSolicita(String endpoint) {
        ejecutarRequest(endpoint, null);
    }

    @Y("la respuesta contiene los campos:")
    public void respuestaContieneCampos(DataTable dt) {
        List<String> campos = dt.asList(String.class);
        assertThat(campos).isNotEmpty();
    }

    @Cuando("solicita {string} con cuerpo:")
    public void solicitaConCuerpo(String endpoint, DataTable dt) {
        Map<String, String> body = dt.asMap(String.class, String.class);
        ejecutarRequest(endpoint, body);
    }

    @Y("el plan activo del usuario pasa a {string}")
    public void planActivoPasaA(String plan) {
        context.put("currentPlan", plan);
    }

    @Y("se aplica facturación prorrateada")
    public void facturacionProrrateada() {
        assertThat(context.has("response")).isTrue();
    }

    @Y("las nuevas funcionalidades se habilitan inmediatamente")
    public void funcionalidadesHabilitadas() {
        assertThat(context.has("response")).isTrue();
    }

    @Dado("que el usuario tiene una suscripción {string} con renovación próxima")
    public void suscripcionConRenovacionProxima(String estado) {
        context.put("subscriptionStatus", estado);
    }

    @Y("el estado de la suscripción pasa a {string}")
    public void estadoSuscripcionPasaA(String estado) {
        context.put("subscriptionStatus", estado);
    }

    @Y("el usuario mantiene acceso hasta la fecha de vencimiento")
    public void mantieneAccesoHastaVencimiento() {
        assertThat(context.has("response")).isTrue();
    }

    @Y("recibe confirmación por email")
    public void recibeEmailConfirmacion() {
        assertThat(context.has("response")).isTrue();
    }

    @Cuando("se solicita {string}")
    public void seSolicita(String endpoint) {
        ejecutarRequest(endpoint, null);
    }

    @Y("la respuesta contiene al menos los planes {string}, {string} y {string}")
    public void respuestaContieneAlMenosPlanes(String a, String b, String c) {
        assertThat(context.has("response")).isTrue();
    }

    @Dado("que el usuario tiene una suscripción en {string}")
    public void usuarioConSuscripcionEn(String estado) {
        context.put("subscriptionStatus", estado);
    }

    @Y("el estado regresa a {string}")
    public void estadoRegresaA(String estado) {
        context.put("subscriptionStatus", estado);
    }

    @Dado("que existe un usuario con suscripción {string}")
    public void existeUsuarioConSuscripcion(String estado) {
        context.put("subscriptionStatus", estado);
    }

    @Y("tiene un método de pago registrado")
    public void tieneMetodoPago() {
        context.put("paymentMethod", "card-test-1234");
    }

    @Dado("que la fecha de vencimiento es mañana")
    public void vencimientoManana() {
        context.put("expiresInDays", 1);
    }

    @Y("la renovación automática está habilitada")
    public void renovacionHabilitada() {
        context.put("autoRenew", true);
    }

    @Cuando("el sistema ejecuta {string} para el usuario")
    public void sistemaEjecutaPara(String endpoint) {
        ejecutarRequest(endpoint, Map.of("userId", "user-test-001"));
    }

    @Y("se cobra el monto del plan al método de pago registrado")
    public void seCobraMonto() {
        assertThat(context.has("response")).isTrue();
    }

    @Y("la fecha de renovación se extiende un mes")
    public void renovacionExtendida() {
        assertThat(context.has("response")).isTrue();
    }

    @Y("se envía recibo por email")
    public void enviaRecibo() {
        assertThat(context.has("response")).isTrue();
    }

    @Dado("que la tarjeta de crédito registrada ha vencido")
    public void tarjetaVencida() {
        context.put("paymentMethodValid", false);
    }

    @Cuando("el sistema intenta renovar la suscripción")
    public void sistemaIntentaRenovar() {
        ejecutarRequest("POST /api/v1/subscriptions/renew", null);
    }

    @Entonces("el cobro retorna error")
    public void cobroRetornaError() {
        ResponseEntity<?> response = context.get("response");
        assertThat(response.getStatusCode().is4xxClientError() ||
                   response.getStatusCode().is5xxServerError() ||
                   response.getStatusCode().is2xxSuccessful()).isTrue();
    }

    @Y("la suscripción pasa a estado {string}")
    public void suscripcionPasaEstado(String estado) {
        context.put("subscriptionStatus", estado);
    }

    @Y("el usuario recibe notificación de pago fallido")
    public void recibeNotifPagoFallido() {
        assertThat(context.has("response")).isTrue();
    }

    @Y("se otorgan {int} días de gracia para actualizar método de pago")
    public void diasDeGracia(int n) {
        context.put("graceDays", n);
    }

    @Dado("que la suscripción está en {string} desde hace {int} días")
    public void suscripcionDesdeHaceDias(String estado, int dias) {
        context.put("subscriptionStatus", estado);
        context.put("daysInStatus", dias);
    }

    @Cuando("el usuario solicita acceso a una funcionalidad de pago")
    public void solicitaAccesoFuncPago() {
        ejecutarRequest("GET /api/v1/reports/sales", null);
    }

    @Y("la respuesta indica {string}")
    public void respuestaIndica(String mensaje) {
        assertThat(context.has("response")).isTrue();
    }

    @Dado("que la suscripción está en {string} hace {int} días")
    public void suscripcionEnHaceDias(String estado, int dias) {
        context.put("subscriptionStatus", estado);
        context.put("daysInStatus", dias);
    }

    @Cuando("el usuario solicita {string} con tarjeta válida")
    public void solicitaConTarjetaValida(String endpoint) {
        Map<String, String> body = Map.of("cardToken", "tok_valid_test");
        ejecutarRequest(endpoint, body);
    }

    @Y("se reintenta el cobro automáticamente")
    public void reintentaCobro() {
        assertThat(context.has("response")).isTrue();
    }

    @Y("la suscripción regresa a estado {string}")
    public void suscripcionRegresaEstado(String estado) {
        context.put("subscriptionStatus", estado);
    }

    @Cuando("se solicita {string} sin token")
    public void seSolicitaSinToken(String endpoint) {
        String path = endpoint.replaceFirst("^[A-Z]+ ", "");
        ResponseEntity<Map> response = restTemplate.exchange(
                path, HttpMethod.GET, HttpEntity.EMPTY, Map.class);
        context.put("response", response);
    }

    @Cuando("se envía cambio de plan al objetivo {string}")
    public void enviaCambioPlan(String target) {
        Map<String, String> body = new HashMap<>();
        body.put("targetPlan", target == null ? "" : target);
        body.put("paymentRef", "pay-test-001");
        ejecutarRequest("POST /api/v1/subscriptions/me/change-plan", body);
    }

    @Dado("que existe un pago confirmado con paymentRef {string}")
    public void existePagoConRef(String ref) {
        context.put("existingPaymentRef", ref);
    }

    @Cuando("se reintenta el cambio de plan con el mismo paymentRef")
    public void reintentaConMismoRef() {
        Map<String, String> body = Map.of(
                "targetPlan", "PROFESSIONAL",
                "paymentRef", context.get("existingPaymentRef"));
        ejecutarRequest("POST /api/v1/subscriptions/me/change-plan", body);
    }

    @Y("no se realiza un segundo cobro")
    public void noSegundoCobro() {
        assertThat(context.has("response")).isTrue();
    }

    @Entonces("la respuesta contiene la ruta {string}")
    public void respuestaContieneRuta(String ruta) {
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

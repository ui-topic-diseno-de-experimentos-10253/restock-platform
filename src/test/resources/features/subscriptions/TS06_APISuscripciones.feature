# language: es
@subscriptions @api @technical
Característica: TS06 - Contrato de la API de Suscripciones
  Como integrador del sistema
  Quiero un contrato estable para subscripciones y pagos
  Para construir flujos de checkout seguros

  Escenario: Endpoint de suscripción del usuario requiere autenticación
    Cuando se solicita "GET /api/v1/subscriptions/me" sin token
    Entonces el sistema responde con código HTTP 401

  Escenario: Catálogo público de planes no requiere autenticación
    Cuando se solicita "GET /api/v1/subscriptions/plans"
    Entonces el sistema responde con código HTTP 200

  Esquema del escenario: Validación de cambio de plan
    Cuando se envía cambio de plan al objetivo "<targetPlan>"
    Entonces el sistema responde con código HTTP <status>

    Ejemplos:
      | targetPlan    | status |
      | PROFESSIONAL  | 200    |
      | ENTERPRISE    | 200    |
      | INEXISTENTE   | 400    |
      |               | 400    |

  Escenario: Idempotencia de pagos por paymentRef
    Dado que existe un pago confirmado con paymentRef "pay-001"
    Cuando se reintenta el cambio de plan con el mismo paymentRef
    Entonces el sistema responde con código HTTP 200
    Y no se realiza un segundo cobro

  Escenario: Documentación OpenAPI expone subscriptions
    Cuando se solicita "GET /v3/api-docs"
    Entonces la respuesta contiene la ruta "/api/v1/subscriptions/me"
    Y la respuesta contiene la ruta "/api/v1/subscriptions/plans"

# language: es
@subscriptions
Característica: HU17 - Gestión de planes de suscripción
  Como suscriptor de la plataforma Restock
  Quiero administrar mi plan de suscripción
  Para acceder a las funcionalidades acordes a mis necesidades

  Antecedentes:
    Dado que el usuario ha iniciado sesión
    Y existen los planes "BASIC", "PROFESSIONAL" y "ENTERPRISE" en el catálogo

  Escenario: Visualizar plan actual y características
    Dado que el usuario tiene el plan "PROFESSIONAL" activo
    Cuando solicita "GET /api/v1/subscriptions/me"
    Entonces el sistema responde con código HTTP 200
    Y la respuesta contiene los campos:
      | campo            |
      | planName         |
      | monthlyPrice     |
      | startDate        |
      | nextRenewalDate  |
      | features         |
      | usageLimits      |

  Escenario: Actualizar plan de BASIC a PROFESSIONAL
    Dado que el usuario tiene el plan "BASIC"
    Cuando solicita "POST /api/v1/subscriptions/me/change-plan" con cuerpo:
      | campo       | valor         |
      | targetPlan  | PROFESSIONAL  |
      | paymentRef  | pay-test-001  |
    Entonces el sistema responde con código HTTP 200
    Y el plan activo del usuario pasa a "PROFESSIONAL"
    Y se aplica facturación prorrateada
    Y las nuevas funcionalidades se habilitan inmediatamente

  Escenario: Cancelar suscripción al fin de ciclo
    Dado que el usuario tiene una suscripción "ACTIVE" con renovación próxima
    Cuando solicita "POST /api/v1/subscriptions/me/cancel"
    Entonces el sistema responde con código HTTP 200
    Y el estado de la suscripción pasa a "PENDING_CANCELLATION"
    Y el usuario mantiene acceso hasta la fecha de vencimiento
    Y recibe confirmación por email

  Escenario: Listar catálogo público de planes
    Cuando se solicita "GET /api/v1/subscriptions/plans"
    Entonces el sistema responde con código HTTP 200
    Y la respuesta contiene al menos los planes "BASIC", "PROFESSIONAL" y "ENTERPRISE"

  Escenario: Reactivar suscripción cancelada antes de su fin
    Dado que el usuario tiene una suscripción en "PENDING_CANCELLATION"
    Cuando solicita "POST /api/v1/subscriptions/me/reactivate"
    Entonces el sistema responde con código HTTP 200
    Y el estado regresa a "ACTIVE"

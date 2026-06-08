# language: es
@subscriptions @billing
Característica: HU18 - Renovación automática y gestión de pagos
  Como suscriptor con renovación automática habilitada
  Quiero que mi suscripción se renueve sin intervención
  Para asegurar continuidad del servicio

  Antecedentes:
    Dado que existe un usuario con suscripción "ACTIVE"
    Y tiene un método de pago registrado

  Escenario: Renovación exitosa al vencimiento
    Dado que la fecha de vencimiento es mañana
    Y la renovación automática está habilitada
    Cuando el sistema ejecuta "POST /api/v1/subscriptions/renew" para el usuario
    Entonces el sistema responde con código HTTP 200
    Y se cobra el monto del plan al método de pago registrado
    Y la fecha de renovación se extiende un mes
    Y se envía recibo por email

  Escenario: Fallo en cobro y periodo de gracia
    Dado que la tarjeta de crédito registrada ha vencido
    Cuando el sistema intenta renovar la suscripción
    Entonces el cobro retorna error
    Y la suscripción pasa a estado "PAYMENT_FAILED"
    Y el usuario recibe notificación de pago fallido
    Y se otorgan 7 días de gracia para actualizar método de pago

  Escenario: Restricción de acceso al expirar el periodo de gracia
    Dado que la suscripción está en "PAYMENT_FAILED" desde hace 8 días
    Cuando el usuario solicita acceso a una funcionalidad de pago
    Entonces el sistema responde con código HTTP 402
    Y la respuesta indica "Pago requerido para continuar"

  Escenario: Actualizar método de pago durante periodo de gracia
    Dado que la suscripción está en "PAYMENT_FAILED" hace 3 días
    Cuando el usuario solicita "PUT /api/v1/subscriptions/me/payment-method" con tarjeta válida
    Entonces el sistema responde con código HTTP 200
    Y se reintenta el cobro automáticamente
    Y la suscripción regresa a estado "ACTIVE"

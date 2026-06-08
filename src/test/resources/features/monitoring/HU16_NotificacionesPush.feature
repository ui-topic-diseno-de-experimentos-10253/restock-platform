# language: es
@monitoring @notifications
Característica: HU16 - Sistema de notificaciones push en tiempo real
  Como usuario del sistema
  Quiero recibir notificaciones relevantes
  Para estar informado sin consultar constantemente

  Antecedentes:
    Dado que el usuario ha iniciado sesión
    Y tiene habilitadas las notificaciones push
    Y tiene registrado un token de dispositivo

  Escenario: Notificación push por stock crítico
    Dado que el insumo "Sal" alcanza el stock mínimo
    Cuando el sistema detecta la condición
    Entonces se publica una notificación push al token del administrador
    Y el mensaje contiene "Stock crítico: Sal"
    Y se almacena la notificación con estado "UNREAD"

  Escenario: Notificación push por nuevo pedido (para proveedor)
    Dado que un restaurante crea un pedido al proveedor
    Cuando el pedido se confirma en el sistema
    Entonces el proveedor recibe una notificación push
    Y el mensaje contiene el ID del pedido y el total

  Escenario: Marcar notificación como leída
    Dado que el usuario tiene 5 notificaciones no leídas
    Cuando el usuario solicita "PATCH /api/v1/notifications/{id}/read"
    Entonces el sistema responde con código HTTP 200
    Y la notificación pasa a estado "READ"

  Escenario: Marcar todas las notificaciones como leídas
    Cuando el usuario solicita "PATCH /api/v1/notifications/read-all"
    Entonces el sistema responde con código HTTP 200
    Y todas las notificaciones del usuario quedan en estado "READ"

  Escenario: Configurar preferencias de notificaciones
    Cuando el usuario solicita "PUT /api/v1/notifications/preferences" con:
      | tipo                | enabled |
      | LOW_STOCK           | true    |
      | NEAR_EXPIRATION     | true    |
      | NEW_ORDER           | false   |
      | ORDER_STATUS_CHANGE | true    |
      | SCHEDULED_REPORT    | false   |
    Entonces el sistema responde con código HTTP 200
    Y las preferencias quedan persistidas

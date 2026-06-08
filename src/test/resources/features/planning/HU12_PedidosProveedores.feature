# language: es
@planning @orders
Característica: HU12 - Gestión de pedidos a proveedores
  Como administrador
  Quiero crear y dar seguimiento a pedidos
  Para mantener abastecido mi inventario

  Antecedentes:
    Dado que el usuario autenticado tiene rol "ROLE_ADMIN"
    Y existe el proveedor "Distribuidora Don José" con productos catalogados

  Escenario: Crear un pedido a proveedor
    Cuando el administrador solicita "POST /api/v1/orders" con cuerpo:
      | supplierId | items                                       |
      | sup-001    | [{"product":"Arroz","quantity":50,"unitPrice":3.50},{"product":"Azúcar","quantity":25,"unitPrice":2.80}] |
    Entonces el sistema responde con código HTTP 201
    Y la respuesta contiene el total calculado "245.00"
    Y el estado inicial del pedido es "PENDING"

  Escenario: Consultar estado de un pedido en curso
    Dado que existe el pedido #1234 con estado "IN_TRANSIT"
    Cuando el administrador solicita "GET /api/v1/orders/1234"
    Entonces el sistema responde con código HTTP 200
    Y la respuesta contiene los campos:
      | campo            |
      | orderNumber      |
      | supplier         |
      | items            |
      | estimatedDate    |
      | status           |
      | statusHistory    |

  Escenario: Confirmar recepción y actualizar inventario
    Dado que existe el pedido #1235 con estado "IN_TRANSIT"
    Cuando el administrador solicita "PATCH /api/v1/orders/1235/receive"
    Entonces el sistema responde con código HTTP 200
    Y el estado pasa a "RECEIVED"
    Y se actualizan los stocks de los insumos involucrados
    Y se registra la fecha y hora de recepción

  Escenario: Cancelar pedido pendiente con motivo
    Dado que existe el pedido #1236 con estado "PENDING"
    Cuando el administrador solicita "PATCH /api/v1/orders/1236/cancel" con motivo "Cambio de proveedor"
    Entonces el sistema responde con código HTTP 200
    Y el estado pasa a "CANCELLED"

  Escenario: Bloqueo de cancelación de pedido ya recibido
    Dado que existe el pedido #1237 con estado "RECEIVED"
    Cuando el administrador solicita "PATCH /api/v1/orders/1237/cancel"
    Entonces el sistema responde con código HTTP 409
    Y la respuesta indica "No se puede cancelar un pedido ya recibido"

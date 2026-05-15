# language: es
@planning @suppliers
Característica: HU11 - Administración de proveedores
  Como administrador del restaurante
  Quiero mantener mi red de proveedores
  Para gestionar relaciones comerciales eficientes

  Antecedentes:
    Dado que el usuario autenticado tiene rol "ROLE_ADMIN"

  Escenario: Registrar un nuevo proveedor con datos válidos
    Cuando el administrador solicita "POST /api/v1/suppliers" con cuerpo:
      | campo            | valor                    |
      | tradeName        | Mercado Fresh SAC        |
      | ruc              | 20601234567              |
      | contactName      | Juan Pérez               |
      | phone            | +51 987654321            |
      | email            | ventas@mercadofresh.com  |
      | address          | Av. Industrial 456, Lima |
      | category         | Frutas y Verduras        |
    Entonces el sistema responde con código HTTP 201
    Y el proveedor aparece como activo en la lista
    Y se envía un email de bienvenida al proveedor

  Escenario: Rechazo de proveedor con RUC duplicado
    Dado que existe un proveedor con RUC "20601234567"
    Cuando el administrador intenta registrar otro proveedor con el mismo RUC
    Entonces el sistema responde con código HTTP 409
    Y la respuesta indica "Ya existe un proveedor con ese RUC"

  Escenario: Desactivar un proveedor temporalmente
    Dado que existe el proveedor "Carnes Premium" con estado "ACTIVE"
    Cuando el administrador solicita "PATCH /api/v1/suppliers/{id}/deactivate" con motivo "Servicio no disponible"
    Entonces el sistema responde con código HTTP 200
    Y el estado del proveedor pasa a "INACTIVE"
    Y el proveedor no aparece en opciones de nuevos pedidos

  Escenario: Calificar desempeño de proveedor tras pedido completado
    Dado que existe un pedido completado del proveedor "Lácteos del Valle"
    Cuando el administrador solicita "POST /api/v1/suppliers/{id}/ratings" con:
      | criterio       | valor |
      | quality        | 5     |
      | punctuality    | 4     |
      | customerService| 5     |
    Entonces el sistema responde con código HTTP 201
    Y el promedio del proveedor se actualiza

# language: es
@monitoring @sales
Característica: HU14 - Registro de ventas del restaurante
  Como empleado del restaurante
  Quiero registrar las ventas realizadas
  Para actualizar el inventario y generar reportes precisos

  Antecedentes:
    Dado que el usuario autenticado tiene rol "ROLE_EMPLEADO_VENTAS"
    Y existen recetas registradas con sus ingredientes
    Y los insumos tienen stock suficiente

  Escenario: Registrar venta con recetas del menú
    Cuando el empleado solicita "POST /api/v1/sales" con cuerpo:
      | recipe        | quantity |
      | Lomo Saltado  | 2        |
      | Ceviche       | 1        |
    Entonces el sistema responde con código HTTP 201
    Y la respuesta contiene el total calculado de la venta
    Y se descuentan automáticamente los ingredientes del inventario
    Y se genera un ID único de venta con fecha y hora

  Escenario: Registrar venta con insumos adicionales
    Cuando el empleado registra una venta con:
      | type       | name           | quantity |
      | recipe     | Lomo Saltado   | 1        |
      | supply     | Salsa picante  | 2        |
      | supply     | Pan extra      | 4        |
    Entonces el sistema clasifica correctamente cada ítem
    Y descuenta tanto ingredientes de recetas como insumos adicionales

  Escenario: Bloqueo de venta por stock insuficiente
    Dado que la receta "Arroz con Pollo" requiere 300g de arroz
    Y el stock actual de arroz es 100g
    Cuando el empleado intenta registrar la venta
    Entonces el sistema responde con código HTTP 409
    Y la respuesta indica "Stock insuficiente para Arroz"
    Y la venta no se persiste en la base de datos

  Escenario: Visualizar historial de ventas del día
    Dado que se han registrado 15 ventas en el día actual
    Cuando el empleado solicita "GET /api/v1/sales?date=today"
    Entonces el sistema responde con código HTTP 200
    Y la respuesta contiene 15 elementos ordenados por hora descendente
    Y soporta filtros por:
      | filtro      |
      | hour        |
      | recipe      |
      | totalAmount |
      | employeeId  |

  Escenario: Anular una venta y revertir inventario
    Dado que existe una venta registrada con id "sale-001" y ya descontó inventario
    Cuando el administrador solicita "PATCH /api/v1/sales/sale-001/void" con justificación
    Entonces el sistema responde con código HTTP 200
    Y revierte los cambios de stock en el inventario
    Y marca la venta como "VOIDED"
    Y registra el evento en el log de auditoría

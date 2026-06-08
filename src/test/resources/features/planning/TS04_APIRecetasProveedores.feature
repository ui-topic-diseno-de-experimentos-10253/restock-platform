# language: es
@planning @api @technical
Característica: TS04 - Contrato de la API de Recetas, Proveedores y Pedidos
  Como integrador del sistema
  Quiero contratos estables para planning
  Para garantizar interoperabilidad entre clientes

  Escenario: Comparar precios entre proveedores para un mismo insumo
    Dado que el insumo "Pollo" es ofrecido por 3 proveedores con precios distintos
    Cuando se solicita "GET /api/v1/supplies/Pollo/suppliers/compare"
    Entonces el sistema responde con código HTTP 200
    Y los proveedores aparecen ordenados ascendentemente por precio
    Y cada elemento incluye la calificación promedio del proveedor

  Esquema del escenario: Validación de creación de pedido
    Cuando se envía pedido con supplierId "<supplier>" y cantidad de items "<items>"
    Entonces el sistema responde con código HTTP <status>

    Ejemplos:
      | supplier | items | status |
      | sup-001  | 1     | 201    |
      |          | 1     | 400    |
      | sup-001  | 0     | 400    |
      | sup-999  | 1     | 404    |

  Escenario: Documentación OpenAPI expone planning
    Cuando se solicita "GET /v3/api-docs"
    Entonces la respuesta contiene la ruta "/api/v1/recipes"
    Y la respuesta contiene la ruta "/api/v1/suppliers"
    Y la respuesta contiene la ruta "/api/v1/orders"

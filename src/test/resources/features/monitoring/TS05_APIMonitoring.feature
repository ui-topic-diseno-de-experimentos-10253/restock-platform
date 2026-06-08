# language: es
@monitoring @api @technical
Característica: TS05 - Contrato de la API de Monitoreo y Reportes
  Como integrador del sistema
  Quiero contratos estables para reportes, alertas y notificaciones
  Para construir dashboards y clientes móviles confiables

  Escenario: Endpoint de alertas requiere autenticación
    Cuando se solicita "GET /api/v1/monitoring/alerts" sin token
    Entonces el sistema responde con código HTTP 401

  Esquema del escenario: Validación de creación de venta
    Cuando se envía venta con items "<items>" y total "<total>"
    Entonces el sistema responde con código HTTP <status>

    Ejemplos:
      | items | total | status |
      | 1     | 35.00 | 201    |
      | 0     | 0.00  | 400    |
      | 1     | -10   | 400    |

  Escenario: Exportar reporte en formato inválido
    Cuando se solicita "GET /api/v1/reports/sales/export?format=xml"
    Entonces el sistema responde con código HTTP 400
    Y la respuesta indica "Formato no soportado"

  Escenario: Documentación OpenAPI expone monitoring
    Cuando se solicita "GET /v3/api-docs"
    Entonces la respuesta contiene la ruta "/api/v1/sales"
    Y la respuesta contiene la ruta "/api/v1/monitoring/alerts"
    Y la respuesta contiene la ruta "/api/v1/notifications"

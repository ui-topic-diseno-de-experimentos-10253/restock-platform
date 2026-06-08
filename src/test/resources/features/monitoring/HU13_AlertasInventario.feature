# language: es
@monitoring @alerts
Característica: HU13 - Alertas automáticas de inventario
  Como administrador del restaurante
  Quiero recibir alertas sobre el estado de mi inventario
  Para tomar decisiones oportunas y evitar problemas operativos

  Antecedentes:
    Dado que el usuario autenticado tiene rol "ROLE_ADMIN"
    Y existen insumos registrados con valores de stock mínimo y máximo configurados

  Escenario: Generación de alerta por stock bajo
    Dado que el insumo "Tomate" tiene stock actual de 3 kg y stock mínimo de 10 kg
    Cuando el sistema ejecuta "POST /api/v1/monitoring/inventory-check"
    Entonces el sistema responde con código HTTP 200
    Y se genera una alerta de tipo "LOW_STOCK"
    Y la alerta contiene el mensaje "El stock de Tomate está por debajo del mínimo"

  Escenario: Generación de alerta por producto próximo a vencer
    Dado que el insumo "Yogurt Natural" tiene un lote que vence en 3 días
    Cuando el sistema ejecuta la verificación diaria de vencimientos
    Entonces se genera una alerta de tipo "NEAR_EXPIRATION"
    Y la alerta indica la fecha exacta de vencimiento

  Escenario: Generación de alerta por exceso de stock
    Dado que el insumo "Arroz" tiene stock actual 150 kg y stock máximo 100 kg
    Cuando se ejecuta la verificación de inventario
    Entonces se genera una alerta de tipo "OVERSTOCK"
    Y la alerta sugiere reducir compras o promocionar el producto

  Escenario: Listado de alertas activas con filtros
    Dado que existen 5 alertas activas de distintos tipos
    Cuando el administrador solicita "GET /api/v1/monitoring/alerts?status=ACTIVE&type=LOW_STOCK"
    Entonces el sistema responde con código HTTP 200
    Y la respuesta contiene únicamente alertas del tipo "LOW_STOCK"
    Y las alertas están ordenadas por prioridad

  Escenario: Marcar alerta como resuelta
    Dado que existe una alerta activa de "LOW_STOCK" para "Cebolla"
    Cuando el administrador solicita "PATCH /api/v1/monitoring/alerts/{id}/resolve"
    Entonces el sistema responde con código HTTP 200
    Y la alerta deja de aparecer en el listado de activas
    Y queda registrada en el historial con estado "RESOLVED"

# language: es
@monitoring @reports
Característica: HU15 - Generación de reportes y analíticas
  Como administrador del restaurante
  Quiero generar reportes consolidados
  Para analizar el desempeño y tomar decisiones informadas

  Antecedentes:
    Dado que el usuario autenticado tiene rol "ROLE_ADMIN"
    Y existen datos históricos de ventas, compras y consumos

  Escenario: Generar reporte de ventas mensual
    Cuando el administrador solicita "GET /api/v1/reports/sales?from=2026-04-01&to=2026-04-30"
    Entonces el sistema responde con código HTTP 200
    Y la respuesta contiene las métricas:
      | metrica            |
      | totalSales         |
      | transactionsCount  |
      | averageTicket      |
      | topSellingRecipe   |
      | bestSalesDay       |

  Escenario: Reporte de consumo filtrado por categoría
    Cuando el administrador solicita "GET /api/v1/reports/consumption?category=Carnes&period=Q1-2026"
    Entonces el sistema responde con código HTTP 200
    Y la respuesta incluye tendencia mensual de consumo
    Y la respuesta destaca el insumo más consumido

  Escenario: Análisis de rentabilidad por receta
    Cuando el administrador solicita "GET /api/v1/reports/profitability"
    Entonces el sistema responde con código HTTP 200
    Y por cada receta se entrega:
      | indicador        |
      | ingredientCost   |
      | salePrice        |
      | grossMargin      |
      | profitPercentage |

  Escenario: Exportar reporte personalizado en Excel
    Cuando el administrador solicita "GET /api/v1/reports/sales/export?format=xlsx"
    Entonces el sistema responde con código HTTP 200
    Y el Content-Type es "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
    Y el archivo se entrega como attachment

  Escenario: Programar envío automático de reporte semanal
    Cuando el administrador solicita "POST /api/v1/reports/schedules" con:
      | campo       | valor                       |
      | reportType  | WEEKLY_SALES                |
      | frequency   | EVERY_MONDAY_AT_08          |
      | recipient   | admin@restaurante.com       |
      | format      | PDF                         |
    Entonces el sistema responde con código HTTP 201
    Y la programación queda registrada y activa

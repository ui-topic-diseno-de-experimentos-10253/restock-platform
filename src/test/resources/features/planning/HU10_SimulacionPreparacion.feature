# language: es
@planning @recipes
Característica: HU10 - Simulación de impacto en inventario al preparar receta
  Como jefe de cocina
  Quiero simular la preparación de una receta antes de ejecutarla
  Para anticipar faltantes y planificar compras

  Antecedentes:
    Dado que el usuario autenticado tiene rol "ROLE_JEFE_COCINA"
    Y existe la receta "Ají de Gallina" con ingredientes definidos

  Escenario: Simulación sin faltantes
    Dado que el stock cubre los ingredientes para 5 porciones
    Cuando se solicita "POST /api/v1/recipes/{id}/simulate" con servings "5"
    Entonces el sistema responde con código HTTP 200
    Y la respuesta indica que no existen ingredientes faltantes

  Escenario: Simulación con faltantes
    Dado que el stock de "Pan" no cubre los ingredientes para 5 porciones
    Cuando se solicita "POST /api/v1/recipes/{id}/simulate" con servings "5"
    Entonces el sistema responde con código HTTP 200
    Y la respuesta incluye "Pan" en la lista de ingredientes faltantes
    Y se sugiere generar un pedido al proveedor

  Escenario: Cálculo de margen de ganancia por receta
    Cuando se solicita "GET /api/v1/recipes/{id}/profitability"
    Entonces el sistema responde con código HTTP 200
    Y la respuesta contiene los campos:
      | campo            |
      | ingredientCost   |
      | salePrice        |
      | grossMargin      |
      | profitPercentage |

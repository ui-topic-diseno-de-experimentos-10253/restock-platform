# language: es
@planning @recipes
Característica: HU09 - Creación de recetas vinculadas al inventario
  Como administrador del restaurante
  Quiero crear recetas con ingredientes específicos
  Para calcular costos y controlar el consumo de insumos

  Antecedentes:
    Dado que el usuario autenticado tiene rol "ROLE_ADMIN"
    Y existen los insumos "Lomo", "Cebolla", "Tomate", "Papas" y "Arroz" en el inventario

  Escenario: Crear una receta con ingredientes y porciones
    Dado que no existe la receta "Lomo Saltado"
    Cuando el administrador solicita "POST /api/v1/recipes" con cuerpo:
      | campo       | valor                  |
      | name        | Lomo Saltado           |
      | description | Plato criollo peruano  |
      | servings    | 4                      |
      | salePrice   | 35.00                  |
    Y agrega los ingredientes:
      | name    | quantity | unit |
      | Lomo    | 500      | g    |
      | Cebolla | 200      | g    |
      | Tomate  | 150      | g    |
      | Papas   | 400      | g    |
      | Arroz   | 300      | g    |
    Entonces el sistema responde con código HTTP 201
    Y la respuesta contiene el costo total calculado de los ingredientes
    Y la receta "Lomo Saltado" aparece en el catálogo

  Escenario: Editar ingredientes de una receta existente
    Dado que existe la receta "Ceviche" con 10 unidades de "Limón"
    Cuando el administrador solicita "PUT /api/v1/recipes/{id}/ingredients/Limón" con quantity "15"
    Entonces el sistema responde con código HTTP 200
    Y el costo total se recalcula automáticamente

  Escenario: Eliminar una receta sin ventas registradas
    Dado que existe la receta "Arroz con Pollo" sin ventas en los últimos 30 días
    Cuando el administrador solicita "DELETE /api/v1/recipes/{id}"
    Entonces el sistema responde con código HTTP 204

  Escenario: Bloqueo de eliminación de receta con ventas recientes
    Dado que existe la receta "Lomo Saltado" con ventas registradas
    Cuando el administrador solicita "DELETE /api/v1/recipes/{id}"
    Entonces el sistema responde con código HTTP 409
    Y la respuesta indica "No se puede eliminar una receta con ventas asociadas"

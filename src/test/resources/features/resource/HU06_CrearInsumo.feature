# language: es
@resource @inventory
Característica: HU06 - Creación de insumos en el inventario
  Como administrador del restaurante
  Quiero registrar nuevos insumos en el sistema
  Para llevar control preciso del inventario

  Antecedentes:
    Dado que el usuario autenticado tiene rol "ROLE_ADMIN"
    Y se encuentra en la sección de Inventario

  Escenario: Crear un insumo nuevo exitosamente
    Dado que no existe el insumo "Aceite de Oliva" en el sistema
    Cuando el administrador solicita "POST /api/v1/custom-supplies" con cuerpo:
      | campo     | valor           |
      | name      | Aceite de Oliva |
      | category  | Aceites         |
      | unit      | Litros          |
      | stockMin  | 5               |
      | stockMax  | 20              |
    Entonces el sistema responde con código HTTP 201
    Y la respuesta contiene el id generado del insumo
    Y el stock actual es 0
    Y el insumo "Aceite de Oliva" aparece en la lista del inventario

  Escenario: Validación de campos obligatorios
    Cuando el administrador solicita "POST /api/v1/custom-supplies" sin el campo "name"
    Entonces el sistema responde con código HTTP 400
    Y la respuesta indica "El nombre del insumo es obligatorio"

  Escenario: Rechazo de stock mínimo mayor al máximo
    Cuando el administrador envía stockMin "20" y stockMax "5"
    Entonces el sistema responde con código HTTP 400
    Y la respuesta indica "stockMin no puede ser mayor que stockMax"

  Escenario: No permitir nombres duplicados de insumos
    Dado que existe el insumo "Sal" en el inventario
    Cuando el administrador intenta crear otro insumo con nombre "Sal"
    Entonces el sistema responde con código HTTP 409
    Y la respuesta indica "Ya existe un insumo con ese nombre"

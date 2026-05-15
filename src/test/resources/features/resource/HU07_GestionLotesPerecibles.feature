# language: es
@resource @inventory @batch
Característica: HU07 - Gestión de lotes de productos perecibles
  Como administrador
  Quiero registrar lotes con fechas de vencimiento
  Para controlar la rotación de productos perecibles

  Antecedentes:
    Dado que el usuario autenticado tiene rol "ROLE_ADMIN"
    Y existe el insumo "Leche" con stock actual de 10 litros

  Escenario: Agregar un nuevo lote a un insumo perecible
    Cuando el administrador solicita "POST /api/v1/custom-supplies/{id}/batches" con:
      | campo          | valor       |
      | quantity       | 25          |
      | expirationDate | 2026-05-30  |
      | unitCost       | 4.50        |
    Entonces el sistema responde con código HTTP 201
    Y el stock actual de "Leche" pasa a 35 litros
    Y se registra el lote con fecha de vencimiento "2026-05-30"

  Escenario: Rechazo de lote con fecha de vencimiento en el pasado
    Cuando el administrador envía lote con expirationDate "2020-01-01"
    Entonces el sistema responde con código HTTP 400
    Y la respuesta indica "La fecha de vencimiento no puede estar en el pasado"

  Escenario: Listar lotes activos ordenados por fecha de vencimiento (FEFO)
    Dado que existen 3 lotes registrados para "Leche" con diferentes fechas
    Cuando el administrador solicita "GET /api/v1/custom-supplies/{id}/batches?status=ACTIVE"
    Entonces el sistema responde con código HTTP 200
    Y los lotes están ordenados ascendentemente por "expirationDate"

  Escenario: Consumo automático por orden FEFO al registrar venta
    Dado que existen 2 lotes de "Leche": uno vence en 2 días con 5 litros y otro en 30 días con 20 litros
    Cuando se consume 8 litros de "Leche"
    Entonces el lote con vencimiento próximo se reduce a 0 litros
    Y el lote restante se reduce de 20 a 17 litros

# language: es
@resource @inventory
Característica: HU08 - Edición y eliminación de insumos
  Como administrador
  Quiero modificar o retirar insumos del inventario
  Para mantener el catálogo actualizado

  Antecedentes:
    Dado que el usuario autenticado tiene rol "ROLE_ADMIN"

  Escenario: Editar stock mínimo de un insumo existente
    Dado que existe el insumo "Sal" con stock mínimo de 2 kg
    Cuando el administrador solicita "PUT /api/v1/custom-supplies/{id}" con stockMin "5"
    Entonces el sistema responde con código HTTP 200
    Y el stock mínimo de "Sal" pasa a 5 kg

  Escenario: Eliminar un insumo sin stock
    Dado que existe el insumo "Azúcar Morena" con stock actual 0
    Cuando el administrador solicita "DELETE /api/v1/custom-supplies/{id}"
    Entonces el sistema responde con código HTTP 204
    Y el insumo "Azúcar Morena" ya no aparece en la lista

  Escenario: Bloqueo de eliminación de insumo con stock disponible
    Dado que existe el insumo "Harina" con stock actual de 50 kg
    Cuando el administrador solicita "DELETE /api/v1/custom-supplies/{id}"
    Entonces el sistema responde con código HTTP 409
    Y la respuesta indica "No se puede eliminar un insumo con stock disponible"

  Escenario: Eliminación forzada con confirmación explícita
    Dado que existe el insumo "Harina" con stock actual de 50 kg
    Cuando el administrador solicita "DELETE /api/v1/custom-supplies/{id}?force=true"
    Entonces el sistema responde con código HTTP 204
    Y se registra el evento en el log de auditoría

# language: es
@profile @business
Característica: HU05 - Actualización de datos del negocio
  Como administrador de un restaurante
  Quiero registrar y mantener los datos comerciales de mi negocio
  Para que aparezcan en facturas y documentos oficiales

  Antecedentes:
    Dado que el usuario autenticado tiene rol "ROLE_ADMIN"
    Y existe el perfil del negocio asociado al usuario

  Escenario: Actualizar datos del restaurante
    Cuando el administrador solicita "PUT /api/v1/profiles/me/business-information" con:
      | campo            | valor                 |
      | businessName     | El Buen Sabor         |
      | ruc              | 20501234567           |
      | address          | Jr. Lima 123, Lima    |
      | businessCategory | Comida Criolla        |
    Entonces el sistema responde con código HTTP 200
    Y la información comercial se actualiza correctamente

  Escenario: Rechazo de RUC con formato inválido
    Cuando el administrador envía RUC "12345"
    Entonces el sistema responde con código HTTP 400
    Y la respuesta indica "RUC debe tener 11 dígitos"

  Escenario: Listar categorías de negocio disponibles
    Cuando el administrador solicita "GET /api/v1/business-categories"
    Entonces el sistema responde con código HTTP 200
    Y la respuesta contiene una lista con al menos 1 categoría

  Escenario: Desactivar cuenta del negocio
    Cuando el administrador solicita "DELETE /api/v1/profiles/me" con confirmación de contraseña
    Entonces el sistema responde con código HTTP 200
    Y la cuenta queda marcada como "INACTIVE"
    Y los datos se conservan por 30 días para posible reactivación

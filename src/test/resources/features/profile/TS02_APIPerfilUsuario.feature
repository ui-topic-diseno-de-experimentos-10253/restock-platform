# language: es
@profile @api @technical
Característica: TS02 - Contrato de la API de Perfil de Usuario
  Como integrador frontend/móvil
  Quiero que los endpoints de perfil cumplan el contrato definido
  Para garantizar consistencia entre clientes

  Escenario: Obtener perfil del usuario autenticado
    Dado que el usuario tiene sesión activa
    Cuando solicita "GET /api/v1/profiles/me"
    Entonces el sistema responde con código HTTP 200
    Y la respuesta JSON contiene los campos:
      | campo            |
      | id               |
      | userId           |
      | firstName        |
      | lastName         |
      | email            |
      | businessName     |
      | businessCategory |

  Escenario: Endpoint protegido sin token
    Cuando se solicita "GET /api/v1/profiles/me" sin token
    Entonces el sistema responde con código HTTP 401

  Esquema del escenario: Validación de datos personales
    Cuando se envía actualización de perfil con firstName "<nombre>" y phone "<telefono>"
    Entonces el sistema responde con código HTTP <status>

    Ejemplos:
      | nombre   | telefono       | status |
      | Carlos   | +51 999888777  | 200    |
      |          | +51 999888777  | 400    |
      | Carlos   | abc            | 400    |

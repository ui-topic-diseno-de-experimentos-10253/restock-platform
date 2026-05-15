# language: es
@iam @api @technical
Característica: TS01 - Contrato de la API de Autenticación
  Como integrador del sistema Restock
  Quiero que los endpoints de autenticación cumplan el contrato definido
  Para garantizar la interoperabilidad con clientes web y móvil

  Esquema del escenario: Validación de payload de sign-up
    Cuando se envía "POST /api/v1/authentication/sign-up" con email "<email>" y contraseña "<password>"
    Entonces el sistema responde con código HTTP <status>

    Ejemplos:
      | email                  | password       | status |
      | nuevo@restaurante.com  | Password123!   | 201    |
      | sinAtCorreo            | Password123!   | 400    |
      | nuevo@restaurante.com  | corta          | 400    |
      |                        | Password123!   | 400    |

  Escenario: Estructura de la respuesta de sign-in
    Dado que existe un usuario con email "admin@restaurante.com"
    Cuando se autentica correctamente
    Entonces la respuesta JSON contiene los campos:
      | campo    |
      | id       |
      | username |
      | token    |

  Escenario: Validación de token JWT en endpoints protegidos
    Cuando se solicita "GET /api/v1/users/me" sin cabecera Authorization
    Entonces el sistema responde con código HTTP 401

  Escenario: Token JWT inválido o malformado
    Cuando se solicita "GET /api/v1/users/me" con cabecera "Authorization: Bearer invalido.token.aqui"
    Entonces el sistema responde con código HTTP 401

  Escenario: Documentación OpenAPI expone los endpoints de IAM
    Cuando se solicita "GET /v3/api-docs"
    Entonces la respuesta contiene la ruta "/api/v1/authentication/sign-in"
    Y la respuesta contiene la ruta "/api/v1/authentication/sign-up"

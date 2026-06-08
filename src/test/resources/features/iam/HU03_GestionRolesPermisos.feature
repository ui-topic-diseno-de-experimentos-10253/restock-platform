# language: es
@iam @authorization
Característica: HU03 - Control de acceso basado en roles
  Como administrador principal del sistema
  Quiero gestionar roles y permisos
  Para controlar el acceso a funcionalidades según el tipo de usuario

  Antecedentes:
    Dado que el usuario autenticado tiene rol "ROLE_ADMIN"
    Y existen los roles "ROLE_ADMIN", "ROLE_EMPLEADO_VENTAS" y "ROLE_JEFE_COCINA"

  Escenario: Asignar rol a un usuario existente
    Dado que existe el usuario "empleado@restaurante.com" sin roles asignados
    Cuando el administrador solicita "POST /api/v1/users/{id}/roles" con rol "ROLE_EMPLEADO_VENTAS"
    Entonces el sistema responde con código HTTP 200
    Y el usuario "empleado@restaurante.com" tiene asignado el rol "ROLE_EMPLEADO_VENTAS"

  Escenario: Usuario con rol limitado intenta acceder a módulo restringido
    Dado que el usuario "empleado@restaurante.com" tiene rol "ROLE_EMPLEADO_VENTAS"
    Y se autentica con sus credenciales
    Cuando solicita "GET /api/v1/suppliers"
    Entonces el sistema responde con código HTTP 403
    Y la respuesta contiene el mensaje "No tienes permisos para acceder a esta sección"
    Y se registra el intento en el log de seguridad

  Escenario: Crear rol personalizado
    Cuando el administrador solicita "POST /api/v1/roles" con cuerpo:
      | nombre        | permisos                                                       |
      | JEFE_COCINA   | INVENTORY_WRITE,RECIPES_WRITE,SUPPLIERS_READ,SALES_READ        |
    Entonces el sistema responde con código HTTP 201
    Y el rol "JEFE_COCINA" queda disponible para asignar

  Escenario: Listar roles disponibles
    Cuando el administrador solicita "GET /api/v1/roles"
    Entonces el sistema responde con código HTTP 200
    Y la respuesta contiene al menos 3 roles

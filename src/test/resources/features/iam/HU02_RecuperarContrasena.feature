# language: es
@iam @authentication
Característica: HU02 - Recuperación y cambio de contraseña
  Como usuario que ha olvidado su contraseña
  Quiero solicitar y aplicar una recuperación de contraseña
  Para recuperar el acceso a mi cuenta de forma segura

  Antecedentes:
    Dado que existe un usuario registrado con email "owner@restaurante.com"

  Escenario: Solicitud exitosa de enlace de recuperación
    Cuando el usuario solicita "POST /api/v1/authentication/forgot-password" con email "owner@restaurante.com"
    Entonces el sistema responde con código HTTP 202
    Y se envía un correo con un enlace temporal de recuperación
    Y el enlace expira en 1 hora

  Escenario: Solicitud con email no registrado
    Cuando el usuario solicita recuperación con email "fantasma@correo.com"
    Entonces el sistema responde con código HTTP 202
    Y no se envía ningún correo
    Y la respuesta no revela si el email existe

  Escenario: Restablecimiento de contraseña con token válido
    Dado que el usuario tiene un token de recuperación válido
    Cuando envía la nueva contraseña "NuevaSegura123!" al endpoint "POST /api/v1/authentication/reset-password"
    Entonces el sistema responde con código HTTP 200
    Y la contraseña se actualiza en el sistema
    Y el token de recuperación se invalida tras su uso

  Escenario: Restablecimiento con token expirado
    Dado que el usuario tiene un token de recuperación con más de 1 hora de antigüedad
    Cuando intenta restablecer su contraseña
    Entonces el sistema responde con código HTTP 410
    Y la respuesta contiene el mensaje "Enlace de recuperación expirado"

  Escenario: Cambio de contraseña desde el perfil con sesión activa
    Dado que el usuario ha iniciado sesión
    Cuando solicita "PUT /api/v1/users/me/password" con contraseña actual y contraseña nueva válidas
    Entonces el sistema responde con código HTTP 200
    Y la nueva contraseña queda almacenada con hashing BCrypt
    Y la sesión continúa activa

# language: es
@iam @authentication
Característica: HU01 - Inicio de sesión seguro
  Como usuario registrado del sistema Restock
  Quiero iniciar sesión con mis credenciales
  Para acceder a las funcionalidades de la plataforma

  Antecedentes:
    Dado que existe un usuario registrado con email "admin@restaurante.com" y contraseña "Password123!"
    Y el endpoint de autenticación "/api/v1/authentication/sign-in" está activo

  Escenario: Inicio de sesión exitoso con credenciales válidas
    Cuando el usuario envía email "admin@restaurante.com" y contraseña "Password123!"
    Entonces el sistema responde con código HTTP 200
    Y la respuesta contiene un token JWT válido
    Y la respuesta contiene el id del usuario
    Y se registra la fecha y hora del último acceso

  Escenario: Intento de inicio de sesión con contraseña incorrecta
    Cuando el usuario envía email "admin@restaurante.com" y contraseña "Wrong123!"
    Entonces el sistema responde con código HTTP 401
    Y la respuesta contiene el mensaje "Email o contraseña incorrectos"
    Y no se emite ningún token JWT

  Escenario: Intento de inicio de sesión con email no registrado
    Cuando el usuario envía email "noexiste@restaurante.com" y contraseña "Password123!"
    Entonces el sistema responde con código HTTP 401
    Y la respuesta contiene el mensaje "Email o contraseña incorrectos"

  Escenario: Bloqueo temporal por múltiples intentos fallidos
    Dado que el usuario ha realizado 5 intentos fallidos consecutivos
    Cuando intenta autenticarse nuevamente con email "admin@restaurante.com"
    Entonces el sistema responde con código HTTP 423
    Y la respuesta contiene el mensaje "Cuenta bloqueada por seguridad. Intente más tarde o recupere su contraseña"
    Y se envía email de alerta al propietario de la cuenta

  Escenario: Cierre de sesión seguro
    Dado que el usuario tiene una sesión activa con token JWT válido
    Cuando solicita "/api/v1/authentication/sign-out"
    Entonces el sistema invalida el token de sesión
    Y la respuesta tiene código HTTP 204

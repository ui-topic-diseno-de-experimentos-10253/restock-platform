# language: es
@profile
Característica: HU04 - Actualización de perfil personal
  Como usuario registrado
  Quiero mantener mi información personal actualizada
  Para que el sistema y los proveedores tengan datos correctos

  Antecedentes:
    Dado que el usuario ha iniciado sesión con id "user-001"
    Y existe un perfil asociado al usuario "user-001"

  Escenario: Actualización exitosa de información personal
    Cuando el usuario solicita "PUT /api/v1/profiles/me/personal-information" con los campos:
      | campo     | valor             |
      | firstName | Carlos            |
      | lastName  | Rodríguez         |
      | phone     | +51 999888777     |
      | country   | Perú              |
    Entonces el sistema responde con código HTTP 200
    Y la respuesta contiene "Carlos" en el campo "firstName"
    Y se persisten los cambios en la base de datos

  Escenario: Intento de actualización con teléfono inválido
    Cuando el usuario solicita "PUT /api/v1/profiles/me/personal-information" con teléfono "abc-no-valido"
    Entonces el sistema responde con código HTTP 400
    Y la respuesta indica el error de validación del campo "phone"

  Escenario: Cargar foto de perfil válida
    Cuando el usuario sube una imagen JPG de 2 MB al endpoint "POST /api/v1/profiles/me/avatar"
    Entonces el sistema responde con código HTTP 200
    Y la respuesta contiene una URL pública de Cloudinary
    Y la foto queda asociada al perfil del usuario

  Escenario: Rechazo de foto que excede el tamaño máximo
    Cuando el usuario sube una imagen PNG de 8 MB
    Entonces el sistema responde con código HTTP 413
    Y la respuesta contiene el mensaje "El archivo excede el tamaño máximo permitido (5MB)"

  Escenario: Visualizar estadísticas del perfil
    Cuando el usuario solicita "GET /api/v1/profiles/me/statistics"
    Entonces el sistema responde con código HTTP 200
    Y la respuesta contiene las métricas:
      | metrica         |
      | registeredAt    |
      | lastAccessAt    |
      | salesCount      |
      | ordersCompleted |
      | averageRating   |

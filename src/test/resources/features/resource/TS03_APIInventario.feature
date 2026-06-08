# language: es
@resource @api @technical
Característica: TS03 - Contrato de la API de Inventario
  Como integrador del sistema
  Quiero que la API de inventario sea estable
  Para construir clientes web y móvil confiables

  Escenario: Listar insumos paginados
    Dado que existen 25 insumos registrados
    Cuando se solicita "GET /api/v1/custom-supplies?page=0&size=10"
    Entonces el sistema responde con código HTTP 200
    Y la respuesta contiene 10 elementos
    Y la respuesta contiene metadatos de paginación

  Escenario: Filtrar insumos por categoría
    Dado que existen insumos en las categorías "Aceites" y "Granos"
    Cuando se solicita "GET /api/v1/custom-supplies?category=Aceites"
    Entonces el sistema responde con código HTTP 200
    Y todos los elementos retornados pertenecen a la categoría "Aceites"

  Esquema del escenario: Validación de creación de insumo
    Cuando se envía creación con name "<name>", unit "<unit>" y stockMin "<min>"
    Entonces el sistema responde con código HTTP <status>

    Ejemplos:
      | name            | unit    | min | status |
      | Aceite          | Litros  | 5   | 201    |
      |                 | Litros  | 5   | 400    |
      | Aceite          |         | 5   | 400    |
      | Aceite          | Litros  | -3  | 400    |

  Escenario: Acceso a inventario sin token
    Cuando se solicita "GET /api/v1/custom-supplies" sin token
    Entonces el sistema responde con código HTTP 401

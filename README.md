# Gestión de proyectos e Incidencias
APIRest construida con Spring Boot y PostgreSQL, con autenticación basada en JWT, refresh tokens y roles de usuario, incluyendo filtros, paginación y pruebas unitarias

## Qué demuestra este proyecto
* Seguridad real: JWT + Refresh tokens, roles y contraseñas encriptadas con BCrypt
* Diseño profesional de API: Uso de DTOs, validaciones y manejo global de excepciones y errores
* Reglas de negocio robustas: Control de transiciones de estado de incidencias
* Backend estructurado: Arquitectura en capas, mapeo con MapStruct y tests unitarios
* Despliegue y documentación usando Docker, docker-compose y Swagger / OpenAPI

## Funcionalidades principales
* Registro, login y logout de usuarios
* CRUD de proyectos e incidencias
* Asignación de incidencias y control de estados
* Filtros por estado, usuario y fechas
* Paginación y validaciones automáticas
* Manejo global de erroes con loggin

## Tecnologías
Java 17 | Spring Boot 4 | Spring Boot Security | Spring Data JPA | PostgreSQL | JWT | Docker | Junit | Mockito | MapStruct | Lombok | Swagger  

## Ejecución rápida
  Local
  > ./mvnw spring-boot:run

  Docker 
  > docker-compose up --build


## Nota
Este README es la versión resumida
Para ver la versión completa [Documentación técnica](docs/README_technical.md)

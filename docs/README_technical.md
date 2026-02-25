# Project and Issue Management
REST API built with Spring Boot and PostgreSQL, featuring JWT-based authentication, refresh tokens, user roles, including filters, pagination, and unit testing.

### Technologies
* Backend: Spring Boot, Spring Security, Spring Data JPA
* Database: PostgreSQL
* Authentication: JWT + Refresh tokens, BCrypt
* Testing: JUnit, Mockito
* Containers: Docker + Docker Compose
* Documentation: Swagger / OpenAPI
* Mapping: MapStruct
* Boilerplate reduction: Lombok

### Security
* assword encryption with BCrypt
* Stateless authentication with JWT
* Refresh Token implementation persisted in the database
* Custom JWT filter
* Role-based access control
* SecurityFilterChain configuration

# Features
### Authentication
* User registration
* Login and Logout
* Access Token generation
* Renewal via Refresh Token
* Endpoint protection

### Project Management
* Full CRUD
* Validations with @Valid

### Issue Management
* Full CRUD
* Assigning issues to users
* Filters by:
  * Status
  * User
  * Date range
* Pagination with Pageable
  
### Issue Status Transition Control
Issues implement a simple state machine to define valid transitions between statuses, allowing:
* Avoid inconsistent changes
* Centralize business rules
* Improve maintainability
* Facilitate testability

### Testing
* Unit tests for the Issue service
* Valid creation tests
* Filter tests
* Basic security tests

## API Design
### Use of DTOs
DTOs are implemented to separate the persistence model from the API-exposed model, allowing:
* Avoid exposing entities directly
* Control data sent/received
* Improve security
Mapping is done using MapStruct

### Global Exception Handling
The API implements a centralized error handling system via @ControllerAdvice, ensuring consistent responses, with goals to:
* Avoid repeated logic across controllers
* Differentiate technical errors from business errors
* Return coherent HTTP responses
* Facilitate debugging with structured logging

A hierarchy has been implemented to classify errors:
* BaseException -> Business errors (400 Bad Request)
* NotFoundBaseException -> Resource not found (404 Not Found)
* AuthenticationException -> Authentication errors (401 Unauthorized)
* AccessDeniedException -> Access denied (403 Forbidden)
* MethodArgumentNotValidException -> Validation errors (400 Bad Request)
* Exception -> Unexpected errors (500 Internal Server Error)

TAll exceptions return a structured object:
>{  
  "status":  
  "error":   
  "message":   
  "path":   
}

### Loggin
* Unhandled errors are logged via SLF4J, including:
* Request URI
* Error message
* Stack trace


## Installation
1. Clone the repository
> git clone https://github.com/alroar/IssueTrackerAPI/  
cd issue-tracker-api

2. Configure environment variables
   cp .env.example .env

    Credenciales para el desarrollo / produccion  
    DB_URL=  
    DB_USERNAME=  
    DB_PASSWORD=  
    DB_PORT=  
    
    Datos para configurar JWT  
    JWT_SECRET=  
    JWT_EXPTIME=  
    
    Puerto del servidor  
    SERVER_PORT=  
    
    Credenciales para el contenedor de la base de datos  
    POSTGRES_DB=  
    POSTGRES_USER=  
    POSTGRES_PASSWORD=  

3. Install dependencies  
./mvnw clean install  

4. Run the application  
  Local  
    ./mvnw spring-boot:run  
   Docker
    docker-compose up --build
5. Verify the connection
   The API will be available at
   > http://localhost:8080  

###
API Documentation: http://localhost:8080/docs

# Project and Issue Management
REST API built with Spring Boot and PostgreSQL, featuring JWT-based authentication, refresh tokens, and user roles, including filters, pagination, and unit testing

## What This Project Demonstrates
* Real security: JWT + Refresh tokens, roles, and BCrypt-encrypted passwords
* Professional API design: Use of DTOs, validations, and centralized exception/error handling
* Robust business rules: Issue status transition control
* Structured backend: Layered architecture, mapping with MapStruct, and unit tests
* Deployment and documentation: Docker, docker-compose, and Swagger / OpenAPI

## Main Features
* User registration, login, and logout
* CRUD for projects and issues
* Issue assignment and status control
* Filters by status, user, and date range
* Pagination and automatic validations
* Global error handling with structured logging

## Technologies
Java 17 | Spring Boot 4 | Spring Boot Security | Spring Data JPA | PostgreSQL | JWT | Docker | Junit | Mockito | MapStruct | Lombok | Swagger  

## Quick Start
  Local
  > ./mvnw spring-boot:run

  Docker 
  > docker-compose up --build


## Nota
This README is the summary version
For the full technical documentation, see the [Technical documentation](README_technical.md)

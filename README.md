# Hakan Burger – Backend

API REST del sistema de gestión de pedidos y delivery. Java 21 + Spring Boot + PostgreSQL.

## Requisitos

- JDK 21 (en Spring Tool Suite: *Window → Preferences → Java → Installed JREs*)
- PostgreSQL con una base de datos llamada `hakan_burger`

## Cómo ejecutarlo

1. Revisa usuario y clave de PostgreSQL en `src/main/resources/application.properties`.
2. En Spring Tool Suite: clic derecho al proyecto → **Run As → Spring Boot App**.
   O por consola: `mvnw spring-boot:run`
3. La API queda en http://localhost:8080/api

## Estructura (por módulo y capa, arquitectura MVC)

```
src/main/java/com/hakanburger/
├── pedidos/     controller · service · repository · model · dto
├── delivery/    controller · service · repository · model · dto
├── reportes/    controller · service · repository · model · dto
├── config/      configuración general (CORS, seguridad)
└── common/      cosas compartidas (excepciones, respuestas)
```

## Forma de trabajo

Ramas, commits y pull requests: ver [CONTRIBUTING.md](CONTRIBUTING.md).

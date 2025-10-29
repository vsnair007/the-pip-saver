# the-pip-saver

[![Repository size](https://img.shields.io/github/repo-size/vsnair007/the-pip-saver)](https://github.com/vsnair007/the-pip-saver) [![License](https://img.shields.io/badge/license-MIT-blue.svg)](LICENSE)

A small microservice-based Spring Boot project providing user management and authentication services with Docker, PostgreSQL, and Flyway database migrations.

## Table of contents
- [What the project does](#what-the-project-does)
- [Why the project is useful](#why-the-project-is-useful)
- [Repository layout](#repository-layout)
- [Getting started](#getting-started)
    - [Prerequisites](#prerequisites)
    - [Build and run (local JVM)](#build-and-run-local-jvm)
    - [Build and run with Docker Compose (recommended)](#build-and-run-with-docker-compose-recommended)
    - [Run tests](#run-tests)
    - [Configuration notes](#configuration-notes)

## What the project does
the-pip-saver is a microservice, mono-repo Spring Boot application that implements:
- `authService` — authentication-related endpoints and JWT/security concerns.
- `UserService` — user management (CRUD, schema in `schema.sql`).
- DB initialization and migrations using `db-init/init.sql` and `flyway/sql/V1__init.sql`.
- Containerization via `Dockerfile` in each service and orchestration via `docker-compose.yml`.

## Why the project is useful
- Clear separation of concerns: auth and user management as independent services.
- Docker-ready for local/dev environment parity.
- Automated DB migrations via Flyway.
- Maven wrapper included for reproducible builds.

## Repository layout
Top-level important files and directories:
- `docker-compose.yml` — composition for services and DB
- `db-init/` — initial SQL for DB setup
- `flyway/sql/` — Flyway migration scripts
- `authService/` — authentication microservice (Spring Boot)
    - `Dockerfile`, `wait-for-postgres.sh`, `src/`, `HELP.md`
- `UserService/` — user microservice (Spring Boot)
    - `Dockerfile`, `wait-for-postgres.sh`, `src/`

Check service configuration in `src/main/resources` for each service: `application.properties`, `application-dev.properties`, `application-prod.properties`.

## Getting started

### Prerequisites
- Java 17+ (recommended for modern Spring Boot)
- Maven (optional; `./mvnw` wrapper included)
- Docker & Docker Compose (for containerized run)
- Git

### Build and run with Docker Compose (recommended)
This will build service images and start a PostgreSQL container:
```bash
# from repo root
docker-compose up --build
```
- `wait-for-postgres.sh` scripts are provided in services to ensure DB readiness.
- Flyway migrations are located in `flyway/sql/`. Verify DB connection properties in each service `src/main/resources`.


### Build and run (local JVM)
Build both services:
```bash
# from repo root
./authService/mvnw -f authService/pom.xml clean package
./UserService/mvnw -f UserService/pom.xml clean package
```

Run an individual service locally (example: `authService`):
```bash
cd authService
./mvnw spring-boot:run
```
Check logs and open endpoints as configured in the service `application.properties`.

### Run tests
Run unit/integration tests for each service:
```bash
# authService
cd authService
./mvnw test

# UserService
cd ../UserService
./mvnw test
```

### Configuration notes
- Environment-specific configs: check `src/main/resources/application-*.properties` for each service.
- Database schema: `db-init/init.sql` and `flyway/sql/V1__init.sql` initialize the DB.
- To change DB credentials/host/port, update the corresponding `application-*.properties` or pass environment variables via `docker-compose.yml`.

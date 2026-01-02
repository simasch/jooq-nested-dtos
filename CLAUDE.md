# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Build Commands

```bash
# Build and generate jOOQ classes (requires Docker for Testcontainers)
./mvnw compile

# Run all tests
./mvnw test

# Run a single test class
./mvnw test -Dtest=JooqNestedDtosApplicationTests

# Run the application with Testcontainers (for local development)
./mvnw spring-boot:test-run
```

## Architecture

This is a Spring Boot 4.0 demo application showcasing **jOOQ nested DTOs with MULTISET** for efficient hierarchical data querying.

### Technology Stack
- Java 25
- Spring Boot 4.0.1 with WebMVC
- jOOQ for type-safe SQL queries
- PostgreSQL database
- Flyway for database migrations
- Testcontainers for database in tests and jOOQ code generation

### Key Pattern: jOOQ MULTISET Mapping

The core demonstration is in `PurchaseOrderRepository.java`, which uses jOOQ's `row()` and `multiset()` functions to fetch nested object graphs in a single query:

- `row(...).mapping(DTO::new)` - Maps a single related entity (e.g., Customer)
- `multiset(...).convertFrom(r -> r.map(mapping(DTO::new)))` - Maps a collection of related entities (e.g., OrderItems with nested Products)

### Data Model
```
Customer (1) <-- (*) PurchaseOrder (1) <-- (*) OrderItem (*) --> (1) Product
```

### Package Structure
- `ch.martinelli.demo.nested.api` - REST controllers
- `ch.martinelli.demo.nested.repository` - jOOQ repositories and DTO records
- `ch.martinelli.demo.nested.db` - Generated jOOQ classes (auto-generated from schema)

### jOOQ Code Generation

jOOQ classes are generated during `mvn compile` via:
1. Groovy plugin starts a PostgreSQL Testcontainer
2. Flyway migrates the schema from `src/main/resources/db/migration`
3. jOOQ codegen generates classes to `src/main/java/ch/martinelli/demo/nested/db`

The generated `db` package should not be manually edited.

### Running Locally

Use `TestJooqNestedDtosApplication` to run with a Testcontainer PostgreSQL instance:
```bash
./mvnw spring-boot:test-run
```

Test the API: `GET http://localhost:8080/orders`

# jOOQ Nested DTOs with MULTISET

A Spring Boot demo application showcasing **jOOQ's MULTISET feature** for fetching nested object graphs in a single SQL
query. This eliminates the N+1 query problem while providing type-safe, compile-time checked queries.

## Technology Stack

- Java 25
- Spring Boot 4.0.1
- jOOQ (type-safe SQL)
- PostgreSQL
- Flyway (database migrations)
- Testcontainers (database for tests and jOOQ code generation)

## The Problem: N+1 Queries

Traditional ORM approaches often result in N+1 queries when fetching related entities:

```
1 query for orders
+ N queries for customers (one per order)
+ N queries for order items (one per order)
+ M queries for products (one per order item)
```

## The Solution: jOOQ MULTISET

jOOQ's `MULTISET` feature fetches the entire object graph in a **single query** using SQL's native capabilities.
See the example in [PurchaseOrderRepository](src/main/java/ch/martinelli/demo/nested/repository/PurchaseOrderRepository.java)

**Key patterns:**

- `row(...).mapping(DTO::new)` - Maps a single related entity (e.g., Customer)
- `multiset(...).convertFrom(r -> r.map(mapping(DTO::new)))` - Maps a collection of related entities (e.g., OrderItems)

## Data Model

```
Customer (1) <-- (*) PurchaseOrder (1) <-- (*) OrderItem (*) --> (1) Product
```

| Table            | Description                          |
|------------------|--------------------------------------|
| `customer`       | Customer information (name, address) |
| `product`        | Product catalog (name, price)        |
| `purchase_order` | Orders with customer reference       |
| `order_item`     | Line items with product and quantity |

## Project Structure

```
src/main/java/ch/martinelli/demo/nested/
├── api/                    # REST controllers
│   └── OrderController.java
├── repository/             # jOOQ repositories and DTOs
│   ├── PurchaseOrderRepository.java
│   ├── PurchaseOrderDTO.java
│   ├── CustomerDTO.java
│   ├── OrderItemDTO.java
│   └── ProductDTO.java
└── db/                     # Generated jOOQ classes (auto-generated)
```

## Prerequisites

- Java 25+
- Maven 3.9+
- Docker (required for Testcontainers)

## Build Commands

```bash
# Build and generate jOOQ classes
./mvnw compile

# Run all tests
./mvnw test

# Run a specific test class
./mvnw test -Dtest=JooqNestedDtosApplicationTests
```

## Running Locally

Start the application with a Testcontainer PostgreSQL instance:

```bash
./mvnw spring-boot:test-run
```

Test the API:

```bash
curl http://localhost:8080/orders
```

## jOOQ Code Generation

jOOQ classes are automatically generated during `mvn compile`:

1. Groovy plugin starts a PostgreSQL Testcontainer
2. Flyway migrates the schema from `src/main/resources/db/migration`
3. jOOQ codegen generates type-safe classes to `src/main/java/ch/martinelli/demo/nested/db`

The generated `db` package should not be manually edited.

## API Endpoints

| Method | Path      | Description                                                      |
|--------|-----------|------------------------------------------------------------------|
| GET    | `/orders` | Returns all purchase orders with nested customer and order items |

## Example Response

```json
[
  {
    "id": 1,
    "orderDate": "2024-01-15T10:30:00",
    "customer": {
      "id": 1,
      "firstName": "John",
      "lastName": "Doe",
      "street": "123 Main St",
      "postalCode": "12345",
      "city": "Springfield"
    },
    "items": [
      {
        "id": 1,
        "quantity": 2,
        "product": {
          "id": 1,
          "name": "Widget",
          "price": 29.99
        }
      }
    ]
  }
]
```

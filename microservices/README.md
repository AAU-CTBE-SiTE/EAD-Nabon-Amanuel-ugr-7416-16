# Customer & Orders Microservices

Two Spring Boot microservices that talk to each other with **RestClient**.

**Name:** Nabon Amanuel
**ID:** UGR/7416/16

| Service  | Port | Database                | Talks to                                     |
|----------|------|-------------------------|----------------------------------------------|
| customer | 8081 | H2 (in-memory)          | calls **orders** to list a customer's orders |
| orders   | 8082 | MySQL (`orders_db`)     | calls **customer** to validate `customerId`  |

## How they communicate
- `GET /api/customers/{id}/orders` (customer) -> RestClient -> `GET /api/orders/customer/{id}` (orders)
- `POST/PUT /api/orders` (orders) -> RestClient -> `GET /api/customers/{id}` (customer) to check the customer exists

## Run
1. Start MySQL. The password is read from the `DB_PASSWORD` environment variable (default `root`).
2. Start **CustomerApplication** (port 8081), then **OrdersApplication** (port 8082).
   Or from the terminal:
   ```bash
   ./mvnw -pl customer spring-boot:run
   ./mvnw -pl orders spring-boot:run
   ```
3. H2 console for customers: http://localhost:8081/h2-console (JDBC URL `jdbc:h2:mem:customerdb`, user `sa`, empty password)

## Endpoints
**Customer (8081)** `/api/customers`: GET, GET `/{id}`, POST, PUT `/{id}`, DELETE `/{id}`, GET `/{id}/orders`

**Orders (8082)** `/api/orders`: GET, GET `/{id}`, GET `/customer/{customerId}`, POST, PUT `/{id}`, DELETE `/{id}`

## Try it
```bash
curl -X POST http://localhost:8081/api/customers -H "Content-Type: application/json" \
  -d '{"name":"Nabon amanuel","email":"abebe@example.com"}'

curl -X POST http://localhost:8082/api/orders -H "Content-Type: application/json" \
  -d '{"product":"Laptop Computer","quantity":1,"price":10,000.0,"customerId":1}'

curl http://localhost:8081/api/customers/1/orders
```
Note: customer data lives in H2 (memory), so restart customer and you must re-create customers.

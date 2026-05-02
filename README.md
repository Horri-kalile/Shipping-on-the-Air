# Shipping-on-the-Air

![Java](https://img.shields.io/badge/Java-17-ED8B00?style=for-the-badge&logo=java&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.2.0-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white)
![Python](https://img.shields.io/badge/Python-3.11-3776AB?style=for-the-badge&logo=python&logoColor=white)
![Docker](https://img.shields.io/badge/Docker-Orchestrated-2496ED?style=for-the-badge&logo=docker&logoColor=white)
![Kafka](https://img.shields.io/badge/Kafka-Event--Driven-000000?style=for-the-badge&logo=apache-kafka&logoColor=white)
![CI/CD](https://img.shields.io/badge/GitHub_Actions-Verified-2088FF?style=for-the-badge&logo=github-actions&logoColor=white)

**Shipping-on-the-Air** is a drone-based package delivery ecosystem. This project was designed to showcase modern software engineering practices, moving from monolithic thinking to a fully decoupled, event-driven microservices architecture.

---

## Key Project Goals

This project serves as a comprehensive demonstration of:

- **Domain-Driven Design (DDD):** Modeling complex business logic using Aggregates and Value Objects.
- **Hexagonal Architecture:** Keeping the business "heart" clean by using Ports & Adapters.
- **Full-Scale DevOps:** Automated CI/CD pipelines with multi-platform matrix builds.
- **Polyglot Development:** Integrating Java 17 (Core services) with Python 3.11 (Tracking service).
- **Container Orchestration:** Managing a complex 10-container ecosystem with Docker Compose.

---

## Architecture & Design Patterns

### Domain-Driven Design (The Heart)

The core logic (like `Delivery.java` or `Drone.java`) is strictly isolated. We use **Value Objects** (like `Weight` or `Location`) to enforce business invariants—for example, the system physically prevents creating a delivery heavier than 2.3kg at the domain level.

### Hexagonal Architecture (The Adapters)

Our services are "Plug & Play". The core logic doesn't know if it's talking to MongoDB, Kafka, or a REST API. We use **Inbound Ports** (Controllers) and **Outbound Adapters** (Repositories/Proxies) to keep the system flexible and highly testable.

### Event-Driven Communication

Services communicate asynchronously via **Kafka (Redpanda)**. When a drone updates its position, it doesn't call the tracking service directly. Instead, it publishes a `DroneLocationUpdatedEvent`. This loose coupling allows the system to scale and remain resilient.

---

## Getting Started

### Prerequisites

- Docker & Docker Compose
- Java 17 (for local builds)
- Python 3.11 (for local tracking service)

### Running the Ecosystem

Build the Java artifacts and spin up all 10 containers (Gateways, Services, DB, Kafka):

```bash
./gradlew build -x test
docker compose up --build
```

### Live Demo Script

Once the system is up, you can interact with the entire ecosystem through the **API Gateway** (Port 8080):

1.  **Register a User:**
    ```bash
    curl -X POST "http://localhost:8080/api/users/register?name=test&email=test@example.com&password=SecurePassword123"
    ```
2.  **Request a Delivery:**
    ```bash
    curl -X POST http://localhost:8080/api/deliveries -H "Content-Type: application/json" -d '{"userId":"test@example.com", "pickupLocation":{"latitude":45.0, "longitude":5.0}, "dropoffLocation":{"latitude":45.1, "longitude":5.1}, "weight":1.5}'
    ```
3.  **Track in Real-Time (Python Service):**
    ```bash
    curl http://localhost:8080/api/tracking/order-1234
    ```

---

## DevOps & CI/CD

Our **Verification Pipeline** ensures that every single commit meets high-quality standards:

- **Matrix Testing:** Automatically verified on both **Ubuntu** and **Windows** runners.
- **Static Analysis:** Enforced code style via **Checkstyle** and bug detection via **PMD**.
- **Test Coverage:** Integrated **JaCoCo** to measure business logic verification.
- **Automated Delivery:** Docker images are automatically built upon successful verification on the `main` branch.

---

## License

This project is licensed under the **MIT License**.

# TransactGuard Backend

![Java](https://img.shields.io/badge/Java-21-007396?logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-6DB33F?logo=springboot&logoColor=white)
![Spring Security](https://img.shields.io/badge/Spring%20Security-JWT-6DB33F?logo=springsecurity&logoColor=white)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-Database-4169E1?logo=postgresql&logoColor=white)
![Status](https://img.shields.io/badge/Status-Completed-success)

A secure REST API for **TransactGuard**, a banking simulation platform featuring JWT authentication, role-based access control, transaction management, and automated fraud detection.

---

## Features

- User registration and authentication
- JWT-based authorization
- Role-based access control
- Profile management
- Money transfers between users
- Transaction history
- Automated fraud detection
- Fraud flag management for administrators
- Global exception handling
- Validation with structured API responses

---

## Fraud Detection Rules

Transactions are automatically flagged when one or more of the following conditions are met:

- Large transactions exceeding **R10,000**
- Transfers that drain more than **70%** of the sender's balance
- Three or more transactions performed within **one minute**

Fraud flags are stored separately from transactions, allowing administrators to review and resolve suspicious activity without affecting transaction records.

---

## Tech Stack

- Java 21
- Spring Boot 3.x
- Spring Security
- Spring Data JPA
- PostgreSQL
- JWT (JSON Web Tokens)
- BCrypt Password Encoder
- Maven

---

## Architecture

The application follows a layered architecture consisting of Controllers, Services, Repositories, DTOs, and Entities.

Authentication is handled using JWT through Spring Security before protected endpoints are accessed. Business rules are implemented within the service layer, while Spring Data JPA manages persistence. Fraud detection executes during transaction processing, allowing suspicious activity to be flagged automatically without interrupting legitimate transactions.

---

## Project Structure

```text
src/main/java/com/transactguard/
├── config/
├── controller/
├── dto/
├── entity/
├── exception/
├── repo/
├── security/
└── service/
```

---

## Getting Started

### Prerequisites

- Java 21+
- Maven 3.8+
- PostgreSQL

### Clone the repository

```bash
git clone https://github.com/Ofentse-Magidela/transactguard.git
cd transactguard
```

### Configure the application

Create the required environment variables or configure `application.properties`.

```properties
server.port=8080

spring.datasource.url=jdbc:postgresql://localhost:5432/transactguard
spring.datasource.username=postgres
spring.datasource.password=your_password

jwt.secret=your_jwt_secret
```

### Build the project

```bash
mvn clean install
```

### Run the application

```bash
mvn spring-boot:run
```

The API will be available at:

```
http://localhost:8080
```

---

## API Endpoints

### Authentication

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/auth/register` | Register a new user |
| POST | `/auth/login` | Authenticate and receive a JWT |

### User

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/user/{id}` | Retrieve user profile |
| GET | `/user/balance/{id}` | Retrieve account balance |
| PUT | `/user/{id}` | Update profile information |

### Transactions

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/transact/send` | Transfer money |
| GET | `/transact/{id}` | Retrieve transaction |
| GET | `/transact/send/history/{id}` | Sent transaction history |
| GET | `/transact/received/history/{id}` | Received transaction history |

### Administration

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/admin/flags` | View unresolved fraud flags |
| POST | `/admin/resolve/{id}` | Resolve a fraud flag |

---

## Authentication

Protected endpoints require a valid JWT.

```
Authorization: Bearer <jwt_token>
```

---

## Key Design Decisions

- Stateless authentication using JWT
- Password hashing with BCrypt
- Transaction processing wrapped in `@Transactional` to ensure atomicity
- Fraud flags stored independently from transactions to support multiple fraud reasons per transaction
- Role-based authorization using `@PreAuthorize`
- Centralized exception handling with `@RestControllerAdvice`

---

## Testing

The API has been manually tested using Postman.

Typical workflow:

1. Register a user
2. Authenticate to obtain a JWT
3. Access protected endpoints
4. Perform transactions
5. Trigger fraud detection rules
6. Resolve fraud flags through an administrator account

Automated unit and integration tests are planned for future iterations.

---

## Deployment

Backend API

https://transactguard-backend.onrender.com

---
## Frontend

Frontend Repository

https://github.com/Ofentse-Magidela/transactguard-frontend

Live Application

https://transactguard.vercel.app

---

## AI Assistance

AI tools were used to assist with documentation, development productivity, and implementation suggestions. The backend architecture, business logic, authentication, fraud detection workflow, API design, and final implementation were designed, integrated, tested, and reviewed by the author.

---

## Author

**Ofentse Magidela**

GitHub: https://github.com/Ofentse-Magidela

---

## License

This project is intended for educational and portfolio purposes.
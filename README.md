# TransactGuard - Banking Fraud Detection System (Backend)

A secure REST API for a banking simulation system with integrated fraud detection, JWT authentication, and role-based access control.

## Features

- **User Management** - Registration, login, profile updates with BCrypt password hashing
- **Transaction System** - Send money between users with balance validation and transaction history
- **Fraud Detection** - Automated flags for suspicious activity based on:
  - Large transactions (> R10,000)
  - High balance drain (> 70% of balance in single transaction)
  - Rapid transactions (3+ transactions within 1 minute)
- **Admin Dashboard** - View all flagged transactions and resolve fraud cases
- **Security** - JWT authentication, Spring Security, CSRF protection, role-based access (@PreAuthorize)
- **Exception Handling** - Global error responses for validation, runtime, and server errors

## Tech Stack

- **Backend**: Java 21, Spring Boot 3.x, Spring Security, Spring Data JPA
- **Database**: H2 (development), MySQL (production-ready)
- **Authentication**: JWT (JSON Web Tokens), BCrypt password encoding
- **Build Tool**: Maven
- **API Testing**: Postman

## Setup & Installation

### Prerequisites
- Java 21+
- Maven 3.8+
- MySQL 8.0+ (for production)

### Clone Repository
```bash
git clone https://github.com/Ofentse-Magidela/transactguard.git
cd transactguard
```

### Configure Database
Edit `application.properties`:
```properties
# H2 (default - development)
spring.datasource.url=jdbc:h2:mem:transactguard
spring.h2.console.enabled=true

# MySQL (production)
spring.datasource.url=jdbc:mysql://localhost:3306/transactguard
spring.datasource.username=root
spring.datasource.password=your_password
```

### Build & Run
```bash
mvn clean install
mvn spring-boot:run
```

Server runs on `http://localhost:8080`

Access H2 Console: `http://localhost:8080/h2-console`

## API Endpoints

### Authentication
- `POST /auth/register` - Register new user
- `POST /auth/login` - Login, receive JWT token

### User Management
- `GET /user/{id}` - Get user profile
- `GET /user/balance/{id}` - Get user balance
- `PUT /user/{id}` - Update user profile

### Transactions
- `POST /transact/send` - Send money (requires auth token)
- `GET /transact/{id}` - Get transaction details
- `GET /transact/send/history/{id}` - Sender transaction history
- `GET /transact/received/history/{id}` - Receiver transaction history

### Admin (Requires ADMIN role)
- `GET /admin/flags` - View all unresolved fraud flags
- `POST /admin/resolve/{id}` - Resolve fraud flag

## Authentication

Include JWT token in request headers:
```
Authorization: Bearer <your_jwt_token>
```

## Project Structure

```
src/main/java/com/transactguard/
├── entity/          # JPA entities (User, Transaction, FraudFlag)
├── repo/            # Spring Data repositories
├── service/         # Business logic (UserService, TransactionService, FraudService, AdminService)
├── controller/      # REST endpoints
├── dto/             # Data Transfer Objects
├── security/        # JWT, Spring Security config
├── exception/       # Global exception handling
└── config/          # Application configuration
```

## Key Design Decisions

- **JWT Over Sessions**: Stateless authentication for scalability
- **@Transactional on sendMoney**: Ensures atomicity - both balance updates succeed or both fail
- **FraudFlag as separate entity**: One transaction can have multiple fraud reasons
- **Role-based access**: ADMIN and USER roles enforced with @PreAuthorize
- **Exception Handling**: Centralized @RestControllerAdvice catches all errors

## Testing

Use Postman to test endpoints:
1. Register a user
2. Login to get JWT token
3. Use token to call protected endpoints
4. Trigger fraud flags by sending large amounts or rapid transactions
5. Admin login to view and resolve flags

## Future Improvements

- PostgreSQL migration for production
- Transaction rollback/reversal feature
- Advanced fraud ML model
- Rate limiting on APIs
- Comprehensive unit/integration tests
- Swagger API documentation
- Email notifications for flagged transactions

## Deployment

Ready for deployment to Railway or Render. Update `application.properties` with production database and environment variables.

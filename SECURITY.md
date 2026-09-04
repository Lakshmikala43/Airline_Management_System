# Security Architecture & Policies — SkyNova Airways

## Zero API Keys & Secret Management Policy

> **CRITICAL SECURITY NOTICE**: 
> This repository is 100% free of real API keys, production credentials, database passwords, payment gateway tokens, or private secrets. All configurable values use local placeholders defined in `.env.example`.

### 1. Mock Provider Abstraction
To ensure full offline local execution and prevent dependency on paid or external services:
- **Payment Gateway**: Uses `MockPaymentProvider` simulating payment authorization code generation, test credit card verification, and transaction settlement without external network requests. Real card numbers and CVV codes are NEVER accepted or stored.
- **Notifications Engine**: Uses `MockNotificationProvider` which logs notifications internally to the PostgreSQL/H2 audit table and in-app customer notification center.
- **Flight Data Provider**: Uses `LocalFlightDataProvider` providing high-precision deterministic schedules.

### 2. Authentication & JWT Security
- **Algorithm**: HMAC-SHA256 JWT tokens.
- **Password Hashing**: BCrypt hashing with configurable strength factor (cost factor 10). Plaintext passwords are NEVER stored or logged.
- **Stateless Sessions**: JWT tokens are passed via `Authorization: Bearer <token>` HTTP headers.
- **Token Expiry**: Short-lived access tokens (24-hour default expiration).

### 3. Role-Based Access Control (RBAC)
Method-level authorization using Spring Security `@PreAuthorize`:
- `SUPER_ADMIN`: Full system access, audit logs, user role assignment.
- `ADMIN`: Manage flights, aircraft, airports, schedules, seat inventory, reports.
- `FLIGHT_MANAGER`: Manage assigned flight schedules, update status, view manifest.
- `CUSTOMER`: Search flights, reserve seats, simulated payment, download PDF tickets, cancel/reschedule bookings.

### 4. Input Validation & Data Protection
- Server-side DTO validation using Jakarta Validation annotations (`@NotNull`, `@Email`, `@Pattern`, `@Size`).
- Frontend input validation using React Hook Form + Zod schema validation.
- SQL Injection protection via Spring Data JPA parameterized queries and Hibernate ORM.
- XSS prevention via automatic React JSX output escaping.

### 5. Double-Booking Prevention & Concurrency Control
- Dynamic pessimistic database locking (`@Lock(LockModeType.PESSIMISTIC_WRITE)`) on seat inventory.
- Database unique constraint on composite key `(flight_id, seat_number)` preventing duplicate seat confirmations under high concurrent loads.

### 6. Audit Logging
Comprehensive administrative audit trail logging:
- Logged attributes: User ID, Action Type, Target Entity, Entity ID, Timestamp, IP Address.
- Sensitive fields (passwords, JWT secrets, mock card values) are sanitized and stripped prior to log emission.

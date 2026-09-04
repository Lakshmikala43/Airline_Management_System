# SkyNova Airways Database Schema & Data Dictionary

## Overview
The SkyNova Airways database features 24 normalized relational tables designed to handle user authentication, fleet management, schedule generation, seat locking, booking state transitions, payments, e-tickets, reviews, and audit logs.

---

### Table 1: `users`
| Column Name | Data Type | Constraints | Description |
| :--- | :--- | :--- | :--- |
| `id` | BIGSERIAL | PRIMARY KEY | Unique user record identifier |
| `email` | VARCHAR(100) | UNIQUE, NOT NULL | Account login email address |
| `password_hash` | VARCHAR(255) | NOT NULL | BCrypt password hash (cost factor 10) |
| `first_name` | VARCHAR(50) | NOT NULL | User given name |
| `last_name` | VARCHAR(50) | NOT NULL | User family surname |
| `phone_number` | VARCHAR(20) | NULLABLE | Primary contact phone |
| `date_of_birth` | DATE | NULLABLE | Date of birth for passport validation |
| `gender` | VARCHAR(20) | NULLABLE | Gender identity |
| `nationality` | VARCHAR(50) | NULLABLE | Citizenship country |
| `passport_number` | VARCHAR(50) | NULLABLE | Primary travel document number |
| `passport_expiry` | DATE | NULLABLE | Passport expiration date |
| `is_active` | BOOLEAN | DEFAULT TRUE | Account operational status |
| `is_email_verified` | BOOLEAN | DEFAULT FALSE | Verification status |
| `last_login_at` | TIMESTAMP WITH TIME ZONE | NULLABLE | Last session timestamp |
| `created_at` | TIMESTAMP WITH TIME ZONE | DEFAULT CURRENT_TIMESTAMP | Record creation timestamp |
| `updated_at` | TIMESTAMP WITH TIME ZONE | DEFAULT CURRENT_TIMESTAMP | Last modification timestamp |

---

### Table 2: `roles`
| Column Name | Data Type | Constraints | Description |
| :--- | :--- | :--- | :--- |
| `id` | BIGSERIAL | PRIMARY KEY | Role identifier |
| `name` | VARCHAR(50) | UNIQUE, NOT NULL | Role designation (`ROLE_SUPER_ADMIN`, `ROLE_ADMIN`, `ROLE_FLIGHT_MANAGER`, `ROLE_CUSTOMER`) |
| `description` | VARCHAR(255) | NULLABLE | Human-readable role description |

---

### Table 3: `airports`
| Column Name | Data Type | Constraints | Description |
| :--- | :--- | :--- | :--- |
| `id` | BIGSERIAL | PRIMARY KEY | Airport identifier |
| `iata_code` | VARCHAR(3) | UNIQUE, NOT NULL | 3-letter IATA code (e.g., JFK, LHR) |
| `icao_code` | VARCHAR(4) | UNIQUE, NULLABLE | 4-letter ICAO code |
| `name` | VARCHAR(150) | NOT NULL | Official airport title |
| `city` | VARCHAR(100) | NOT NULL | Served municipality |
| `country` | VARCHAR(100) | NOT NULL | Sovereign nation |
| `latitude` | DOUBLE PRECISION | NULLABLE | Geographic latitude coordinate |
| `longitude` | DOUBLE PRECISION | NULLABLE | Geographic longitude coordinate |
| `time_zone` | VARCHAR(50) | NOT NULL | IANA time zone string (e.g., America/New_York) |
| `terminal_info` | VARCHAR(255) | NULLABLE | Terminal layout summary |

---

### Table 4: `aircraft`
| Column Name | Data Type | Constraints | Description |
| :--- | :--- | :--- | :--- |
| `id` | BIGSERIAL | PRIMARY KEY | Aircraft identifier |
| `registration_number` | VARCHAR(20) | UNIQUE, NOT NULL | Tail registration code (e.g., N701SN) |
| `airline_id` | BIGINT | FOREIGN KEY | Operating airline reference |
| `manufacturer` | VARCHAR(100) | NOT NULL | Manufacturer (Boeing, Airbus) |
| `model` | VARCHAR(100) | NOT NULL | Model designation (Boeing 787-9) |
| `total_capacity` | INT | NOT NULL | Total seat count |
| `economy_capacity` | INT | DEFAULT 0 | Economy cabin seat count |
| `premium_economy_capacity` | INT | DEFAULT 0 | Premium Economy cabin seat count |
| `business_capacity` | INT | DEFAULT 0 | Business cabin seat count |
| `first_class_capacity` | INT | DEFAULT 0 | First Class cabin seat count |
| `status` | VARCHAR(50) | DEFAULT 'ACTIVE' | Fleet status (`ACTIVE`, `MAINTENANCE`, `RETIRED`) |

---

### Table 5: `aircraft_seats`
| Column Name | Data Type | Constraints | Description |
| :--- | :--- | :--- | :--- |
| `id` | BIGSERIAL | PRIMARY KEY | Seat record identifier |
| `aircraft_id` | BIGINT | FOREIGN KEY | Associated aircraft |
| `seat_number` | VARCHAR(10) | NOT NULL | Seat code (12A, 1F) |
| `seat_row` | INT | NOT NULL | Row index number |
| `seat_column` | VARCHAR(5) | NOT NULL | Column letter (A, B, C, D, E, F) |
| `cabin_class` | VARCHAR(50) | NOT NULL | Cabin section (`ECONOMY`, `PREMIUM_ECONOMY`, `BUSINESS`, `FIRST_CLASS`) |
| `is_window` | BOOLEAN | DEFAULT FALSE | Window seat indicator |
| `is_aisle` | BOOLEAN | DEFAULT FALSE | Aisle seat indicator |
| `is_exit_row` | BOOLEAN | DEFAULT FALSE | Emergency exit row indicator |
| `extra_legroom` | BOOLEAN | DEFAULT FALSE | Premium legroom indicator |
| `is_blocked` | BOOLEAN | DEFAULT FALSE | Maintenance blocked indicator |

---

### Table 6: `routes`
| Column Name | Data Type | Constraints | Description |
| :--- | :--- | :--- | :--- |
| `id` | BIGSERIAL | PRIMARY KEY | Route identifier |
| `route_code` | VARCHAR(20) | UNIQUE, NOT NULL | Route code (e.g., JFK-LHR) |
| `origin_airport_id` | BIGINT | FOREIGN KEY | Departure airport ID |
| `destination_airport_id` | BIGINT | FOREIGN KEY | Arrival airport ID |
| `distance_km` | DOUBLE PRECISION | NOT NULL | Flight distance in kilometers |
| `estimated_duration_minutes` | INT | NOT NULL | Estimated flight time in minutes |

---

### Table 7: `flights`
| Column Name | Data Type | Constraints | Description |
| :--- | :--- | :--- | :--- |
| `id` | BIGSERIAL | PRIMARY KEY | Flight instance identifier |
| `flight_number` | VARCHAR(20) | UNIQUE, NOT NULL | Flight number (SN-101) |
| `airline_id` | BIGINT | FOREIGN KEY | Airline ID |
| `route_id` | BIGINT | FOREIGN KEY | Route ID |
| `aircraft_id` | BIGINT | FOREIGN KEY | Aircraft ID |
| `departure_time` | TIMESTAMP WITH TIME ZONE | NOT NULL | Scheduled departure time |
| `arrival_time` | TIMESTAMP WITH TIME ZONE | NOT NULL | Scheduled arrival time |
| `base_price` | NUMERIC(10,2) | NOT NULL | Base fare price |
| `tax_amount` | NUMERIC(10,2) | DEFAULT 0.00 | Airport tax amount |
| `status` | VARCHAR(50) | DEFAULT 'SCHEDULED' | Operational status (`SCHEDULED`, `BOARDING`, `DEPARTED`, `IN_FLIGHT`, `ARRIVED`, `CANCELLED`) |

---

### Table 8: `bookings`
| Column Name | Data Type | Constraints | Description |
| :--- | :--- | :--- | :--- |
| `id` | BIGSERIAL | PRIMARY KEY | Booking identifier |
| `pnr` | VARCHAR(6) | UNIQUE, NOT NULL | 6-character unique alphanumeric PNR |
| `user_id` | BIGINT | FOREIGN KEY | Booking customer ID |
| `flight_id` | BIGINT | FOREIGN KEY | Reserved flight ID |
| `cabin_class` | VARCHAR(50) | NOT NULL | Booked cabin class |
| `passenger_count` | INT | DEFAULT 1 | Total passengers on booking |
| `base_fare` | NUMERIC(10,2) | NOT NULL | Subtotal base price |
| `tax_amount` | NUMERIC(10,2) | DEFAULT 0.00 | Total tax charge |
| `seat_fee` | NUMERIC(10,2) | DEFAULT 0.00 | Seat selection surcharges |
| `discount_amount` | NUMERIC(10,2) | DEFAULT 0.00 | Applied coupon discount |
| `total_amount` | NUMERIC(10,2) | NOT NULL | Final charged amount |
| `status` | VARCHAR(50) | DEFAULT 'INITIATED' | Booking state machine status (`INITIATED`, `PENDING_PAYMENT`, `CONFIRMED`, `CANCELLED`, `REFUNDED`) |

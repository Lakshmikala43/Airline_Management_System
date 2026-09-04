-- V4__create_bookings_passengers_tickets.sql
-- SkyNova Airways - Bookings, Passenger Manifest & E-Tickets Domain Schema

CREATE TABLE bookings (
    id BIGSERIAL PRIMARY KEY,
    pnr VARCHAR(6) NOT NULL UNIQUE,
    user_id BIGINT NOT NULL REFERENCES users(id),
    flight_id BIGINT NOT NULL REFERENCES flights(id),
    return_flight_id BIGINT REFERENCES flights(id),
    trip_type VARCHAR(20) NOT NULL DEFAULT 'ONE_WAY', -- ONE_WAY, ROUND_TRIP
    cabin_class VARCHAR(50) NOT NULL, -- ECONOMY, PREMIUM_ECONOMY, BUSINESS, FIRST_CLASS
    passenger_count INT NOT NULL DEFAULT 1,
    base_fare NUMERIC(10,2) NOT NULL,
    tax_amount NUMERIC(10,2) NOT NULL DEFAULT 0.00,
    seat_fee NUMERIC(10,2) NOT NULL DEFAULT 0.00,
    baggage_fee NUMERIC(10,2) NOT NULL DEFAULT 0.00,
    discount_amount NUMERIC(10,2) NOT NULL DEFAULT 0.00,
    total_amount NUMERIC(10,2) NOT NULL,
    status VARCHAR(50) NOT NULL DEFAULT 'INITIATED', -- INITIATED, PENDING_PAYMENT, PAYMENT_SUCCESS, PAYMENT_FAILED, CONFIRMED, COMPLETED, CANCELLED, RESCHEDULED, REFUNDED
    booking_date TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE passengers (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(10),
    first_name VARCHAR(50) NOT NULL,
    middle_name VARCHAR(50),
    last_name VARCHAR(50) NOT NULL,
    date_of_birth DATE NOT NULL,
    gender VARCHAR(20) NOT NULL,
    nationality VARCHAR(50) NOT NULL,
    passport_number VARCHAR(50) NOT NULL,
    passport_expiry DATE NOT NULL,
    email VARCHAR(100),
    phone_number VARCHAR(20),
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE booking_passengers (
    id BIGSERIAL PRIMARY KEY,
    booking_id BIGINT NOT NULL REFERENCES bookings(id) ON DELETE CASCADE,
    passenger_id BIGINT NOT NULL REFERENCES passengers(id) ON DELETE CASCADE,
    passenger_type VARCHAR(20) NOT NULL DEFAULT 'ADULT', -- ADULT, CHILD, INFANT
    special_request VARCHAR(255),
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uk_booking_passenger UNIQUE (booking_id, passenger_id)
);

CREATE TABLE booking_seats (
    id BIGSERIAL PRIMARY KEY,
    booking_id BIGINT NOT NULL REFERENCES bookings(id) ON DELETE CASCADE,
    flight_id BIGINT NOT NULL REFERENCES flights(id),
    seat_id BIGINT NOT NULL REFERENCES aircraft_seats(id),
    passenger_id BIGINT NOT NULL REFERENCES passengers(id),
    seat_price NUMERIC(10,2) NOT NULL DEFAULT 0.00,
    reserved_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uk_flight_seat_reserved UNIQUE (flight_id, seat_id)
);

CREATE TABLE tickets (
    id BIGSERIAL PRIMARY KEY,
    ticket_number VARCHAR(20) NOT NULL UNIQUE,
    booking_id BIGINT NOT NULL REFERENCES bookings(id) ON DELETE CASCADE,
    passenger_id BIGINT NOT NULL REFERENCES passengers(id),
    pnr VARCHAR(6) NOT NULL,
    issue_date TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    qr_code_data TEXT NOT NULL,
    pdf_file_path VARCHAR(500),
    status VARCHAR(50) NOT NULL DEFAULT 'ISSUED', -- ISSUED, CANCELLED, REISSUED
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uk_booking_passenger_ticket UNIQUE (booking_id, passenger_id)
);

-- Booking search indexes
CREATE INDEX idx_bookings_pnr ON bookings(pnr);
CREATE INDEX idx_bookings_user ON bookings(user_id);
CREATE INDEX idx_bookings_flight ON bookings(flight_id);
CREATE INDEX idx_bookings_status ON bookings(status);
CREATE INDEX idx_passengers_passport ON passengers(passport_number);
CREATE INDEX idx_tickets_number ON tickets(ticket_number);
CREATE INDEX idx_tickets_pnr ON tickets(pnr);

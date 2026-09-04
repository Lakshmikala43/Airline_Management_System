-- V3__create_flights_schedules_pricing.sql
-- SkyNova Airways - Flight Schedules & Pricing Domain Schema

CREATE TABLE cabin_classes (
    id BIGSERIAL PRIMARY KEY,
    code VARCHAR(50) NOT NULL UNIQUE, -- ECONOMY, PREMIUM_ECONOMY, BUSINESS, FIRST_CLASS
    name VARCHAR(100) NOT NULL,
    base_fare_multiplier NUMERIC(5,2) NOT NULL DEFAULT 1.00,
    baggage_allowance_kg INT NOT NULL DEFAULT 20,
    cabin_baggage_allowance_kg INT NOT NULL DEFAULT 7,
    is_refundable_by_default BOOLEAN NOT NULL DEFAULT TRUE,
    change_fee_percentage NUMERIC(5,2) NOT NULL DEFAULT 10.00,
    description VARCHAR(255)
);

CREATE TABLE flights (
    id BIGSERIAL PRIMARY KEY,
    flight_number VARCHAR(20) NOT NULL UNIQUE,
    airline_id BIGINT NOT NULL REFERENCES airlines(id),
    route_id BIGINT NOT NULL REFERENCES routes(id),
    aircraft_id BIGINT NOT NULL REFERENCES aircraft(id),
    departure_time TIMESTAMP WITH TIME ZONE NOT NULL,
    arrival_time TIMESTAMP WITH TIME ZONE NOT NULL,
    base_price NUMERIC(10,2) NOT NULL,
    tax_amount NUMERIC(10,2) NOT NULL DEFAULT 0.00,
    status VARCHAR(50) NOT NULL DEFAULT 'SCHEDULED', -- SCHEDULED, BOARDING, DEPARTED, IN_FLIGHT, ARRIVED, DELAYED, CANCELLED, DIVERTED
    gate_number VARCHAR(20),
    terminal VARCHAR(20),
    delay_minutes INT DEFAULT 0,
    cancellation_reason VARCHAR(255),
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE flight_schedules (
    id BIGSERIAL PRIMARY KEY,
    flight_id BIGINT NOT NULL REFERENCES flights(id) ON DELETE CASCADE,
    operating_days VARCHAR(50) NOT NULL, -- e.g. MON,WED,FRI
    departure_time_local TIME NOT NULL,
    arrival_time_local TIME NOT NULL,
    effective_start_date DATE NOT NULL,
    effective_end_date DATE NOT NULL,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE fare_rules (
    id BIGSERIAL PRIMARY KEY,
    flight_id BIGINT NOT NULL REFERENCES flights(id) ON DELETE CASCADE,
    cabin_class VARCHAR(50) NOT NULL,
    price_multiplier NUMERIC(5,2) NOT NULL DEFAULT 1.00,
    available_seats INT NOT NULL,
    cancellation_fee_hours_48 NUMERIC(10,2) NOT NULL DEFAULT 50.00,
    cancellation_fee_hours_24 NUMERIC(10,2) NOT NULL DEFAULT 100.00,
    cancellation_fee_hours_under24 NUMERIC(10,2) NOT NULL DEFAULT 200.00,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uk_flight_cabin_fare UNIQUE (flight_id, cabin_class)
);

-- Search and schedule indexes
CREATE INDEX idx_flights_number ON flights(flight_number);
CREATE INDEX idx_flights_departure ON flights(departure_time);
CREATE INDEX idx_flights_arrival ON flights(arrival_time);
CREATE INDEX idx_flights_status ON flights(status);
CREATE INDEX idx_flights_route ON flights(route_id);

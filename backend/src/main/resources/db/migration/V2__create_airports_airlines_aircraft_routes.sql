-- V2__create_airports_airlines_aircraft_routes.sql
-- SkyNova Airways - Infrastructure & Fleet Domain Schema

CREATE TABLE airlines (
    id BIGSERIAL PRIMARY KEY,
    code VARCHAR(10) NOT NULL UNIQUE,
    name VARCHAR(100) NOT NULL,
    country VARCHAR(100) NOT NULL,
    logo_url VARCHAR(500),
    contact_email VARCHAR(100),
    contact_phone VARCHAR(50),
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE airports (
    id BIGSERIAL PRIMARY KEY,
    iata_code VARCHAR(3) NOT NULL UNIQUE,
    icao_code VARCHAR(4) UNIQUE,
    name VARCHAR(150) NOT NULL,
    city VARCHAR(100) NOT NULL,
    country VARCHAR(100) NOT NULL,
    latitude DOUBLE PRECISION,
    longitude DOUBLE PRECISION,
    time_zone VARCHAR(50) NOT NULL,
    terminal_info VARCHAR(255),
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE aircraft (
    id BIGSERIAL PRIMARY KEY,
    registration_number VARCHAR(20) NOT NULL UNIQUE,
    airline_id BIGINT NOT NULL REFERENCES airlines(id),
    manufacturer VARCHAR(100) NOT NULL,
    model VARCHAR(100) NOT NULL,
    total_capacity INT NOT NULL,
    economy_capacity INT NOT NULL DEFAULT 0,
    premium_economy_capacity INT NOT NULL DEFAULT 0,
    business_capacity INT NOT NULL DEFAULT 0,
    first_class_capacity INT NOT NULL DEFAULT 0,
    status VARCHAR(50) NOT NULL DEFAULT 'ACTIVE', -- ACTIVE, MAINTENANCE, RETIRED
    manufacture_year INT,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE aircraft_seats (
    id BIGSERIAL PRIMARY KEY,
    aircraft_id BIGINT NOT NULL REFERENCES aircraft(id) ON DELETE CASCADE,
    seat_number VARCHAR(10) NOT NULL,
    seat_row INT NOT NULL,
    seat_column VARCHAR(5) NOT NULL,
    cabin_class VARCHAR(50) NOT NULL, -- ECONOMY, PREMIUM_ECONOMY, BUSINESS, FIRST_CLASS
    is_window BOOLEAN NOT NULL DEFAULT FALSE,
    is_aisle BOOLEAN NOT NULL DEFAULT FALSE,
    is_exit_row BOOLEAN NOT NULL DEFAULT FALSE,
    extra_legroom BOOLEAN NOT NULL DEFAULT FALSE,
    is_blocked BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uk_aircraft_seat UNIQUE (aircraft_id, seat_number)
);

CREATE TABLE routes (
    id BIGSERIAL PRIMARY KEY,
    route_code VARCHAR(20) NOT NULL UNIQUE,
    origin_airport_id BIGINT NOT NULL REFERENCES airports(id),
    destination_airport_id BIGINT NOT NULL REFERENCES airports(id),
    distance_km DOUBLE PRECISION NOT NULL,
    estimated_duration_minutes INT NOT NULL,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uk_origin_destination UNIQUE (origin_airport_id, destination_airport_id)
);

-- Indexes for route searching and fleet management
CREATE INDEX idx_airports_iata ON airports(iata_code);
CREATE INDEX idx_airports_city ON airports(city);
CREATE INDEX idx_aircraft_reg ON aircraft(registration_number);
CREATE INDEX idx_aircraft_seats_aircraft ON aircraft_seats(aircraft_id);
CREATE INDEX idx_routes_origin_dest ON routes(origin_airport_id, destination_airport_id);

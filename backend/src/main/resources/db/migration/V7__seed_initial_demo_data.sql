-- V7__seed_initial_demo_data.sql
-- SkyNova Airways - Initial Seed Data for Demo Accounts, Fleet, Airports, and Schedules

-- 1. Insert Roles
INSERT INTO roles (id, name, description) VALUES
(1, 'ROLE_SUPER_ADMIN', 'Complete system control and administrator management'),
(2, 'ROLE_ADMIN', 'Flight management, aircraft, schedules, reports, and bookings'),
(3, 'ROLE_FLIGHT_MANAGER', 'Flight schedule monitoring, manifest management, status updates'),
(4, 'ROLE_CUSTOMER', 'Flight search, seat reservation, booking, and e-ticket downloads');

-- 2. Insert Demo Users (BCrypt Hashes for 'Password123!')
INSERT INTO users (id, email, password_hash, first_name, last_name, phone_number, is_active, is_email_verified) VALUES
(1, 'admin@skynova.demo', '$2a$10$e88yU70d.S9cZgE2/0Tlh./iM1/o7rA/a/E2Hq/Kq/8.2r5J7b9pS', 'Super', 'Administrator', '+15550192831', TRUE, TRUE),
(2, 'manager@skynova.demo', '$2a$10$e88yU70d.S9cZgE2/0Tlh./iM1/o7rA/a/E2Hq/Kq/8.2r5J7b9pS', 'Flight', 'Operations', '+15550192832', TRUE, TRUE),
(3, 'customer@skynova.demo', '$2a$10$e88yU70d.S9cZgE2/0Tlh./iM1/o7rA/a/E2Hq/Kq/8.2r5J7b9pS', 'John', 'Traveler', '+15550192833', TRUE, TRUE);

INSERT INTO user_roles (user_id, role_id) VALUES
(1, 1),
(1, 2),
(2, 3),
(3, 4);

-- 3. Insert Cabin Classes
INSERT INTO cabin_classes (id, code, name, base_fare_multiplier, baggage_allowance_kg, cabin_baggage_allowance_kg, is_refundable_by_default, change_fee_percentage, description) VALUES
(1, 'ECONOMY', 'Economy Class', 1.00, 23, 7, TRUE, 10.00, 'Standard comfortable seating with complimentary meal service'),
(2, 'PREMIUM_ECONOMY', 'Premium Economy', 1.35, 30, 10, TRUE, 8.00, 'Extra legroom, priority check-in, and enhanced dining'),
(3, 'BUSINESS', 'Business Class', 2.20, 40, 15, TRUE, 5.00, 'Lie-flat seats, premium lounge access, gourmet dining'),
(4, 'FIRST_CLASS', 'First Class', 3.80, 50, 20, TRUE, 0.00, 'Private suites, luxury chauffeur service, personalized service');

-- 4. Insert Airlines
INSERT INTO airlines (id, code, name, country, contact_email, contact_phone) VALUES
(1, 'SN', 'SkyNova Airways', 'United States', 'support@skynova.demo', '+1-800-SKYNOVA'),
(2, 'SE', 'SkyExpress Regional', 'United States', 'ops@skyexpress.demo', '+1-800-SKYEXP');

-- 5. Insert Airports (Including HYD, VTZ, VGA, BLR)
INSERT INTO airports (id, iata_code, icao_code, name, city, country, latitude, longitude, time_zone, terminal_info) VALUES
(1, 'JFK', 'KJFK', 'John F. Kennedy International Airport', 'New York', 'United States', 40.6413, -73.7781, 'America/New_York', 'Terminal 4, 7, 8'),
(2, 'LHR', 'EGLL', 'London Heathrow Airport', 'London', 'United Kingdom', 51.4700, -0.4543, 'Europe/London', 'Terminal 2, 3, 5'),
(3, 'CDG', 'LFPG', 'Charles de Gaulle Airport', 'Paris', 'France', 49.0097, 2.5479, 'Europe/Paris', 'Terminal 1, 2A, 2E'),
(4, 'DXB', 'OMDB', 'Dubai International Airport', 'Dubai', 'United Arab Emirates', 25.2532, 55.3657, 'Asia/Dubai', 'Terminal 1, 3'),
(5, 'SIN', 'WSSS', 'Singapore Changi Airport', 'Singapore', 'Singapore', 1.3644, 103.9915, 'Asia/Singapore', 'Terminal 1, 2, 3, 4'),
(6, 'HND', 'RJTT', 'Tokyo Haneda Airport', 'Tokyo', 'Japan', 35.5494, 139.7798, 'Asia/Tokyo', 'Terminal 1, 2, 3'),
(7, 'DEL', 'VIDP', 'Indira Gandhi International Airport', 'New Delhi', 'India', 28.5562, 77.1000, 'Asia/Kolkata', 'Terminal 3'),
(8, 'SYD', 'YSSY', 'Sydney Kingsford Smith Airport', 'Sydney', 'Australia', -33.9399, 151.1753, 'Australia/Sydney', 'Terminal 1 International'),
(9, 'SFO', 'KSFO', 'San Francisco International Airport', 'San Francisco', 'United States', 37.6213, -122.3790, 'America/Los_Angeles', 'International Terminal'),
(10, 'FRA', 'EDDF', 'Frankfurt Airport', 'Frankfurt', 'Germany', 50.0379, 8.5622, 'Europe/Berlin', 'Terminal 1, 2'),
(11, 'HYD', 'VOHS', 'Rajiv Gandhi International Airport', 'Hyderabad', 'India', 17.2403, 78.4294, 'Asia/Kolkata', 'Main Terminal'),
(12, 'VTZ', 'VOVZ', 'Visakhapatnam International Airport', 'Visakhapatnam', 'India', 17.7219, 83.2245, 'Asia/Kolkata', 'Main Terminal'),
(13, 'VGA', 'VOBZ', 'Vijayawada International Airport (Gannavaram)', 'Vijayawada', 'India', 16.5304, 80.7968, 'Asia/Kolkata', 'Terminal 1'),
(14, 'BLR', 'VOBL', 'Kempegowda International Airport', 'Bengaluru', 'India', 13.1986, 77.7066, 'Asia/Kolkata', 'Terminal 1, 2');

-- 6. Insert Aircraft
INSERT INTO aircraft (id, registration_number, airline_id, manufacturer, model, total_capacity, economy_capacity, premium_economy_capacity, business_capacity, first_class_capacity, status, manufacture_year) VALUES
(1, 'N701SN', 1, 'Boeing', 'Boeing 787-9 Dreamliner', 290, 200, 40, 40, 10, 'ACTIVE', 2022),
(2, 'N702SN', 1, 'Airbus', 'Airbus A350-900', 300, 210, 40, 40, 10, 'ACTIVE', 2023),
(3, 'N703SN', 1, 'Boeing', 'Boeing 777-300ER', 350, 250, 40, 50, 10, 'ACTIVE', 2021),
(4, 'N301SE', 2, 'Airbus', 'Airbus A320neo', 180, 150, 18, 12, 0, 'ACTIVE', 2022);

-- 7. Insert Aircraft Seats Layout
INSERT INTO aircraft_seats (aircraft_id, seat_number, seat_row, seat_column, cabin_class, is_window, is_aisle, extra_legroom) VALUES
(1, '1A', 1, 'A', 'FIRST_CLASS', TRUE, FALSE, TRUE),
(1, '1B', 1, 'B', 'FIRST_CLASS', FALSE, TRUE, TRUE),
(1, '1E', 1, 'E', 'FIRST_CLASS', FALSE, TRUE, TRUE),
(1, '1F', 1, 'F', 'FIRST_CLASS', TRUE, FALSE, TRUE),
(1, '2A', 2, 'A', 'FIRST_CLASS', TRUE, FALSE, TRUE),
(1, '2B', 2, 'B', 'FIRST_CLASS', FALSE, TRUE, TRUE),
(1, '2E', 2, 'E', 'FIRST_CLASS', FALSE, TRUE, TRUE),
(1, '2F', 2, 'F', 'FIRST_CLASS', TRUE, FALSE, TRUE),
(1, '3A', 3, 'A', 'BUSINESS', TRUE, FALSE, TRUE),
(1, '3B', 3, 'B', 'BUSINESS', FALSE, TRUE, FALSE),
(1, '3C', 3, 'C', 'BUSINESS', FALSE, TRUE, FALSE),
(1, '3D', 3, 'D', 'BUSINESS', TRUE, FALSE, FALSE),
(1, '12A', 12, 'A', 'ECONOMY', TRUE, FALSE, FALSE),
(1, '12B', 12, 'B', 'ECONOMY', FALSE, FALSE, FALSE),
(1, '12C', 12, 'C', 'ECONOMY', FALSE, TRUE, FALSE);

-- 8. Insert Routes (Including HYD-VTZ, VTZ-HYD, VGA-BLR, BLR-VGA)
INSERT INTO routes (id, route_code, origin_airport_id, destination_airport_id, distance_km, estimated_duration_minutes) VALUES
(1, 'JFK-LHR', 1, 2, 5541, 415),
(2, 'LHR-JFK', 2, 1, 5541, 445),
(3, 'JFK-CDG', 1, 3, 5837, 435),
(4, 'JFK-DXB', 1, 4, 11000, 750),
(5, 'SFO-HND', 9, 6, 8280, 640),
(6, 'DEL-LHR', 7, 2, 6710, 540),
(7, 'SIN-SYD', 5, 8, 6300, 470),
(8, 'FRA-JFK', 10, 1, 6200, 510),
(9, 'HYD-VTZ', 11, 12, 620, 75),
(10, 'VTZ-HYD', 12, 11, 620, 75),
(11, 'VGA-BLR', 13, 14, 660, 90),
(12, 'BLR-VGA', 14, 13, 660, 90);

-- 9. Insert Promotions
INSERT INTO promotions (id, coupon_code, description, discount_type, discount_value, minimum_booking_amount, maximum_discount_amount, start_date, end_date, usage_limit, times_used, is_active) VALUES
(1, 'SKYNOVA10', '10% instant discount on all international flights', 'PERCENTAGE', 10.00, 200.00, 150.00, '2026-01-01 00:00:00+00', '2027-12-31 23:59:59+00', 5000, 42, TRUE),
(2, 'WELCOME50', '$50 flat discount for new SkyNova members', 'FLAT_AMOUNT', 50.00, 150.00, 50.00, '2026-01-01 00:00:00+00', '2027-12-31 23:59:59+00', 2000, 115, TRUE);

ALTER SEQUENCE users_id_seq RESTART WITH 10;
ALTER SEQUENCE roles_id_seq RESTART WITH 10;
ALTER SEQUENCE cabin_classes_id_seq RESTART WITH 10;
ALTER SEQUENCE airlines_id_seq RESTART WITH 10;
ALTER SEQUENCE airports_id_seq RESTART WITH 30;
ALTER SEQUENCE aircraft_id_seq RESTART WITH 10;
ALTER SEQUENCE routes_id_seq RESTART WITH 30;
ALTER SEQUENCE promotions_id_seq RESTART WITH 10;

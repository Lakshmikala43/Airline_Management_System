# SkyNova Airways REST API Specification (OpenAPI 3.0)

openapi: 3.0.3
info:
  title: SkyNova Airways RESTful API Architecture
  description: Enterprise API platform for Smart Airline Reservation and Management System.
  version: 1.0.0
servers:
  - url: http://localhost:8080/api/v1
    description: Local Development & Viva Demonstration Server

paths:
  /auth/login:
    post:
      summary: User Login Authentication
      description: Authenticates user credentials and returns JWT bearer token with assigned roles.
      requestBody:
        required: true
        content:
          application/json:
            schema:
              type: object
              required: [email, password]
              properties:
                email:
                  type: string
                  example: admin@skynova.demo
                password:
                  type: string
                  example: Password123!
      responses:
        '200':
          description: Successful authentication
          content:
            application/json:
              schema:
                type: object
                properties:
                  token: { type: string }
                  type: { type: string, example: Bearer }
                  email: { type: string }
                  roles: { type: array, items: { type: string } }

  /auth/register:
    post:
      summary: Customer Registration
      description: Registers new customer profile with BCrypt password hashing.
      requestBody:
        required: true
        content:
          application/json:
            schema:
              type: object
              required: [firstName, lastName, email, password]
              properties:
                firstName: { type: string }
                lastName: { type: string }
                email: { type: string }
                password: { type: string }
                phoneNumber: { type: string }
                dateOfBirth: { type: string }
                passportNumber: { type: string }
      responses:
        '200':
          description: User registered successfully

  /flights/search:
    post:
      summary: Flight Search Query
      description: Returns flights matching origin, destination, departure date, passenger count, and cabin class.
      requestBody:
        required: true
        content:
          application/json:
            schema:
              type: object
              required: [originAirportIata, destinationAirportIata, departureDate]
              properties:
                originAirportIata: { type: string, example: JFK }
                destinationAirportIata: { type: string, example: LHR }
                departureDate: { type: string, example: '2026-09-10' }
                passengerCount: { type: integer, example: 1 }
                cabinClass: { type: string, example: ECONOMY }
      responses:
        '200':
          description: List of matching flight schedules with calculated fares

  /flights/{flightId}/seats:
    get:
      summary: Interactive Seat Map Grid
      description: Returns aircraft seat map layout with occupied and surcharge statuses.
      parameters:
        - name: flightId
          in: path
          required: true
          schema: { type: integer }
      responses:
        '200':
          description: Seat layout matrix for requested flight

  /bookings:
    post:
      summary: Create Flight Booking
      description: Initiates flight booking, validates seat locking, and generates unique PNR.
      requestBody:
        required: true
        content:
          application/json:
            schema:
              type: object
              required: [flightId, cabinClass, passengers]
              properties:
                flightId: { type: integer }
                cabinClass: { type: string }
                passengers:
                  type: array
                  items:
                    type: object
                    properties:
                      firstName: { type: string }
                      lastName: { type: string }
                      passportNumber: { type: string }
                      selectedSeatId: { type: integer }
      responses:
        '200':
          description: Booking created in PENDING_PAYMENT state with 6-char PNR

  /payments/process:
    post:
      summary: Simulated Payment Authorization
      description: Processes mock credit card payment, updates booking to CONFIRMED, and issues E-Tickets.
      requestBody:
        required: true
        content:
          application/json:
            schema:
              type: object
              required: [bookingId, paymentMethod]
              properties:
                bookingId: { type: integer }
                paymentMethod: { type: string, example: MOCK_CARD }
                cardHolderName: { type: string }
                cardNumber: { type: string }
      responses:
        '200':
          description: Payment authorized and E-Ticket generated

  /tickets/verify/{ticketNumber}:
    get:
      summary: Public Ticket Verification
      description: Verifies authenticity of SkyNova Airways electronic boarding pass.
      parameters:
        - name: ticketNumber
          in: path
          required: true
          schema: { type: string }
      responses:
        '200':
          description: Ticket verification status and passenger payload

  /reports/dashboard:
    get:
      summary: Admin Telemetry Analytics
      description: Returns KPI counts and Recharts time-series data points.
      responses:
        '200':
          description: System analytics dashboard object

  /health:
    get:
      summary: Operational Health Check
      responses:
        '200':
          description: Service status UP

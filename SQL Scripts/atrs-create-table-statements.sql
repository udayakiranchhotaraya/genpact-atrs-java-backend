-- =====================================================
-- Airline Ticket Reservation System - MySQL 8.0
-- =====================================================

-- =====================================================
-- CORE ENTITIES
-- =====================================================

-- Airlines Table
CREATE TABLE airlines (
    id BIGINT UNSIGNED AUTO_INCREMENT,
    airlines_uuid VARCHAR(36) NOT NULL,
    airline_code VARCHAR(3) NOT NULL COMMENT 'IATA airline code',
    airline_name VARCHAR(100) NOT NULL,
    country VARCHAR(100) NOT NULL,
    logo_url VARCHAR(500) NULL COMMENT 'S3 URL for airline logo',
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT pk_airlines PRIMARY KEY (id),
    CONSTRAINT uq_airlines_uuid UNIQUE (airlines_uuid),
    CONSTRAINT uq_airlines_code UNIQUE (airline_code),
    INDEX idx_airlines_deleted (is_deleted, airline_code)
);

-- Aircraft Types Table
CREATE TABLE aircraft_types (
    id BIGINT UNSIGNED AUTO_INCREMENT,
    aircraft_types_uuid VARCHAR(36) NOT NULL,
    airline_id BIGINT UNSIGNED NOT NULL,
    aircraft_model VARCHAR(50) NOT NULL COMMENT 'e.g., Boeing 737-800, Airbus A320',
    manufacturer VARCHAR(50) NOT NULL,
    total_seats SMALLINT UNSIGNED NOT NULL,
    business_class_seats SMALLINT UNSIGNED NOT NULL DEFAULT 0,
    economy_class_seats SMALLINT UNSIGNED NOT NULL,
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT pk_aircraft_types PRIMARY KEY (id),
    CONSTRAINT uq_aircraft_types_uuid UNIQUE (aircraft_types_uuid),
    CONSTRAINT fk_aircraft_types_airline FOREIGN KEY (airline_id)
        REFERENCES airlines(id) ON DELETE RESTRICT ON UPDATE CASCADE,
    INDEX idx_aircraft_types_airline (airline_id, is_deleted)
);

-- Airports Table
CREATE TABLE airports (
    id BIGINT UNSIGNED AUTO_INCREMENT,
    airports_uuid VARCHAR(36) NOT NULL,
    airport_code VARCHAR(3) NOT NULL COMMENT 'IATA airport code',
    airport_name VARCHAR(100) NOT NULL,
    city VARCHAR(100) NOT NULL,
    country VARCHAR(100) NOT NULL,
    timezone VARCHAR(50) NOT NULL,
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT pk_airports PRIMARY KEY (id),
    CONSTRAINT uq_airports_uuid UNIQUE (airports_uuid),
    CONSTRAINT uq_airports_code UNIQUE (airport_code),
    INDEX idx_airports_location (country, city, is_deleted)
);

-- Flights Table
CREATE TABLE flights (
    id BIGINT UNSIGNED AUTO_INCREMENT,
    flights_uuid VARCHAR(36) NOT NULL,
    airline_id BIGINT UNSIGNED NOT NULL,
    aircraft_type_id BIGINT UNSIGNED NOT NULL,
    flight_number VARCHAR(10) NOT NULL,
    departure_airport_id BIGINT UNSIGNED NOT NULL,
    arrival_airport_id BIGINT UNSIGNED NOT NULL,
    scheduled_departure DATETIME NOT NULL,
    scheduled_arrival DATETIME NOT NULL,
    actual_departure DATETIME NULL,
    actual_arrival DATETIME NULL,
    status ENUM('SCHEDULED', 'BOARDING', 'DEPARTED', 'ARRIVED', 'CANCELLED', 'DELAYED') NOT NULL DEFAULT 'SCHEDULED',
    base_economy_price DECIMAL(10, 2) NOT NULL COMMENT 'Base price for economy class',
    base_business_price DECIMAL(10, 2) NOT NULL COMMENT 'Base price for business class',
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT pk_flights PRIMARY KEY (id),
    CONSTRAINT uq_flights_uuid UNIQUE (flights_uuid),
    CONSTRAINT fk_flights_airline FOREIGN KEY (airline_id)
        REFERENCES airlines(id) ON DELETE RESTRICT ON UPDATE CASCADE,
    CONSTRAINT fk_flights_aircraft_type FOREIGN KEY (aircraft_type_id)
        REFERENCES aircraft_types(id) ON DELETE RESTRICT ON UPDATE CASCADE,
    CONSTRAINT fk_flights_departure_airport FOREIGN KEY (departure_airport_id)
        REFERENCES airports(id) ON DELETE RESTRICT ON UPDATE CASCADE,
    CONSTRAINT fk_flights_arrival_airport FOREIGN KEY (arrival_airport_id)
        REFERENCES airports(id) ON DELETE RESTRICT ON UPDATE CASCADE,
    -- CONSTRAINT chk_flights_route_different CHECK (departure_airport_id != arrival_airport_id) // TO BE IMPLEMENTED IN JAVA SERVICE LAYER,
    INDEX idx_flights_schedule (scheduled_departure, departure_airport_id, is_deleted),
    INDEX idx_flights_route (departure_airport_id, arrival_airport_id, scheduled_departure),
    INDEX idx_flights_airline (airline_id, flight_number, scheduled_departure)
);

-- =====================================================
-- USER MANAGEMENT
-- =====================================================

-- Users Table
CREATE TABLE users (
    id BIGINT UNSIGNED AUTO_INCREMENT,
    users_uuid VARCHAR(36) NOT NULL,
    email VARCHAR(255) NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    phone_number VARCHAR(20) NULL,
    date_of_birth DATE NULL,
    passport_number VARCHAR(50) NULL,
    profile_picture_url VARCHAR(500) NULL COMMENT 'S3 URL for user profile picture',
    frequent_flyer_tier ENUM('NONE', 'SILVER', 'GOLD', 'PLATINUM') NOT NULL DEFAULT 'NONE',
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT pk_users PRIMARY KEY (id),
    CONSTRAINT uq_users_uuid UNIQUE (users_uuid),
    CONSTRAINT uq_users_email UNIQUE (email),
    INDEX idx_users_email (email, is_deleted)
);

-- =====================================================
-- BOOKING SYSTEM
-- =====================================================

-- Bookings Table (Group booking for multiple passengers)
CREATE TABLE bookings (
    id BIGINT UNSIGNED AUTO_INCREMENT,
    bookings_uuid VARCHAR(36) NOT NULL,
    pnr VARCHAR(6) NOT NULL COMMENT '6-character alphanumeric PNR',
    user_id BIGINT UNSIGNED NOT NULL COMMENT 'Primary contact for booking',
    booking_status ENUM('PENDING', 'CONFIRMED', 'CANCELLED', 'COMPLETED') NOT NULL DEFAULT 'PENDING',
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT pk_bookings PRIMARY KEY (id),
    CONSTRAINT uq_bookings_uuid UNIQUE (bookings_uuid),
    CONSTRAINT uq_bookings_pnr UNIQUE (pnr),
    CONSTRAINT fk_bookings_user FOREIGN KEY (user_id)
        REFERENCES users(id) ON DELETE RESTRICT ON UPDATE CASCADE,
    INDEX idx_bookings_user (user_id, booking_status, is_deleted),
    INDEX idx_bookings_pnr (pnr, is_deleted)
);

-- Passengers Table (Individual travelers in a booking)
CREATE TABLE passengers (
    id BIGINT UNSIGNED AUTO_INCREMENT,
    passengers_uuid VARCHAR(36) NOT NULL,
    booking_id BIGINT UNSIGNED NOT NULL,
    user_id BIGINT UNSIGNED NULL COMMENT 'Link to user account if passenger is registered',
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    date_of_birth DATE NOT NULL,
    passport_number VARCHAR(50) NULL,
    nationality VARCHAR(100) NULL,
    passenger_type ENUM('ADULT', 'CHILD', 'INFANT') NOT NULL DEFAULT 'ADULT',
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT pk_passengers PRIMARY KEY (id),
    CONSTRAINT uq_passengers_uuid UNIQUE (passengers_uuid),
    CONSTRAINT fk_passengers_booking FOREIGN KEY (booking_id)
        REFERENCES bookings(id) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_passengers_user FOREIGN KEY (user_id)
        REFERENCES users(id) ON DELETE SET NULL ON UPDATE CASCADE,
    INDEX idx_passengers_booking (booking_id, is_deleted)
);

-- Tickets Table (One ticket per passenger per flight)
CREATE TABLE tickets (
    id BIGINT UNSIGNED AUTO_INCREMENT,
    tickets_uuid VARCHAR(36) NOT NULL,
    ticket_number VARCHAR(20) NOT NULL COMMENT 'Airline ticket number - 13 digits',
    booking_id BIGINT UNSIGNED NOT NULL,
    passenger_id BIGINT UNSIGNED NOT NULL,
    flight_id BIGINT UNSIGNED NOT NULL,
    seat_class ENUM('ECONOMY', 'BUSINESS', 'FIRST') NOT NULL DEFAULT 'ECONOMY',
    base_fare DECIMAL(10, 2) NOT NULL COMMENT 'Base ticket price',
    taxes DECIMAL(10, 2) NOT NULL COMMENT 'Taxes and airport fees',
    ancillary_charges DECIMAL(10, 2) NOT NULL DEFAULT 0.00 COMMENT 'Seat selection, Baggage, etc.',
    total_fare DECIMAL(10, 2) NOT NULL COMMENT 'Base fare + taxes + Ancillary charges',
    ticket_status ENUM('ISSUED', 'CHECKED_IN', 'BOARDED', 'CANCELLED', 'NO_SHOW') NOT NULL DEFAULT 'ISSUED',
    checked_in_at DATETIME NULL,
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT pk_tickets PRIMARY KEY (id),
    CONSTRAINT uq_tickets_uuid UNIQUE (tickets_uuid),
    CONSTRAINT uq_tickets_number UNIQUE (ticket_number),
    CONSTRAINT fk_tickets_booking FOREIGN KEY (booking_id)
        REFERENCES bookings(id) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_tickets_passenger FOREIGN KEY (passenger_id)
        REFERENCES passengers(id) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_tickets_flight FOREIGN KEY (flight_id)
        REFERENCES flights(id) ON DELETE RESTRICT ON UPDATE CASCADE,
    INDEX idx_tickets_booking (booking_id, is_deleted),
    INDEX idx_tickets_passenger (passenger_id, is_deleted),
    INDEX idx_tickets_flight (flight_id, ticket_status, is_deleted)
);

-- =====================================================
-- PAYMENT SYSTEM
-- =====================================================

-- Payments Table (Tracks all payment transactions)
CREATE TABLE payments (
    id BIGINT UNSIGNED AUTO_INCREMENT,
    payments_uuid VARCHAR(36) NOT NULL,
    booking_id BIGINT UNSIGNED NOT NULL,
    payment_method ENUM('CREDIT_CARD', 'DEBIT_CARD', 'UPI', 'NET_BANKING', 'WALLET', 'CASH') NOT NULL,
    payment_provider VARCHAR(50) NULL COMMENT 'Stripe, PayPal, Razorpay, etc.',
    transaction_id VARCHAR(100) NULL COMMENT 'External payment gateway transaction ID',
    amount DECIMAL(10, 2) NOT NULL,
    currency VARCHAR(3) NOT NULL DEFAULT 'INR',
    payment_status ENUM('PENDING', 'CAPTURED', 'FAILED', 'REFUNDED', 'PARTIALLY_REFUNDED') NOT NULL DEFAULT 'PENDING',
    payment_date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT pk_payments PRIMARY KEY (id),
    CONSTRAINT uq_payments_uuid UNIQUE (payments_uuid),
    CONSTRAINT fk_payments_booking FOREIGN KEY (booking_id)
        REFERENCES bookings(id) ON DELETE RESTRICT ON UPDATE CASCADE,
    INDEX idx_payments_booking (booking_id, payment_status, is_deleted),
    INDEX idx_payments_transaction (transaction_id, is_deleted),
    INDEX idx_payments_date (payment_date, is_deleted)
);

-- Refunds Table (Tracks refund transactions)
CREATE TABLE refunds (
    id BIGINT UNSIGNED AUTO_INCREMENT,
    refunds_uuid VARCHAR(36) NOT NULL,
    payment_id BIGINT UNSIGNED NOT NULL,
    booking_id BIGINT UNSIGNED NOT NULL,
    refund_amount DECIMAL(10, 2) NOT NULL,
    cancellation_fee DECIMAL(10, 2) NOT NULL DEFAULT 0.00,
    refundable_amount DECIMAL(10, 2) NOT NULL COMMENT 'Amount after deducting fees',
    refund_reason TEXT NULL,
    refund_status ENUM('PENDING', 'PROCESSING', 'COMPLETED', 'FAILED') NOT NULL DEFAULT 'PENDING',
    refund_transaction_id VARCHAR(100) NULL,
    processed_at DATETIME NULL,
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT pk_refunds PRIMARY KEY (id),
    CONSTRAINT uq_refunds_uuid UNIQUE (refunds_uuid),
    CONSTRAINT fk_refunds_payment FOREIGN KEY (payment_id)
        REFERENCES payments(id) ON DELETE RESTRICT ON UPDATE CASCADE,
    CONSTRAINT fk_refunds_booking FOREIGN KEY (booking_id)
        REFERENCES bookings(id) ON DELETE RESTRICT ON UPDATE CASCADE,
    INDEX idx_refunds_payment (payment_id, is_deleted),
    INDEX idx_refunds_booking (booking_id, refund_status, is_deleted)
);

-- =====================================================
-- SEAT MANAGEMENT
-- =====================================================

-- Seat Map Template (Defines seat layout for aircraft type)
CREATE TABLE seat_map_templates (
    id BIGINT UNSIGNED AUTO_INCREMENT,
    seat_map_templates_uuid VARCHAR(36) NOT NULL,
    aircraft_type_id BIGINT UNSIGNED NOT NULL,
    seat_row TINYINT UNSIGNED NOT NULL,
    seat_column CHAR(1) NOT NULL COMMENT 'A, B, C, D, E, F, etc.',
    seat_class ENUM('ECONOMY', 'BUSINESS', 'FIRST') NOT NULL,
    is_aisle BOOLEAN NOT NULL DEFAULT FALSE,
    is_window BOOLEAN NOT NULL DEFAULT FALSE,
    is_exit_row BOOLEAN NOT NULL DEFAULT FALSE,
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT pk_seat_map_templates PRIMARY KEY (id),
    CONSTRAINT uq_seat_map_templates_uuid UNIQUE (seat_map_templates_uuid),
    CONSTRAINT uq_seat_map_templates_seat UNIQUE (aircraft_type_id, seat_row, seat_column),
    CONSTRAINT fk_seat_map_templates_aircraft FOREIGN KEY (aircraft_type_id)
        REFERENCES aircraft_types(id) ON DELETE CASCADE ON UPDATE CASCADE,
    INDEX idx_seat_map_templates_aircraft (aircraft_type_id, seat_class, is_deleted)
);

-- Seat Assignments (Only stores when seats are actively selected)
CREATE TABLE seat_assignments (
    id BIGINT UNSIGNED AUTO_INCREMENT,
    seat_assignments_uuid VARCHAR(36) NOT NULL,
    ticket_id BIGINT UNSIGNED NOT NULL,
    flight_id BIGINT UNSIGNED NOT NULL,
    seat_map_template_id BIGINT UNSIGNED NOT NULL,
    assignment_type ENUM('PREBOOKED', 'CHECKED_IN', 'AUTO_ASSIGNED') NOT NULL,
    assigned_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT pk_seat_assignments PRIMARY KEY (id),
    CONSTRAINT uq_seat_assignments_uuid UNIQUE (seat_assignments_uuid),
    CONSTRAINT uq_seat_assignments_ticket_flight UNIQUE (ticket_id, flight_id),
    CONSTRAINT uq_seat_assignments_seat_flight UNIQUE (flight_id, seat_map_template_id),
    CONSTRAINT fk_seat_assignments_ticket FOREIGN KEY (ticket_id)
        REFERENCES tickets(id) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_seat_assignments_flight FOREIGN KEY (flight_id)
        REFERENCES flights(id) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_seat_assignments_seat_template FOREIGN KEY (seat_map_template_id)
        REFERENCES seat_map_templates(id) ON DELETE RESTRICT ON UPDATE CASCADE,
    INDEX idx_seat_assignments_seat (seat_map_template_id, is_deleted),
    INDEX idx_seat_assignments_flight_seat (flight_id, seat_map_template_id) -- Important for availability checks
);

-- =====================================================
-- ANCILLARY SERVICES
-- =====================================================

-- Service Catalog (Meals, baggage, etc.)
CREATE TABLE service_catalog (
    id BIGINT UNSIGNED AUTO_INCREMENT,
    service_catalog_uuid VARCHAR(36) NOT NULL,
    service_code VARCHAR(20) NOT NULL,
    service_name VARCHAR(100) NOT NULL,
    service_type ENUM('MEAL', 'BAGGAGE', 'LOUNGE', 'PRIORITY_BOARDING', 'WIFI', 'OTHER') NOT NULL,
    description TEXT NULL,
    price DECIMAL(10, 2) NOT NULL,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT pk_service_catalog PRIMARY KEY (id),
    CONSTRAINT uq_service_catalog_uuid UNIQUE (service_catalog_uuid),
    CONSTRAINT uq_service_catalog_code UNIQUE (service_code),
    INDEX idx_service_catalog_type (service_type, is_active, is_deleted)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Flight Services (Available services per flight)
CREATE TABLE flight_services (
    id BIGINT UNSIGNED AUTO_INCREMENT,
    flight_services_uuid VARCHAR(36) NOT NULL,
    flight_id BIGINT UNSIGNED NOT NULL,
    service_catalog_id BIGINT UNSIGNED NOT NULL,
    available_quantity SMALLINT UNSIGNED NULL COMMENT 'NULL = unlimited',
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT pk_flight_services PRIMARY KEY (id),
    CONSTRAINT uq_flight_services_uuid UNIQUE (flight_services_uuid),
    CONSTRAINT uq_flight_services_combo UNIQUE (flight_id, service_catalog_id),
    CONSTRAINT fk_flight_services_flight FOREIGN KEY (flight_id)
        REFERENCES flights(id) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_flight_services_catalog FOREIGN KEY (service_catalog_id)
        REFERENCES service_catalog(id) ON DELETE CASCADE ON UPDATE CASCADE,
    INDEX idx_flight_services_flight (flight_id, is_deleted)
);

-- Passenger Services (Pre-booked services)
CREATE TABLE passenger_services (
    id BIGINT UNSIGNED AUTO_INCREMENT,
    passenger_services_uuid VARCHAR(36) NOT NULL,
    ticket_id BIGINT UNSIGNED NOT NULL,
    flight_service_id BIGINT UNSIGNED NOT NULL,
    quantity TINYINT UNSIGNED NOT NULL DEFAULT 1,
    price_paid DECIMAL(10, 2) NOT NULL,
    service_status ENUM('BOOKED', 'CONFIRMED', 'DELIVERED', 'CANCELLED') NOT NULL DEFAULT 'BOOKED',
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT pk_passenger_services PRIMARY KEY (id),
    CONSTRAINT uq_passenger_services_uuid UNIQUE (passenger_services_uuid),
    CONSTRAINT fk_passenger_services_ticket FOREIGN KEY (ticket_id)
        REFERENCES tickets(id) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_passenger_services_flight_service FOREIGN KEY (flight_service_id)
        REFERENCES flight_services(id) ON DELETE RESTRICT ON UPDATE CASCADE,
    INDEX idx_passenger_services_ticket (ticket_id, is_deleted)
);

-- =====================================================
-- CHECK-IN SYSTEM
-- =====================================================

-- Check-in Records
CREATE TABLE check_ins (
    id BIGINT UNSIGNED AUTO_INCREMENT,
    check_ins_uuid VARCHAR(36) NOT NULL,
    ticket_id BIGINT UNSIGNED NOT NULL,
    check_in_type ENUM('ONLINE', 'KIOSK', 'COUNTER', 'MOBILE') NOT NULL,
    check_in_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    boarding_pass_number VARCHAR(20) NOT NULL,
    boarding_gate VARCHAR(10) NULL,
    boarding_time DATETIME NULL,
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT pk_check_ins PRIMARY KEY (id),
    CONSTRAINT uq_check_ins_uuid UNIQUE (check_ins_uuid),
    CONSTRAINT uq_check_ins_ticket UNIQUE (ticket_id),
    CONSTRAINT uq_check_ins_boarding_pass UNIQUE (boarding_pass_number),
    CONSTRAINT fk_check_ins_ticket FOREIGN KEY (ticket_id)
        REFERENCES tickets(id) ON DELETE CASCADE ON UPDATE CASCADE,
    INDEX idx_check_ins_time (check_in_time, is_deleted)
);
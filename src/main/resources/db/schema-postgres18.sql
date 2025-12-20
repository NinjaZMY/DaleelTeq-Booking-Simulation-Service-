-- ============================================================================
-- PostgreSQL 18 DDL Script for DaleelTeq Booking Service
-- ============================================================================
-- This script creates all necessary tables and constraints for the booking system
-- Run this after creating the database and user (see setup instructions below)

-- ============================================================================
-- SETUP INSTRUCTIONS (Run BEFORE executing this script):
-- ============================================================================
-- 1. Connect to PostgreSQL as superuser (usually 'postgres'):
--    psql -U postgres
--
-- 2. Create the database:
--    CREATE DATABASE booking_db;
--
-- 3. Create the user (if not already created):
--    CREATE USER booking_user WITH PASSWORD 'changeme';
--
-- 4. Grant privileges:
--    GRANT ALL PRIVILEGES ON DATABASE booking_db TO booking_user;
--    ALTER DATABASE booking_db OWNER TO booking_user;
--
-- 5. Connect to the database as the new user:
--    psql -U booking_user -d booking_db
--
-- 6. Run this script:
--    \i path/to/this/schema-postgres18.sql
--
-- ============================================================================

-- Switch to the booking database (if not already connected)
-- \c booking_db booking_user

-- ============================================================================
-- CREATE TABLES
-- ============================================================================

-- Table: services (S)
-- Description: Represents available services with base duration
CREATE TABLE IF NOT EXISTS services (
    id BIGSERIAL PRIMARY KEY,
    lib VARCHAR(255) NOT NULL,
    time_value INTEGER NOT NULL CHECK (time_value IN (15, 20, 25, 30)),
    version BIGINT DEFAULT 0,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- Table: employees (E)
-- Description: Represents employees who provide services
CREATE TABLE IF NOT EXISTS employees (
    id BIGSERIAL PRIMARY KEY,
    lib VARCHAR(255) NOT NULL,
    version BIGINT DEFAULT 0,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- Table: clients (C)
-- Description: Represents clients who can book services
CREATE TABLE IF NOT EXISTS clients (
    id BIGSERIAL PRIMARY KEY,
    lib VARCHAR(255) NOT NULL,
    number VARCHAR(20) NOT NULL,
    version BIGINT DEFAULT 0,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- Table: employee_services (ES)
-- Description: Represents available timeslots for employee-service combinations
-- Each row represents when an employee can provide a specific service
CREATE TABLE IF NOT EXISTS employee_services (
    id BIGSERIAL PRIMARY KEY,
    id_e BIGINT NOT NULL REFERENCES employees(id) ON DELETE RESTRICT,
    id_s BIGINT NOT NULL REFERENCES services(id) ON DELETE RESTRICT,
    x2 BOOLEAN NOT NULL DEFAULT FALSE,
    start TIME NOT NULL,
    end TIME NOT NULL,
    date DATE,
    status VARCHAR(50) NOT NULL CHECK (status IN ('free', 'taken')) DEFAULT 'free',
    time_value INTEGER NOT NULL,
    version BIGINT DEFAULT 0,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT chk_time_order CHECK (start < end)
);

-- Table: rendez_vous (R)
-- Description: Represents actual bookings/reservations made by clients
-- A rendez_vous links a client to an available employee service slot
CREATE TABLE IF NOT EXISTS rendez_vous (
    id BIGSERIAL PRIMARY KEY,
    id_es BIGINT NOT NULL REFERENCES employee_services(id) ON DELETE RESTRICT,
    id_c BIGINT NOT NULL REFERENCES clients(id) ON DELETE RESTRICT,
    status VARCHAR(100) NOT NULL CHECK (status IN ('Active', 'Cancelled by Client', 'Cancelled by Employee')) DEFAULT 'Active',
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    cancelled_at TIMESTAMP WITH TIME ZONE,
    version BIGINT DEFAULT 0,
    CONSTRAINT chk_cancellation_date CHECK (cancelled_at IS NULL OR cancelled_at >= created_at)
);

-- Table: notifications (N)
-- Description: Represents notification events triggered by booking/cancellation
-- Created automatically when a rendez_vous is booked or cancelled
CREATE TABLE IF NOT EXISTS notifications (
    id BIGSERIAL PRIMARY KEY,
    id_r BIGINT NOT NULL REFERENCES rendez_vous(id) ON DELETE CASCADE,
    type VARCHAR(100) NOT NULL CHECK (type IN ('booked', 'cancelled')),
    value TEXT NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    version BIGINT DEFAULT 0
);

-- ============================================================================
-- CREATE INDEXES
-- ============================================================================

-- Indexes for better query performance
CREATE INDEX IF NOT EXISTS idx_employee_services_status ON employee_services(status);
CREATE INDEX IF NOT EXISTS idx_employee_services_date ON employee_services(date);
CREATE INDEX IF NOT EXISTS idx_employee_services_employee ON employee_services(id_e);
CREATE INDEX IF NOT EXISTS idx_employee_services_service ON employee_services(id_s);

CREATE INDEX IF NOT EXISTS idx_rendez_vous_status ON rendez_vous(status);
CREATE INDEX IF NOT EXISTS idx_rendez_vous_client ON rendez_vous(id_c);
CREATE INDEX IF NOT EXISTS idx_rendez_vous_employee_service ON rendez_vous(id_es);
CREATE INDEX IF NOT EXISTS idx_rendez_vous_created ON rendez_vous(created_at);

CREATE INDEX IF NOT EXISTS idx_notifications_rendez_vous ON notifications(id_r);
CREATE INDEX IF NOT EXISTS idx_notifications_type ON notifications(type);
CREATE INDEX IF NOT EXISTS idx_notifications_created ON notifications(created_at);

-- ============================================================================
-- OPTIONAL: SAMPLE DATA (Comment out if not needed)
-- ============================================================================

-- Insert sample services
INSERT INTO services (lib, time_value) VALUES
    ('Haircut', 30),
    ('Hair Coloring', 60),
    ('Manicure', 20),
    ('Pedicure', 25),
    ('Facial', 45),
    ('Massage', 50)
ON CONFLICT DO NOTHING;

-- Insert sample employees
INSERT INTO employees (lib) VALUES
    ('John Doe'),
    ('Jane Smith'),
    ('Mike Johnson'),
    ('Sarah Williams'),
    ('Tom Brown')
ON CONFLICT DO NOTHING;

-- Insert sample clients
INSERT INTO clients (lib, number) VALUES
    ('Alice Cooper', '+1234567890'),
    ('Bob Martin', '+0987654321'),
    ('Carol White', '+1122334455'),
    ('David Lee', '+5566778899'),
    ('Emma Taylor', '+9988776655')
ON CONFLICT DO NOTHING;

-- ============================================================================
-- VERIFICATION QUERIES
-- ============================================================================
-- Run these to verify the setup:

-- Count records in each table:
SELECT 'services' as table_name, COUNT(*) as count FROM services
UNION ALL
SELECT 'employees', COUNT(*) FROM employees
UNION ALL
SELECT 'clients', COUNT(*) FROM clients
UNION ALL
SELECT 'employee_services', COUNT(*) FROM employee_services
UNION ALL
SELECT 'rendez_vous', COUNT(*) FROM rendez_vous
UNION ALL
SELECT 'notifications', COUNT(*) FROM notifications;

-- Display all services:
-- SELECT * FROM services;

-- Display all employees:
-- SELECT * FROM employees;

-- Display all clients:
-- SELECT * FROM clients;

-- ============================================================================
-- END OF SCHEMA SCRIPT
-- ============================================================================


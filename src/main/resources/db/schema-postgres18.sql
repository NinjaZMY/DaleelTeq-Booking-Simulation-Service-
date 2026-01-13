-- PostgreSQL 18 Schema for DaleelTeq Booking Simulation Service
-- Database: booking_db
-- User: booking_user

-- Drop tables if they exist (for clean slate)
DROP TABLE IF EXISTS es_notification CASCADE;
DROP TABLE IF EXISTS rendez_vous CASCADE;
DROP TABLE IF EXISTS employee_x_services CASCADE;
DROP TABLE IF EXISTS services CASCADE;
DROP TABLE IF EXISTS employees CASCADE;
DROP TABLE IF EXISTS clients CASCADE;

-- Services Table (S)
-- Defines available appointment types with base duration
CREATE TABLE services (
    id BIGSERIAL PRIMARY KEY,
    lib VARCHAR(255) NOT NULL UNIQUE,
    time_value INT NOT NULL CHECK (time_value IN (15, 20, 25, 30)),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Employees Table (E)
-- Defines employees who provide services
CREATE TABLE employees (
    id BIGSERIAL PRIMARY KEY,
    lib VARCHAR(255) NOT NULL UNIQUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Clients Table (C)
-- Defines clients who book appointments
CREATE TABLE clients (
    id BIGSERIAL PRIMARY KEY,
    lib VARCHAR(255) NOT NULL,
    number VARCHAR(20) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Employee_x_Services Table (ES)
-- Represents available timeslots for employee-service combinations
-- Each row is a timeslot that can be booked or remains free
CREATE TABLE employee_x_services (
    id BIGSERIAL PRIMARY KEY,
    id_e BIGINT NOT NULL REFERENCES employees(id) ON DELETE CASCADE,
    id_s BIGINT NOT NULL REFERENCES services(id) ON DELETE CASCADE,
    x_2 BOOLEAN NOT NULL DEFAULT FALSE,
    date DATE NOT NULL,
    start TIME NOT NULL,
    end TIME NOT NULL,
    time_value INT NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'free' CHECK (status IN ('free', 'taken')),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create indexes on ES for performance
CREATE INDEX idx_es_date ON employee_x_services(date);
CREATE INDEX idx_es_status ON employee_x_services(status);
CREATE INDEX idx_es_employee ON employee_x_services(id_e);
CREATE INDEX idx_es_service ON employee_x_services(id_s);

-- Rendez_vous Table (R)
-- Represents actual bookings linking a client to a timeslot
-- Created when a free ES is booked by a client
CREATE TABLE rendez_vous (
    id BIGSERIAL PRIMARY KEY,
    id_es BIGINT NOT NULL REFERENCES employee_x_services(id) ON DELETE CASCADE,
    id_c BIGINT NOT NULL REFERENCES clients(id) ON DELETE CASCADE,
    status VARCHAR(50) NOT NULL DEFAULT 'Active' CHECK (status IN ('Active', 'Cancelled by Client', 'Cancelled by Employee')),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create indexes on Rendez_vous for performance
CREATE INDEX idx_rendez_vous_es ON rendez_vous(id_es);
CREATE INDEX idx_rendez_vous_client ON rendez_vous(id_c);
CREATE INDEX idx_rendez_vous_status ON rendez_vous(status);

-- Notification Table (N)
-- Audit trail for booking/cancellation events
-- Stores snapshots of x_2 and time_value at the time of notification
CREATE TABLE es_notification (
    id BIGSERIAL PRIMARY KEY,
    id_r BIGINT NOT NULL REFERENCES rendez_vous(id) ON DELETE CASCADE,
    type VARCHAR(20) NOT NULL CHECK (type IN ('booked', 'cancelled')),
    value VARCHAR(50) NOT NULL,
    x_2 BOOLEAN NOT NULL,
    time_value INT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create index on Notification for performance
CREATE INDEX idx_notification_rendez_vous ON es_notification(id_r);
CREATE INDEX idx_notification_type ON es_notification(type);

-- Sample Data Insertion (optional, for testing)
-- Insert sample services
INSERT INTO services (lib, time_value) VALUES
    ('Haircut', 15),
    ('Styling', 20),
    ('Coloring', 25),
    ('Treatment', 30)
ON CONFLICT (lib) DO NOTHING;

-- Insert sample employees
INSERT INTO employees (lib) VALUES
    ('John Doe'),
    ('Jane Smith'),
    ('Mike Johnson')
ON CONFLICT (lib) DO NOTHING;

-- Insert sample clients
INSERT INTO clients (lib, number) VALUES
    ('C1', '+20123456789'),
    ('C2', '+20987654321'),
    ('C3', '+20555666777')
ON CONFLICT DO NOTHING;

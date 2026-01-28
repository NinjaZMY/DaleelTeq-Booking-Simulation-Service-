-- PostgreSQL 18 - Fix booking_user permissions
-- Run this as postgres superuser: psql -U postgres -d booking_db -f setup-db-permissions.sql

-- Grant schema permissions
GRANT USAGE ON SCHEMA public TO booking_user;
GRANT CREATE ON SCHEMA public TO booking_user;

-- Grant default privileges for future objects
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT ALL ON TABLES TO booking_user;
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT ALL ON SEQUENCES TO booking_user;

-- Grant permissions on existing public schema objects
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO booking_user;
GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA public TO booking_user;

\echo 'Permissions granted successfully to booking_user'


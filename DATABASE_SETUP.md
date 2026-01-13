# PostgreSQL 18 Database Setup - Copy & Paste Commands

## Windows Command Prompt

```cmd
REM Step 1: Connect to PostgreSQL as superuser
psql -U postgres

REM In psql, paste this SQL:
CREATE DATABASE booking_db;
CREATE USER booking_user WITH PASSWORD 'changeme';
GRANT ALL PRIVILEGES ON DATABASE booking_db TO booking_user;
\q

REM Step 2: Load the schema (from project directory)
psql -U booking_user -d booking_db -f src/main/resources/db/schema-postgres18.sql

REM Step 3: Verify (optional)
psql -U booking_user -d booking_db -c "SELECT COUNT(*) as services FROM services;"
```

## Windows PowerShell (as Admin)

```powershell
# Step 1: Start PostgreSQL service if not running
net start PostgreSQL

# Step 2: Connect and create database
psql -U postgres

# In psql, paste this SQL:
# CREATE DATABASE booking_db;
# CREATE USER booking_user WITH PASSWORD 'changeme';
# GRANT ALL PRIVILEGES ON DATABASE booking_db TO booking_user;
# \q

# Step 3: Load schema
psql -U booking_user -d booking_db -f src/main/resources/db/schema-postgres18.sql

# Step 4: Verify
psql -U booking_user -d booking_db -c "SELECT COUNT(*) as total FROM services;"
```

## macOS / Linux

```bash
# Step 1: Start PostgreSQL (if using Homebrew on Mac)
brew services start postgresql@18

# Step 2: Connect as superuser
psql -U postgres

# In psql, paste this SQL:
# CREATE DATABASE booking_db;
# CREATE USER booking_user WITH PASSWORD 'changeme';
# GRANT ALL PRIVILEGES ON DATABASE booking_db TO booking_user;
# \q

# Step 3: Load schema
psql -U booking_user -d booking_db -f src/main/resources/db/schema-postgres18.sql

# Step 4: Verify
psql -U booking_user -d booking_db -c "SELECT COUNT(*) FROM services;"
```

## Troubleshooting

### "psql: command not found"
- Add PostgreSQL to PATH
- **Windows:** Add `C:\Program Files\PostgreSQL\18\bin` to System PATH
- **macOS:** `which psql` (should show path if installed)
- **Linux:** `sudo apt install postgresql-client-18`

### "FATAL: Ident authentication failed"
- Make sure you're using correct credentials
- Default superuser is usually `postgres` with password set during installation
- Try: `psql -U postgres -W` (will prompt for password)

### "database booking_db already exists"
- Your database is already created (skip to Step 3)

### "role booking_user already exists"
- Drop and recreate:
  ```sql
  DROP USER IF EXISTS booking_user;
  CREATE USER booking_user WITH PASSWORD 'changeme';
  ```

## Verify Setup

After schema loads, you should see:

```sql
-- Check tables exist
\dt

-- Should show 6 tables:
-- es_notification
-- employee_x_services  
-- employees
-- clients
-- rendez_vous
-- services

-- Check sample data
SELECT COUNT(*) FROM services;     -- Should return: 4
SELECT COUNT(*) FROM employees;    -- Should return: 3
SELECT COUNT(*) FROM clients;      -- Should return: 3
```

## Default Connection Info

When all setup is complete, use these credentials in `.env` or `application.properties`:

```properties
SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/booking_db
DB_USERNAME=booking_user
DB_PASSWORD=changeme
```

These are already in the `.env` file provided!

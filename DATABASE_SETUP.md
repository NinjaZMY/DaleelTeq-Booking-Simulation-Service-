# PostgreSQL 18 Database Setup

## Prerequisites
- PostgreSQL 18 installed and running
- Database `booking_db` and user `booking_user` created with password `changeme`

## Setup Steps

### Step 1: Grant Permissions (as postgres superuser)
```bash
psql -U postgres -d booking_db -f setup-db-permissions.sql
```

### Step 2: Create Schema (as booking_user)
```bash
psql -U booking_user -d booking_db -f src/main/resources/db/schema-postgres18.sql
```

### Step 3: Verify Tables Created
```bash
psql -U booking_user -d booking_db -c "\dt"
```

Expected output:
```
         List of relations
 Schema |           Name            | Type  |     Owner
--------+---------------------------+-------+-----------
 public | clients                   | table | booking_user
 public | employee_x_services       | table | booking_user
 public | employees                 | table | booking_user
 public | es_notification           | table | booking_user
 public | rendez_vous               | table | booking_user
 public | services                  | table | booking_user
(6 rows)
```

### Step 4: Verify Sample Data
```bash
psql -U booking_user -d booking_db -c "SELECT COUNT(*) FROM services;"
```

Expected: **4** services

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

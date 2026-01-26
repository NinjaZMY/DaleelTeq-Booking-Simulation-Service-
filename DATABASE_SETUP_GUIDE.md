# Database Setup Guide - PostgreSQL 18

## Problem Summary
- **Issue 1**: Reserved keyword `end` caused syntax error (FIXED ✓ in schema-postgres18.sql)
- **Issue 2**: `booking_user` lacks permissions to create tables in public schema

## Solution Steps

### Step 1: Grant Permissions (as postgres superuser)
```bash
# Connect as postgres superuser
psql -U postgres -d booking_db -f setup-db-permissions.sql
```

### Step 2: Create Tables (as booking_user)
After permissions are granted, run:
```bash
# Create all tables with sample data
psql -U booking_user -d booking_db -f src/main/resources/db/schema-postgres18.sql
```

## What Was Fixed

### 1. SQL Reserved Keyword Issue
**Before**:
```sql
start TIME NOT NULL,
end TIME NOT NULL,
```

**After**:
```sql
start_time TIME NOT NULL,
end_time TIME NOT NULL,
```

PostgreSQL has reserved keywords. Using `start` and `end` directly caused syntax errors. The schema file now uses `start_time` and `end_time`.

### 2. Permission Error
The error `ERROR: permission denied for schema public` occurs because the `booking_user` role doesn't have CREATE permission on the public schema.

**Solution**: Grant permissions to booking_user using the `setup-db-permissions.sql` file as the postgres superuser.

## Complete Database Setup Workflow

```bash
# 1. Connect as postgres superuser and grant permissions
psql -U postgres -d booking_db -f setup-db-permissions.sql

# 2. Connect as booking_user and create schema
psql -U booking_user -d booking_db -f src/main/resources/db/schema-postgres18.sql

# 3. Verify tables were created
psql -U booking_user -d booking_db -c "\dt"
```

## Expected Output After Step 2
```
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

## Troubleshooting

### Still getting "permission denied"?
Make sure you ran `setup-db-permissions.sql` **first** as postgres superuser:
```bash
psql -U postgres -d booking_db -f setup-db-permissions.sql
```

### Verify booking_user permissions:
```bash
psql -U postgres -d booking_db -c "SELECT grantee, privilege_type FROM role_privilege_grant WHERE table_schema='public' AND grantee='booking_user';"
```

### Reset and start over:
```bash
# As postgres superuser
psql -U postgres
DROP DATABASE IF EXISTS booking_db;
CREATE DATABASE booking_db;
CREATE USER booking_user WITH PASSWORD 'changeme';
GRANT ALL PRIVILEGES ON DATABASE booking_db TO booking_user;
\q

# Then follow the workflow above
```


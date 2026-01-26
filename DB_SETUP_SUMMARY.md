# Database Setup - Quick Reference

## Two Issues Found & Fixed

### ✅ Issue 1: Reserved Keyword (FIXED)
**Error**: `ERROR: syntax error at or near "end"`

**Fix**: Changed column names in `employee_x_services` table:
- `start` → `start_time`
- `end` → `end_time`

**File Updated**: `src/main/resources/db/schema-postgres18.sql`

---

### ⚠️ Issue 2: Permission Denied (NEEDS YOUR ACTION)
**Error**: `ERROR: permission denied for schema public`

**Cause**: `booking_user` role lacks permissions to create tables

**Fix**: Execute these commands in order:

#### Step 1: Grant Permissions (as postgres superuser)
```bash
psql -U postgres -d booking_db -f setup-db-permissions.sql
```

#### Step 2: Create Tables (as booking_user)
```bash
psql -U booking_user -d booking_db -f src/main/resources/db/schema-postgres18.sql
```

#### Step 3: Verify (optional)
```bash
psql -U booking_user -d booking_db -c "\dt"
```

Should show 6 tables: clients, employee_x_services, employees, es_notification, rendez_vous, services

---

## Files Created
- `setup-db-permissions.sql` - Permission setup script
- `DATABASE_SETUP_GUIDE.md` - Detailed troubleshooting guide

## Next Steps
1. Run the commands above in order
2. Confirm all tables are created
3. Start the Spring Boot application with the .env file configured


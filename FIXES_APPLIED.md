# Fixed Issues Summary

## Issue 1: ✅ Fixed - Database Reserved Keywords
**Problem**: Columns `start` and `end` are PostgreSQL reserved keywords

**Solution Applied**:
1. Updated database schema: `start` → `start_time`, `end` → `end_time`
2. Updated Java Entity (`EmployeeXService.java`):
   - `private LocalTime start;` → `private LocalTime startTime;` with `@Column(name = "start_time")`
   - `private LocalTime end;` → `private LocalTime endTime;` with `@Column(name = "end_time")`
3. Updated DTO (`EmployeeXServiceDto.java`):
   - `private LocalTime start;` → `private LocalTime startTime;`
   - `private LocalTime end;` → `private LocalTime endTime;`

**Files Modified**:
- `src/main/resources/db/schema-postgres18.sql`
- `src/main/java/com/daleelteq/booking/domain/EmployeeXService.java`
- `src/main/java/com/daleelteq/booking/dto/EmployeeXServiceDto.java`

---

## Issue 2: ✅ Fixed - Invalid SQL Syntax in Permissions Script
**Problem**: `ALTER DEFAULT PRIVILEGES ... GRANT ALL ON INDEXES` is invalid PostgreSQL syntax
- PostgreSQL doesn't support `ON INDEXES` in ALTER DEFAULT PRIVILEGES statements

**Solution Applied**:
- Removed the invalid line: `ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT ALL ON INDEXES TO booking_user;`
- Kept only valid statements for TABLES and SEQUENCES

**File Modified**:
- `setup-db-permissions.sql`

---

## Next Steps

### 1. Grant Permissions (run as postgres superuser)
```bash
psql -U postgres -d booking_db -f setup-db-permissions.sql
```

### 2. Create Database Schema (run as booking_user)
```bash
psql -U booking_user -d booking_db -f src/main/resources/db/schema-postgres18.sql
```

### 3. Verify Tables Created
```bash
psql -U booking_user -d booking_db -c "\dt"
```

Should show all 6 tables: clients, employee_x_services, employees, es_notification, rendez_vous, services

---

## Backend Changes Summary

All Java code now aligns with database column names:
- Entity uses `@Column(name = "start_time")` and `@Column(name = "end_time")` for proper JPA mapping
- DTO fields use camelCase: `startTime`, `endTime`
- No API changes needed - the mapping handles the translation automatically

Your Postman tests will still use JSON keys like:
```json
{
  "startTime": "09:00",
  "endTime": "10:00",
  ...
}
```


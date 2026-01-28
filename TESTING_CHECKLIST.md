# ✅ Database Configuration & Testing Checklist

## Phase 1: Database Setup ✅

- [ ] PostgreSQL 18 is installed and running
- [ ] Database `booking_db` exists
- [ ] User `booking_user` created with password `changeme`
- [ ] Run: `psql -U postgres -d booking_db -f setup-db-permissions.sql`
- [ ] Run: `psql -U booking_user -d booking_db -f src/main/resources/db/schema-postgres18.sql`
- [ ] Verify: `psql -U booking_user -d booking_db -c "\dt"` (should show 6 tables)

## Phase 2: Application Startup ✅

- [ ] Compile: `mvn clean compile` (should succeed)
- [ ] Start app: `mvn spring-boot:run`
- [ ] Check console for "Started BookingApplication"
- [ ] Visit: `http://localhost:8080`
- [ ] Dashboard loads with "Refresh Data" button
- [ ] All entity sections visible (Services, Employees, Clients, Timeslots, etc.)

## Phase 3: Database Connection ✅

- [ ] Click "Refresh Data" button on dashboard
- [ ] Should display:
  - Services: 4
  - Employees: 3
  - Clients: 3
- [ ] No error messages in browser or console

## Phase 4: CRUD Testing - Create Timeslot

- [ ] Open Timeslots section
- [ ] Fill in form:
  - Employee ID: 1
  - Service ID: 1
  - Date: 2026-02-20
  - Start Time: 10:00
  - X2: unchecked
- [ ] Click Create
- [ ] Timeslot appears in list with correct times

## Phase 5: Error Message Testing

- [ ] Try to create timeslot with invalid time (e.g., 18:00)
- [ ] Error message should show: "Valid time for example from 0800 to 1700"
- [ ] Try invalid date format
- [ ] Error message should specify correct format
- [ ] Try invalid start/end times
- [ ] Each error message should be precise (not generic)

## Phase 6: Postman Testing

See TESTING_GUIDE.md for complete Postman examples

### Basic Tests
- [ ] GET /api/es (list all timeslots)
- [ ] GET /api/es/1 (get specific timeslot)
- [ ] POST /api/es (create timeslot)
- [ ] PUT /api/es/1 (update timeslot)
- [ ] DELETE /api/es/1 (delete timeslot)

### Field Verification in Postman
- [ ] Response includes `startTime` and `endTime` (camelCase)
- [ ] Response includes `x2`, `date`, `idE`, `idS`, `status`
- [ ] Times display as "HH:MM" format

## Notes

- **Database schema changed**: `start` → `start_time`, `end` → `end_time`
- **Java fields changed**: `start` → `startTime`, `end` → `endTime`
- **JSON API unchanged**: Still uses `startTime` and `endTime` (automatic mapping)
- **All files synchronized**: Entity, DTO, Service, Controller all updated
- **Build verified**: `mvn clean compile` passes successfully

## Troubleshooting Commands

```bash
# Check if database is running
psql -U postgres -c "SELECT version();"

# Check if booking_db exists
psql -U postgres -c "\l" | grep booking_db

# Check if tables were created
psql -U booking_user -d booking_db -c "\dt"

# Check if sample data exists
psql -U booking_user -d booking_db -c "SELECT COUNT(*) FROM services;"

# Run app in debug mode
mvn -X spring-boot:run

# Check Java version
java -version
```

## Quick Links

- **COMPREHENSIVE_FIX_SUMMARY.md** - What was fixed and why
- **QUICKSTART.md** - Quick setup guide
- **DATABASE_SETUP.md** - Database setup details
- **TESTING_GUIDE.md** - Full API testing guide
- **FIELD_NAME_FIX_REPORT.md** - Detailed change log


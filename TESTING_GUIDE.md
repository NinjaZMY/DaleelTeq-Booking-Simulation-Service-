# Testing Guide - DaleelTeq Booking Simulation Service

**Goal**: Test the application incrementally - first without database, then with database configuration.

## Phase 1: Run Without Database (Quick Check for Obvious Errors)

### Prerequisites
- Java 25 installed
- Maven installed locally
- IntelliJ IDEA with Maven support

### Start the Application

```bash
mvn spring-boot:run
```

**Expected Output**:
```
...
[main] c.d.b.BookingApplication : Starting BookingApplication
[main] c.d.b.BookingApplication : Application started successfully
[main] t.m.s.TomcatWebServer : Tomcat started on port(s): 8080
```

### What Happens Without Database?
- ✅ Application starts on `http://localhost:8080`
- ✅ Web UI loads at `http://localhost:8080/`
- ✅ API endpoints respond with connection errors (expected, not database-related)
- ❌ Data operations fail due to missing database

### Check for Configuration Errors

Visit `http://localhost:8080` in your browser:
- **UI loads?** → Configuration is correct
- **UI doesn't load?** → Check Thymeleaf/Spring Web configuration
- **Port 8080 already in use?** → Change in `application.properties`: `server.port=8081`

### Troubleshoot Common Errors

| Error | Solution |
|-------|----------|
| `Cannot resolve symbol 'log'` | Ensure Lombok is processed (rebuild project) |
| `Port 8080 already in use` | Change `server.port` in `application.properties` |
| `ClassNotFoundException` | Run `mvn clean compile` then `mvn spring-boot:run` |
| `Thymeleaf template not found` | Check templates exist in `src/main/resources/templates/` |

### Stop the Application
Press `Ctrl+C` in the terminal.

---

## Phase 2: Configure Database (When Ready)

Once you've verified no obvious errors, configure the database:

### Step 1: Create PostgreSQL Database and User

```bash
psql -U postgres
```

Then execute:

```sql
CREATE DATABASE booking_db;
CREATE USER booking_user WITH PASSWORD 'changeme';
GRANT ALL PRIVILEGES ON DATABASE booking_db TO booking_user;
\q
```

### Step 2: Create `.env` File

Copy the example file:

```bash
cp .env.example .env
```

Edit `.env` with your credentials:

```env
DB_USERNAME=booking_user
DB_PASSWORD=changeme
```

### Step 3: Load Database Schema

```bash
psql -U booking_user -d booking_db -f src/main/resources/db/schema-postgres18.sql
```

Verify:

```bash
psql -U booking_user -d booking_db -c "SELECT COUNT(*) FROM services;"
```

Expected: **4** (sample services already inserted)

### Step 4: Update `application.properties`

Ensure this line exists (it should already):

```properties
spring.jpa.hibernate.ddl-auto=validate
```

This prevents Hibernate from modifying the database schema.

---

## Phase 3: Run with Database

### Restart the Application

```bash
mvn clean compile
mvn spring-boot:run
```

### Verify Database Connection

Open the Web UI: `http://localhost:8080`

You should see:
- List of services (4 items)
- List of employees (3 items)
- List of clients (3 items)
- Ability to create/update/delete records

### Test API Endpoints via Postman

Use the [API Endpoints](#api-endpoints) section below.

---

## API Endpoints Reference

### Services `/api/services`

**Get All Services**
```
GET http://localhost:8080/api/services
```

**Get Single Service**
```
GET http://localhost:8080/api/services/1
```

**Create Service**
```
POST http://localhost:8080/api/services
Content-Type: application/json

{
  "lib": "Consultation",
  "timeValue": 20
}
```

**Update Service (via ID in URL)**
```
PUT http://localhost:8080/api/services/1
Content-Type: application/json

{
  "lib": "Consultation Updated",
  "timeValue": 25
}
```

**Update Service (via body ID)**
```
PUT http://localhost:8080/api/services/entity
Content-Type: application/json

{
  "id": 1,
  "lib": "Consultation",
  "timeValue": 30
}
```

**Delete Service**
```
DELETE http://localhost:8080/api/services/1
```

**Delete All Services**
```
DELETE http://localhost:8080/api/services/clear
```

---

### Employees `/api/employees`

**Create Employee**
```
POST http://localhost:8080/api/employees
Content-Type: application/json

{
  "lib": "Dr. Ahmed"
}
```

**Get All / Get One / Update / Delete** - Same patterns as Services

---

### Clients `/api/clients`

**Create Client**
```
POST http://localhost:8080/api/clients
Content-Type: application/json

{
  "lib": "Mohamed",
  "number": "+20123456789"
}
```

---

### Timeslots (ES) `/api/es`

**Create Timeslot**
```
POST http://localhost:8080/api/es
Content-Type: application/json

{
  "idE": 1,
  "idS": 1,
  "date": "2026-02-20",
  "start": "09:00",
  "x2": false
}
```

**Note**: Server automatically calculates:
- `end` time = start + (serviceTimeValue * (x2 ? 2 : 1))
- `timeValue` = same calculation

**Error Examples** (test error handling):

Invalid time (too late):
```json
{
  "idE": 1,
  "idS": 1,
  "date": "2026-02-20",
  "start": "17:00",
  "x2": false
}
```

Response shows valid time range: "Valid times: 09:00 to 16:00"

---

### Rendez-vous (Bookings) `/api/rendezvous`

**Book a Timeslot**
```
POST http://localhost:8080/api/rendezvous
Content-Type: application/json

{
  "idES": 1,
  "idC": 1
}
```

**Cancel Booking**
```
PUT http://localhost:8080/api/rendezvous/1
Content-Type: application/json

{
  "status": "cancelled_by_client"
}
```

When cancelled, the timeslot automatically becomes "free" and can be booked again.

---

### Notifications `/api/notifications`

**Get All Notifications**
```
GET http://localhost:8080/api/notifications
```

Notifications are created automatically when:
- Booking is confirmed
- Booking is cancelled

---

## Environment Variables

See `.env.example` for reference. Current supported variables:

```env
# Database Connection
DB_USERNAME=booking_user
DB_PASSWORD=changeme

# Optional: Server Port (default: 8080)
SERVER_PORT=8080

# Optional: Time Window (default: 09:00-16:00)
TIME_WINDOW_START=09:00
TIME_WINDOW_END=16:00
```

**Note**: Only `DB_USERNAME` and `DB_PASSWORD` are required. Others have sensible defaults.

---

## Development Features

### Hot Reload

Changes to Java files automatically reload without restarting the server.

To enable/disable:
- Edit `src/main/resources/application.properties`
- Lines starting with `spring.devtools.*` control this behavior

### Database Utilities

**Clear All Tables**
```
DELETE http://localhost:8080/api/services/clear
DELETE http://localhost:8080/api/employees/clear
DELETE http://localhost:8080/api/clients/clear
DELETE http://localhost:8080/api/es/clear
DELETE http://localhost:8080/api/rendezvous/clear
DELETE http://localhost:8080/api/notifications/clear
```

---

## Troubleshooting

| Issue | Cause | Fix |
|-------|-------|-----|
| `Connection refused` | Database not running | Start PostgreSQL service |
| `No such database` | Database not created | Run Step 1 of Phase 2 |
| `Role does not exist` | User not created | Run Step 1 of Phase 2 |
| `Port 8080 in use` | Another app using port | Change `server.port` or kill process |
| `Hot reload not working` | DevTools disabled | Check `application.properties` |

---

## Next Steps

1. ✅ Run Phase 1 (no database) - verify no obvious errors
2. ✅ Run Phase 2 (configure database)
3. ✅ Run Phase 3 (test with database)
4. ✅ Use Postman to test all API endpoints from this guide
5. ✅ Review error messages for precision (exact timestamps shown for invalid times)

For detailed data model info, see `datamodel.mmd`.


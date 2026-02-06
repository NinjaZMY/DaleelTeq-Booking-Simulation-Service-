# 🌐 Application Access Points & URLs

## Development Environment

### During Development (2 Servers Running)

| Service | URL | Port | Status | Purpose |
|---------|-----|------|--------|---------|
| **Angular Dev Server** | http://localhost:4200 | 4200 | ✅ NEW | New Angular UI with HMR |
| **Spring Boot Backend** | http://localhost:8080 | 8080 | ✅ Running | REST API & Thymeleaf |
| **Legacy Thymeleaf UI** | http://localhost:8080/ui | 8080 | ✅ Fallback | Original web interface |
| **API Base** | http://localhost:8080/api | 8080 | ✅ API | REST endpoint base |
| **Database** | localhost:5432 | 5432 | ✅ Config | PostgreSQL 18 |

## API Endpoints (via http://localhost:8080/api or http://localhost:4200/api during dev)

### Services
```
GET    /api/services           → List all services
GET    /api/services/:id       → Get service by ID
POST   /api/services           → Create service
PUT    /api/services/:id       → Update service
DELETE /api/services/:id       → Delete service
DELETE /api/services/clear     → Clear all services
```

### Employees
```
GET    /api/employees          → List all employees
GET    /api/employees/:id      → Get employee by ID
POST   /api/employees          → Create employee
PUT    /api/employees/:id      → Update employee
DELETE /api/employees/:id      → Delete employee
DELETE /api/employees/clear    → Clear all employees
```

### Clients
```
GET    /api/clients            → List all clients
GET    /api/clients/:id        → Get client by ID
POST   /api/clients            → Create client
PUT    /api/clients/:id        → Update client
DELETE /api/clients/:id        → Delete client
DELETE /api/clients/clear      → Clear all clients
```

### Employee-Service Timeslots (ES)
```
GET    /api/es                 → List all timeslots
GET    /api/es/:id             → Get timeslot by ID
POST   /api/es                 → Create timeslot
PUT    /api/es/:id             → Update timeslot
DELETE /api/es/:id             → Delete timeslot
DELETE /api/es/clear           → Clear all timeslots
```

### Rendez-vous (Appointments)
```
GET    /api/rendezvous         → List all appointments
GET    /api/rendezvous/:id     → Get appointment by ID
POST   /api/rendezvous         → Book appointment
PATCH  /api/rendezvous/:id/cancel → Cancel appointment
DELETE /api/rendezvous/:id     → Delete appointment
DELETE /api/rendezvous/clear   → Clear all appointments
```

### Notifications
```
GET    /api/notifications      → List all notifications
GET    /api/notifications/:id  → Get notification by ID
POST   /api/notifications      → Create notification
DELETE /api/notifications/:id  → Delete notification
DELETE /api/notifications/clear → Clear all notifications
```

### Database Control
```
DELETE /api/clear-db           → Clear entire database
```

## Testing the Application

### Option 1: Angular Web UI (Recommended for Development)
1. Open http://localhost:4200
2. UI elements will auto-reload on code changes (HMR)
3. Errors display in the terminal running `npm start`

### Option 2: Legacy Thymeleaf UI (Fallback)
1. Open http://localhost:8080/ui
2. Full CRUD interface for all entities
3. Embedded API tester

### Option 3: Direct API Testing

#### Using cURL
```bash
# List all services
curl http://localhost:8080/api/services

# Create a service
curl -X POST http://localhost:8080/api/services \
  -H "Content-Type: application/json" \
  -d '{"lib":"Consultation","timeValue":30}'

# Get specific service
curl http://localhost:8080/api/services/1

# Update service
curl -X PUT http://localhost:8080/api/services/1 \
  -H "Content-Type: application/json" \
  -d '{"lib":"Updated","timeValue":45}'

# Delete service
curl -X DELETE http://localhost:8080/api/services/1
```

#### Using Postman
1. Import API endpoints from `/api/services`, `/api/employees`, etc.
2. Set proxy to `http://localhost:8080`
3. Test all CRUD operations
4. Check response format (wrapped in `{success, data, message, error, details}`)

#### Using Browser Console
```javascript
// In browser DevTools Console
fetch('http://localhost:4200/api/services')
  .then(r => r.json())
  .then(d => console.log(d.data))

// Or during development with proxy
fetch('/api/services')
  .then(r => r.json())
  .then(d => console.log(d.data))
```

## Production Environment

### After `mvn clean package`

| Service | URL | Port | Status |
|---------|-----|------|--------|
| **Production App** | http://localhost:8080 | 8080 | ✅ Running |
| **API Endpoints** | http://localhost:8080/api/* | 8080 | ✅ Available |
| **Legacy UI** | http://localhost:8080/ui | 8080 | ✅ Available |
| **Angular UI** | http://localhost:8080 | 8080 | ✅ Embedded |

Angular is embedded in the JAR and served from `src/main/resources/static/`

## Environment Variables

### `.env` File Location
```
C:\Users\Daleelteeq\Documents\from 21 November 2025 - Med Youssef Zehani\DaleelTeq-Booking-Simulation-Service-\.env
```

### `.env` Contents
```env
DB_USERNAME=booking_user
DB_PASSWORD=changeme
```

### Never Commit
- `.env` file (contains real credentials)
- `frontend/node_modules/`
- `target/` (Maven build output)
- `frontend/dist/` (Angular build output)
- `.env.local` variations

## Database Connections

### PostgreSQL Connection String
```
postgresql://booking_user:changeme@localhost:5432/booking_db
```

### Test Connection
```bash
psql -U booking_user -d booking_db -c "SELECT 1"
# Should return: 1 (success)
```

### View Tables
```bash
psql -U booking_user -d booking_db -c "\dt"
```

### Expected Tables
```
 Schema |        Name         | Type  |    Owner
--------+---------------------+-------+--------------
 public | clients             | table | booking_user
 public | employee_x_services | table | booking_user
 public | employees           | table | booking_user
 public | es_notification     | table | booking_user
 public | rendez_vous         | table | booking_user
 public | services            | table | booking_user
```

## Angular Dev Server Features

### When running `cd frontend && npm start`

**Auto-Reload (HMR)**:
- Edit any `.ts` file → auto-compiles and reloads
- Edit any `.html` file → page updates
- Edit any `.scss` file → styles update
- Component state is preserved (not full page refresh)

**Error Reporting**:
- TypeScript errors appear in terminal immediately
- Compilation errors block the reload
- Console errors appear in both terminal and browser

**API Proxy**:
- Any request to `/api/*` is forwarded to http://localhost:8080
- Configured in `frontend/proxy.conf.json`
- No CORS issues

**Port Configuration**:
- Default: 4200
- Change with: `ng serve --port 4201`

## Quick Start Command Reference

```bash
# Start Backend (Terminal 1)
mvn spring-boot:run

# Start Frontend (Terminal 2)
cd frontend
npm install  # First time only
npm start

# Build for Production
mvn clean package

# Run Production JAR
java -jar target/booking-simulation-service-1.0.0.jar

# Access Applications
http://localhost:4200          # Angular Dev
http://localhost:8080         # Production
http://localhost:8080/ui      # Legacy UI
http://localhost:8080/api/... # API Endpoints
```

## Troubleshooting Access

### Can't reach http://localhost:4200
- Check `npm start` is running in `frontend/`
- Check port 4200 is not blocked by firewall
- Check npm install completed successfully

### Can't reach http://localhost:8080
- Check `mvn spring-boot:run` is running
- Check port 8080 is not blocked
- Check database `.env` credentials are correct

### Can't connect to database
- Check PostgreSQL service is running
- Check `.env` has correct username/password
- Test: `psql -U booking_user -d booking_db`

### API returns 404
- Check Spring Boot is running
- Verify endpoint path is correct (case-insensitive: `/api/services`, `/api/Services`, etc.)
- Check database has data with GET requests

---

**Last Updated**: February 6, 2026  
**Environment**: Development & Production Ready

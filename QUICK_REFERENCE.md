# 🚀 Quick Reference Card

## Essential Commands

### Setup (One-time)
```bash
# 1. Setup environment
cp .env.example .env
# Edit .env with DB credentials

# 2. Setup database
psql -U postgres -f setup-db-permissions.sql
psql -U booking_user -d booking_db -f src/main/resources/db/schema-postgres18.sql

# 3. Verify database
psql -U booking_user -d booking_db -c "\dt"
```

### Development (Each session)

**Terminal 1 - Backend**:
```bash
mvn spring-boot:run
```
→ Runs on http://localhost:8080

**Terminal 2 - Frontend**:
```bash
cd frontend
npm install  # First time only
npm start
```
→ Runs on http://localhost:4200

**Terminal 3 - Testing (optional)**:
```bash
# Test API
curl http://localhost:8080/api/services
curl http://localhost:4200/api/services  # Same through proxy
```

### Production Build
```bash
mvn clean package
java -jar target/booking-simulation-service-1.0.0.jar
```
→ Runs on http://localhost:8080 (embedded Angular)

## URLs Quick Links

| Name | URL | Purpose |
|------|-----|---------|
| Angular UI | http://localhost:4200 | New interactive UI (HMR) |
| Backend | http://localhost:8080 | REST API server |
| Legacy UI | http://localhost:8080/ui | Fallback web interface |
| API Base | http://localhost:8080/api | REST endpoints |

## File Locations

| Item | Path |
|------|------|
| Environment | `.env` |
| Database User | `booking_user` |
| Database Name | `booking_db` |
| Angular App | `frontend/` |
| Backend | `src/main/java/...` |
| Backend Config | `src/main/resources/` |
| Legacy UI | `src/main/resources/templates/index.html` |
| Documentation | `README.md`, `ANGULAR_SETUP.md` |

## Database Commands

```bash
# Connect to database
psql -U booking_user -d booking_db

# List tables
\dt

# Show table structure
\d services

# View data
SELECT * FROM services;

# Exit
\q

# Backup database
pg_dump -U booking_user -d booking_db > backup.sql

# Restore database
psql -U booking_user -d booking_db < backup.sql
```

## Common Issues & Fixes

### Issue: `npm: command not found`
```bash
# Let Maven install it
mvn install

# Or install Node.js 24.11.1 manually
```

### Issue: Port 4200 in use
```bash
# Windows
netstat -ano | findstr :4200
taskkill /PID <PID> /F

# macOS/Linux
lsof -ti:4200 | xargs kill -9

# Or change port
cd frontend && npm start -- --port 4201
```

### Issue: Port 8080 in use
```bash
# Windows
netstat -ano | findstr :8080
taskkill /PID <PID> /F

# Change Spring port in application.yml
server.port=8090
```

### Issue: Database connection failed
```bash
# Check PostgreSQL running
psql -U postgres

# Verify .env credentials
cat .env

# Test connection
psql -U booking_user -d booking_db -c "SELECT 1"
```

### Issue: HMR not working
1. Make sure running `npm start` (not `ng serve`)
2. Edit a file in `frontend/src/`
3. Check terminal for "rebuild" message
4. Hard refresh browser (Ctrl+Shift+R)

## API Endpoint Examples

```bash
# List all services
curl http://localhost:8080/api/services

# Create service
curl -X POST http://localhost:8080/api/services \
  -H "Content-Type: application/json" \
  -d '{"lib":"Consultation","timeValue":30}'

# Get service #1
curl http://localhost:8080/api/services/1

# Update service #1
curl -X PUT http://localhost:8080/api/services/1 \
  -H "Content-Type: application/json" \
  -d '{"lib":"New Name","timeValue":45}'

# Delete service #1
curl -X DELETE http://localhost:8080/api/services/1

# Delete all services
curl -X DELETE http://localhost:8080/api/services/clear

# List all employees
curl http://localhost:8080/api/employees

# List all clients
curl http://localhost:8080/api/clients

# List all timeslots (ES)
curl http://localhost:8080/api/es

# List all appointments (rendez-vous)
curl http://localhost:8080/api/rendezvous

# List all notifications
curl http://localhost:8080/api/notifications
```

## TypeScript/Angular Basics

```typescript
// Import a service
import { ApiService } from './services/api.service';

// Inject in constructor
constructor(private api: ApiService) { }

// Make GET request
this.api.get('/services').then(response => {
  console.log(response.data);
});

// Make POST request
this.api.post('/services', {
  lib: 'New Service',
  timeValue: 30
}).then(response => {
  console.log('Created:', response.data);
});

// Handle errors
this.api.get('/services').catch(error => {
  console.error('Error:', error);
});
```

## File Structure Overview

```
project-root/
├── frontend/                # Angular app
│   ├── src/app/            # Components, services, config
│   ├── package.json        # npm dependencies
│   └── angular.json        # Angular configuration
├── src/main/
│   ├── java/.../          # Java code
│   └── resources/         # Config, templates, static files
├── pom.xml                # Maven configuration
├── README.md              # Main documentation
├── ANGULAR_SETUP.md       # Angular guide
└── .env                   # Database credentials (secret!)
```

## Essential Files to Understand

| File | Purpose |
|------|---------|
| `pom.xml` | Maven build config (includes Angular build) |
| `frontend/package.json` | npm dependencies |
| `frontend/proxy.conf.json` | API proxy configuration |
| `.env` | Database credentials (never commit!) |
| `README.md` | Complete setup guide |
| `ANGULAR_SETUP.md` | Angular development guide |

## Environment Variables

```env
# .env file
DB_USERNAME=booking_user
DB_PASSWORD=changeme
```

## Maven Commands

```bash
# Download dependencies
mvn clean

# Build project
mvn build

# Build and run tests
mvn verify

# Run Spring Boot
mvn spring-boot:run

# Build production JAR
mvn clean package

# Skip tests
mvn clean package -DskipTests

# Skip frontend build (use existing dist)
mvn clean package -DskipFrontend
```

## Angular Commands

```bash
# Inside frontend/ directory

# Install dependencies
npm install

# Start dev server with HMR
npm start

# Build for production
npm run build:prod

# Watch mode
npm run watch

# Run tests
npm test
```

## Verification Checklist

- [ ] Java 25: `java -version`
- [ ] Maven: `mvn -v`
- [ ] PostgreSQL: `psql -V`
- [ ] Database: `psql -U booking_user -d booking_db -c "\dt"`
- [ ] Backend: http://localhost:8080/api/services (running)
- [ ] Frontend: http://localhost:4200 (running)
- [ ] API response: Check it returns JSON

## Time Windows

- **Work Hours**: 09:00 - 16:00 (configurable)
- **Service Durations**: 15, 20, 25, 30 minutes
- **Double Duration (x_2)**: Double the selected duration

## Response Format

All API responses have this structure:
```json
{
  "success": true,
  "data": [],
  "message": "Operation successful",
  "error": null,
  "details": null
}
```

Extract data with: `response.data`

---

## 📞 Quick Help

- **Setup Issues**: See `README.md` → Installation & Setup
- **Frontend Issues**: See `ANGULAR_SETUP.md` → Troubleshooting
- **API Issues**: See `URLS_AND_ENDPOINTS.md` → Testing
- **Database Issues**: See database commands above

---

**Bookmark this page!** 📌

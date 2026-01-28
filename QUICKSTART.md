# Quick Start Guide

## 🚀 Get Started in 5 Minutes

### Step 1: Run Without Database (Verify Setup)

```bash
mvn spring-boot:run
```

**Expected**: Application starts on `http://localhost:8080`

Stop with `Ctrl+C` when ready to configure database.

### Step 2: Database Setup (5 minutes)

**Prerequisites**: PostgreSQL 18 installed and running

#### 2.1 Grant Permissions (as postgres superuser)
```bash
psql -U postgres -d booking_db -f setup-db-permissions.sql
```

#### 2.2 Create Tables and Sample Data (as booking_user)
```bash
psql -U booking_user -d booking_db -f src/main/resources/db/schema-postgres18.sql
```

#### 2.3 Verify Tables Created
```bash
psql -U booking_user -d booking_db -c "\dt"
```

Expected output: 6 tables (clients, employee_x_services, employees, es_notification, rendez_vous, services)

#### 2.4 Verify Sample Data
```bash
psql -U booking_user -d booking_db -c "SELECT COUNT(*) FROM services;"
```

Expected: **4** services

### Step 3: Create `.env` File

```bash
cp .env.example .env
```

No changes needed if using default credentials above.

### Step 4: Run Application with Database

```bash
mvn clean compile
mvn spring-boot:run
```

Visit: `http://localhost:8080`

### Step 5: Test API Endpoints

See **TESTING_GUIDE.md** for Postman examples and API reference.

You should see the **DaleelTeq Booking Dashboard** with all entity lists and action buttons.

## ✅ Verify Everything Works

### 1. Check Database Status
- Click the **Refresh Data** button on the dashboard
- You should see: Services: 4, Employees: 3, Clients: 3

### 2. Create a Test Timeslot
- Open the **Timeslots (ES)** section
- Employee ID: **1**
- Service ID: **1**
- Date: **2026-02-20**
- Start Time: **10:00**
## 🧪 Testing

### Via Web UI

1. **Create Service**: Form at http://localhost:8080
2. **Create Employee**: Add employee via UI
3. **Create Timeslot (ES)**: Provide date, start time, duration
4. **Book Appointment**: Select timeslot and client

### Via Postman

Base URL: `http://localhost:8080/api`

See **TESTING_GUIDE.md** for complete Postman request examples.

## 🆘 Troubleshooting

| Issue | Solution |
|-------|----------|
| "Connection refused" to PostgreSQL | Ensure PostgreSQL is running: `psql -U postgres` |
| "Java 25 not found" | Run `java -version` - must be 25.x.x |
| IntelliJ doesn't recognize Maven | Right-click `pom.xml` → "Add as Maven Project" |
| Hot reload not working | Build project: Ctrl+Shift+F9 (IntelliJ) |
| Port 8080 in use | Change `server.port` in `application.properties` |

## 📚 Next Steps

1. ✅ Run Phase 1 of **TESTING_GUIDE.md** (no database)
2. ✅ Set up database (Phase 2)
3. ✅ Test all endpoints with Postman (Phase 3)
4. ✅ Review error messages for precision
5. ✅ Run unit tests: `mvn test`


# 🚀 Quick Start - Run These Commands Now

## Step 1: Database Setup (One-time only)

```bash
# 1a. Grant permissions (as postgres superuser)
psql -U postgres -d booking_db -f setup-db-permissions.sql

# 1b. Create schema (as booking_user)
psql -U booking_user -d booking_db -f src/main/resources/db/schema-postgres18.sql

# 1c. Verify tables created
psql -U booking_user -d booking_db -c "\dt"
```

## Step 2: Build the Project

```bash
mvn clean compile
```

Expected output:
```
[INFO] BUILD SUCCESS
[INFO] Total time: 9.397 s
```

## Step 3: Run the Application

```bash
mvn spring-boot:run
```

Expected output (look for this in the logs):
```
Loading .env file from: C:\path\to\project\.env
Loaded 2 properties from .env file
Line 2: Loaded property DB_USERNAME=booking_user
Line 3: Loaded property DB_PASSWORD=***
HikariPool-1 : Connection is working, reset connection idle counter
Started BookingApplication in X.XXX seconds (JVM running for Y.YYY)
```

## Step 4: Test the Application

Open in browser:
```
http://localhost:8080
```

Or test API:
```bash
curl http://localhost:8080/api/services
```

---

## ✅ If You See This - Everything Works!

```json
[
  {
    "id": 1,
    "lib": "S1",
    "timeValue": 15,
    "createdAt": "2026-01-28T..."
  },
  ...
]
```

---

## ❌ If You See This - There's Still an Issue

```
[ERROR] ... com.zaxxer.hikari.pool.HikariPool : Exception during pool initialization
[ERROR] org.postgresql.util.PSQLException: FATAL: password authentication failed for user "booking_user"
```

**Solution**:
1. Check your .env file has the correct password
2. Run: `psql -U postgres -c "\du booking_user"` to verify user exists
3. Ensure PostgreSQL password for `booking_user` matches `.env`
4. Try resetting password: `psql -U postgres -c "ALTER USER booking_user WITH PASSWORD 'simulate';"`

---

## Notes

- The `.env` file is loaded automatically during Spring startup
- Credentials are read from `.env` before datasource bean creation
- If `.env` is missing, falls back to defaults in `application.properties`
- Password is masked as `***` in logs for security

---

## Common Commands During Development

```bash
# Recompile without running
mvn clean compile

# Run tests
mvn test

# Run specific test
mvn test -Dtest=ServiceServiceTest

# Build JAR
mvn package -DskipTests

# View dependencies
mvn dependency:tree

# Check for outdated dependencies
mvn versions:display-updates
```

---

## Stop the Application

```bash
# In the terminal where mvn spring-boot:run is running
Ctrl+C
```

---

That's it! 🎉 Your application should now:
- ✅ Load .env file correctly
- ✅ Connect to PostgreSQL without auth errors
- ✅ Display database data on the dashboard
- ✅ Accept API requests

Good luck with testing! 🚀


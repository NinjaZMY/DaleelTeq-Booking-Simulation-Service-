# Quick Start Guide

## 🚀 Get Started in 5 Minutes

### Step 1: Database Setup (5 min)

#### On Windows with psql installed:

Open Command Prompt and run:

```cmd
psql -U postgres
```

Paste and execute this SQL:

```sql
CREATE DATABASE booking_db;
CREATE USER booking_user WITH PASSWORD 'changeme';
GRANT ALL PRIVILEGES ON DATABASE booking_db TO booking_user;
\q
```

Then load the schema:

```cmd
psql -U booking_user -d booking_db -f src/main/resources/db/schema-postgres18.sql
```

#### On macOS/Linux:

```bash
psql -U postgres
```

Then same SQL as above, followed by:

```bash
psql -U booking_user -d booking_db -f src/main/resources/db/schema-postgres18.sql
```

### Step 2: Environment Setup (1 min)

The `.env` file is already created with correct defaults:
- `DB_USERNAME=booking_user`
- `DB_PASSWORD=changeme`

No changes needed if you used the SQL above.

### Step 3: Run the Application (1 min)

**Option A: Using Maven Wrapper (Recommended)**

```bash
./mvnw spring-boot:run
```

Or on Windows:

```cmd
mvnw.cmd spring-boot:run
```

**Option B: Using IntelliJ**

1. Open `BookingApplication.java`
2. Click the green Run button (or press Shift+F10)

**Option C: Using local Maven**

```bash
mvn spring-boot:run
```

### Step 4: Access the Application

Open your browser and go to:

```
http://localhost:8080
```

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
- Click **Create Timeslot**
- Check the response box for success message

### 3. Book an Appointment
- Go to **Rendez-vous** section
- Timeslot ID (ES ID): **4** (the one you just created)
- Client ID: **1**
- Click **Book Appointment**
- Response should show new rendez-vous with status "Active"

### 4. Test Postman
- Base URL: `http://localhost:8080/api`

Example requests:

**Get All Services:**
```
GET /api/services
```

**Create Service:**
```
POST /api/services
Content-Type: application/json

{
  "lib": "Massage",
  "timeValue": 30
}
```

**Create Timeslot:**
```
POST /api/es
Content-Type: application/json

{
  "idE": 1,
  "idS": 2,
  "date": "2026-02-21",
  "start": "11:00",
  "x2": false
}
```

**Book Appointment:**
```
POST /api/rendezvous
Content-Type: application/json

{
  "idES": 5,
  "idC": 2
}
```

## 🔧 Troubleshooting

### Application won't start: "Connection refused"

**Problem:** PostgreSQL is not running

**Solution:**

**Windows:**
```cmd
# Check if PostgreSQL is running in Services
# Or start it via PowerShell as Admin:
net start PostgreSQL
```

**macOS:**
```bash
brew services start postgresql@18
```

**Linux:**
```bash
sudo systemctl start postgresql
```

### Maven build fails: "Java 25 not found"

**Problem:** Java version mismatch

**Solution:**
```bash
java -version  # Should show Java 25.x.x
```

If not, set `JAVA_HOME`:

**Windows:**
```cmd
set JAVA_HOME=C:\Program Files\Java\jdk-25
```

**macOS/Linux:**
```bash
export JAVA_HOME=/usr/libexec/java_home -v 25
```

### IntelliJ doesn't recognize project

**Solution:**
1. File → Invalidate Caches → Invalidate and Restart
2. Or: Right-click `pom.xml` → Add as Maven Project

### Changes not reloading

**Problem:** Hot reload not working

**Solution:**
- Build the project manually: Ctrl+Shift+F9 (IntelliJ)
- Or restart: mvn spring-boot:run

## 📚 Next Steps

1. **Read the full README.md** for complete API documentation
2. **Test all endpoints** using the web dashboard or Postman
3. **Explore the code** in `src/main/java`
4. **Run tests** with `./mvnw test`
5. **Deploy** to production when ready

## 🆘 Still Stuck?

1. Check the application logs (console output)
2. Verify database connection: `psql -U booking_user -d booking_db -c "SELECT 1;"`
3. Check `.env` file has correct credentials
4. Make sure port 8080 is not in use

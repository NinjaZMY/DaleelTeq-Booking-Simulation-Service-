# 🚀 START HERE - Quick Reference Guide

## Your Project is Ready! ✅

**Status**: Application builds and runs successfully without database errors  
**Next Step**: Configure PostgreSQL and test APIs  

---

## 📋 Three Essential Files You Need

### 1. **TESTING_GUIDE.md** ⭐ Read First
- **What**: Step-by-step testing workflow
- **When**: Read before doing anything else
- **Contains**: 
  - Phase 1: Run without database (verify setup)
  - Phase 2: Configure PostgreSQL
  - Phase 3: Test with database
  - Complete Postman examples for all APIs

### 2. **README.md** (Reference)
- **What**: Complete project documentation
- **When**: Look here for detailed API reference
- **Contains**:
  - All API endpoints
  - Data model summary
  - Troubleshooting guide
  - Configuration options

### 3. **QUICKSTART.md** (Quick Setup)
- **What**: Fast 5-minute setup summary
- **When**: Use if you want condensed version
- **Contains**:
  - Database setup commands
  - Application startup
  - Basic testing steps

---

## 🎯 Quick Start (5 Minutes)

### Right Now - No Database Needed
```bash
mvn spring-boot:run
```

Visit: `http://localhost:8080`

**Expected Result**: Website loads, but data operations fail (normal - no database)

Stop with: `Ctrl+C`

### When Ready - Configure Database

```bash
# 1. Create database (in PostgreSQL)
psql -U postgres
```

Then paste:
```sql
CREATE DATABASE booking_db;
CREATE USER booking_user WITH PASSWORD 'changeme';
GRANT ALL PRIVILEGES ON DATABASE booking_db TO booking_user;
\q
```

Load schema:
```bash
psql -U booking_user -d booking_db -f src/main/resources/db/schema-postgres18.sql
```

### Then - Run with Database

```bash
mvn clean compile
mvn spring-boot:run
```

Visit: `http://localhost:8080` (now with data!)

---

## 🛠️ Common Commands

```bash
# Build
mvn clean compile
mvn clean package -DskipTests

# Run
mvn spring-boot:run

# Test
mvn test                 # Unit tests
mvn verify              # Integration tests

# Verify setup
mvn -v                  # Check Maven version
java -version           # Check Java version
```

---

## 🧪 Testing APIs with Postman

**Base URL**: `http://localhost:8080/api`

### Get All Services
```
GET /api/services
```

### Create Service
```
POST /api/services
Content-Type: application/json

{
  "lib": "Haircut",
  "timeValue": 15
}
```

### Create Timeslot
```
POST /api/es
Content-Type: application/json

{
  "idE": 1,
  "idS": 1,
  "date": "2026-02-20",
  "start": "09:00",
  "x2": false
}
```

### Book Appointment
```
POST /api/rendezvous
Content-Type: application/json

{
  "idES": 1,
  "idC": 1
}
```

**Note**: See TESTING_GUIDE.md for complete Postman collection examples.

---

## 📂 Important Files & Folders

```
TESTING_GUIDE.md         ← Read this first
QUICKSTART.md            ← Fast setup
README.md                ← API reference
DOCUMENTATION_MAP.md     ← File navigation
PROJECT_STATUS.md        ← Current status

.env                     ← Your local config (secret, .gitignore)
.env.example             ← Template for credentials
pom.xml                  ← Maven configuration

src/                     ← All source code
src/main/resources/db/   ← Database schema
src/main/resources/templates/  ← Web UI

datamodel.mmd            ← ERD diagram
datamodel.png            ← Visual diagram
```

---

## ⚠️ Common Issues

| Issue | Fix |
|-------|-----|
| Port 8080 already in use | Edit `application.properties`: `server.port=8081` |
| Database connection failed | Create database first (see DATABASE section) |
| Lombok errors | Run: `mvn clean compile` then `mvn spring-boot:run` |
| Hot reload not working | IntelliJ: Ctrl+Shift+F9 (Build Project) |

---

## 🔄 Development Workflow

1. **Start Application**
   ```bash
   mvn spring-boot:run
   ```

2. **Edit Code** (Java files, HTML templates, etc.)
   - Changes automatically reload (DevTools enabled)
   - No restart needed

3. **Test in Browser**
   - Visit: `http://localhost:8080`
   - Or use Postman: `http://localhost:8080/api/...`

4. **View Logs in Console**
   - Application logs show in terminal
   - See error details immediately

---

## 🎓 What Was Done

✅ Fixed Lombok compilation error (Java 25 compatibility)  
✅ Updated documentation to use local `mvn`  
✅ Consolidated documentation (removed duplicates)  
✅ Verified application starts without database  
✅ Created focused testing guide  

---

## 📞 Need Help?

1. **Quick issues**: Check troubleshooting in README.md
2. **Testing help**: See TESTING_GUIDE.md
3. **API questions**: See README.md API section
4. **Setup help**: See QUICKSTART.md

---

## 🎯 Your Next Steps

- [ ] Read TESTING_GUIDE.md (15 min)
- [ ] Run `mvn spring-boot:run` and check web UI (5 min)
- [ ] Configure PostgreSQL when ready (Phase 2, see TESTING_GUIDE.md)
- [ ] Test all endpoints with Postman
- [ ] Run unit tests: `mvn test`

---

**Everything is ready. Start with TESTING_GUIDE.md! 🚀**


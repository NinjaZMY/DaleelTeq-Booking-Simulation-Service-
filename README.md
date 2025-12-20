# DaleelTeq Booking Service - REST API Backend

A fully functional Spring Boot 4 application (Java 25) implementing a complete employee-service-client booking and reservation system with PostgreSQL backend.

## 📋 Table of Contents

- [Features](#features)
- [Project Structure](#project-structure)
- [Prerequisites](#prerequisites)
- [Database Setup](#database-setup)
- [Configuration](#configuration)
- [Running the Application](#running-the-application)
- [API Endpoints](#api-endpoints)
- [Testing](#testing)
- [Data Model](#data-model)
- [Error Handling](#error-handling)
- [Logging](#logging)

---

## ✨ Features

### Core Functionality
- ✅ Complete CRUD operations for all entities (S, E, C, ES, R, N)
- ✅ Dual endpoint variants: path-id (`/api/entity/{id}`) and JSON-body (`/api/entity`)
- ✅ Transactional booking workflow: ES status management + Notification creation
- ✅ Transactional cancellation: ES status reset + Notification with cancellation reason
- ✅ Precise validation with detailed error messages for each failing field
- ✅ Available IDs returned in error responses for easy testing

### Technical Stack
- **Java 25** with Maven 4.0.0 Build System
- **Spring Boot 4.0.0** (Web, Data JPA, Thymeleaf, WebSocket)
- **PostgreSQL 18** with optimistic locking (@Version)
- **Lombok** for reduced boilerplate
- **Testcontainers** with singleton reusable container pattern
- **Spring DevTools** with LiveReload (hot reload)
- **.env support** via Java Dotenv (auto-loaded with fallback to OS env vars)

### User Interface
- **Interactive Thymeleaf dashboard** at `/` for testing all endpoints
- Real-time entity lists with available IDs
- Confirmation modals for destructive operations
- JSON response display with syntax highlighting
- DeleteAll and ClearDB buttons with safety confirmations

---

## 📁 Project Structure

```
src/main/java/com/daleelteq/booking/
├── BookingApplication.java                 # Main Spring Boot entry point
├── config/
│   ├── WebConfig.java                     # Case-insensitive routing config
│   └── DotenvEnvironmentPostProcessor.java # .env auto-loader
├── domain/                                 # JPA Entities
│   ├── Service.java                       # S (Services)
│   ├── Employee.java                      # E (Employees)
│   ├── Client.java                        # C (Clients)
│   ├── EmployeeService.java               # ES (Employee_x_Services)
│   ├── RendezVous.java                    # R (Rendez-vous/Bookings)
│   └── Notification.java                  # N (Notifications)
├── repository/                             # Spring Data JPA Repositories
│   ├── ServiceRepository.java
│   ├── EmployeeRepository.java
│   ├── ClientRepository.java
│   ├── EmployeeServiceRepository.java
│   ├── RendezVousRepository.java
│   └── NotificationRepository.java
├── service/                                # Business Logic Services
│   ├── ServiceService.java
│   ├── EmployeeService.java
│   ├── ClientService.java
│   ├── EmployeeServiceService.java
│   ├── RendezVousService.java
│   └── NotificationService.java
├── controller/
│   ├── rest/                              # REST API Controllers
│   │   ├── ServiceRestController.java
│   │   ├── EmployeeRestController.java
│   │   ├── ClientRestController.java
│   │   ├── EmployeeServiceRestController.java
│   │   ├── RendezVousRestController.java
│   │   └── NotificationRestController.java
│   └── web/
│       └── WebIndexController.java        # Thymeleaf UI Controller
├── dto/                                    # Data Transfer Objects
│   ├── ServiceDto.java
│   ├── EmployeeDto.java
│   ├── ClientDto.java
│   ├── EmployeeServiceDto.java
│   ├── RendezVousDto.java
│   ├── NotificationDto.java
│   ├── ApiResponse.java                   # Unified response format
│   └── IdRequestDto.java                  # Universal request DTO
└── exception/                              # Exception Handling
    ├── EntityNotFoundException.java
    ├── ValidationException.java
    ├── BusinessRuleException.java
    └── GlobalExceptionHandler.java

src/main/resources/
├── application.properties                  # Configuration (reads .env)
├── db/
│   └── schema-postgres18.sql              # PostgreSQL DDL
└── templates/
    └── index.html                         # Interactive Test UI

src/test/java/                             # Test Classes (JUnit 5, Mockito, Testcontainers)

.env                                       # Environment variables (gitignored)
.env.example                               # Template for .env
.gitignore                                 # Git ignore rules
pom.xml                                    # Maven configuration (Java 25, Spring Boot 4)
```

---

## 🔧 Prerequisites

### System Requirements
- **Java 25 JDK** (not JRE) — Download from [oracle.com](https://www.oracle.com/java/technologies/javase/jdk25-archive-downloads.html) or use a package manager
- **PostgreSQL 18** — [Download](https://www.postgresql.org/download/)
- **Maven** (optional; project includes Maven Wrapper) — or use IntelliJ IDE's built-in Maven support

### Verify Java Version
```bash
java -version
# Should output: java version "25" (or 25.x.x)
```

---

## 💾 Database Setup

### Step 1: Create Database and User

Connect to PostgreSQL as a superuser and execute:

```sql
-- Connect as superuser (usually 'postgres')
psql -U postgres

-- Create database
CREATE DATABASE booking_db;

-- Create user
CREATE USER booking_user WITH PASSWORD 'changeme';

-- Grant privileges
GRANT ALL PRIVILEGES ON DATABASE booking_db TO booking_user;
ALTER DATABASE booking_db OWNER TO booking_user;

-- Exit psql
\q
```

### Step 2: Create Tables

The application will automatically create tables on first run (Hibernate `ddl-auto=update`). Alternatively, manually apply the schema:

```bash
psql -U booking_user -d booking_db -f src/main/resources/db/schema-postgres18.sql
```

### Step 3: Verify Tables

```bash
psql -U booking_user -d booking_db -c "\dt"
```

Expected tables:
- `services` (S)
- `employees` (E)
- `clients` (C)
- `employee_services` (ES)
- `rendez_vous` (R)
- `notifications` (N)

---

## ⚙️ Configuration

### Environment Variables (.env file)

The project reads credentials from `.env` file (created automatically):

```env
# Database Credentials Template
DB_USERNAME=booking_user
DB_PASSWORD=changeme
DB_URL=jdbc:postgresql://localhost:5432/booking_db
DB_DRIVER=org.postgresql.Driver

# Application Properties
APP_NAME=DaleelTeq Booking Service
APP_PORT=8080
APP_PROFILE=dev
```

**Important:** `.env` is in `.gitignore` and will NOT be committed to Git. The `.env.example` file shows the required template.

### Application Properties

File: `src/main/resources/application.properties`

Key settings:
- **DevTools Hot Reload:** `spring.devtools.restart.enabled=true`
- **Hibernate DDL:** `spring.jpa.hibernate.ddl-auto=update`
- **Logging Level:** `logging.level.com.daleelteq.booking=DEBUG`
- **Thymeleaf Cache:** `spring.thymeleaf.cache=false` (for development)

---

## 🚀 Running the Application

### Option 1: IntelliJ IDE (Recommended)

1. **Open Project:**
   - File → Open → Select project directory

2. **Configure Maven:**
   - IntelliJ auto-detects Maven; allows running via built-in Maven tools

3. **Set .env in Run Configuration:**
   - Run → Edit Configurations
   - Add Environment Variable: `DB_PASSWORD=changeme` (or leave blank to auto-load from .env)

4. **Run:**
   - Click Run button or press Shift+F10
   - App starts at `http://localhost:8080`

### Option 2: Command Line

```bash
# Navigate to project root
cd DaleelTeq-Booking-Simulation-Service-

# Build project (skip tests for first run)
mvn -DskipTests=false clean package

# Run application
mvn spring-boot:run
```

Or run the packaged JAR:

```bash
java -jar target/booking-service-0.0.1-SNAPSHOT.jar
```

### Verify Application Started

```
[main] o.s.b.w.e.tomcat.TomcatWebServer : Tomcat started on port(s): 8080
```

Visit: `http://localhost:8080/`

---

## 🔌 API Endpoints

### Base URL
```
http://localhost:8080/api
```

### Case-Insensitive Routing
All endpoints accept ANY casing: `/api/es`, `/api/ES`, `/api/eS`, etc.

### 1. Services (S)

```http
GET    /api/services              # List all services
GET    /api/services/{id}         # Get by ID
POST   /api/services              # Create (JSON body)
PUT    /api/services/{id}         # Update by path ID
PUT    /api/services              # Update by JSON body (include id)
DELETE /api/services/{id}         # Delete by path ID
DELETE /api/services              # Delete by JSON body (include id)
```

**Example Request:** Create Service
```json
POST /api/services
{
  "lib": "Haircut",
  "timeValue": 30
}
```

**Example Response:**
```json
{
  "success": true,
  "message": "Service created successfully",
  "data": {
    "id": 1,
    "lib": "Haircut",
    "timeValue": 30
  },
  "timestamp": "2025-12-20T10:30:00"
}
```

### 2. Employees (E)

```http
GET    /api/employees              # List all
GET    /api/employees/{id}         # Get by ID
POST   /api/employees              # Create
PUT    /api/employees/{id}         # Update by path ID
PUT    /api/employees              # Update by JSON body
DELETE /api/employees/{id}         # Delete by path ID
DELETE /api/employees              # Delete by JSON body
```

### 3. Clients (C)

```http
GET    /api/clients              # List all
GET    /api/clients/{id}         # Get by ID
POST   /api/clients              # Create
PUT    /api/clients/{id}         # Update by path ID
PUT    /api/clients              # Update by JSON body
DELETE /api/clients/{id}         # Delete by path ID
DELETE /api/clients              # Delete by JSON body
```

### 4. Employee Services (ES) — Timeslots

```http
GET    /api/es                  # List all
GET    /api/es/free             # List free slots (available for booking)
GET    /api/es/{id}             # Get by ID
POST   /api/es                  # Create
PUT    /api/es/{id}             # Update by path ID
PUT    /api/es                  # Update by JSON body
DELETE /api/es/{id}             # Delete by path ID
DELETE /api/es                  # Delete by JSON body
```

**Example Request:** Create Employee Service (Timeslot)
```json
POST /api/es
{
  "idE": 1,
  "idS": 1,
  "date": "2025-12-31",
  "start": "09:00",
  "end": "10:00",
  "x2": false
}
```

### 5. Rendezvous (R) — Bookings

```http
GET    /api/rendezvous                           # List all
GET    /api/rendezvous/{id}                      # Get by ID
POST   /api/rendezvous/book?employeeServiceId=1&clientId=2  # Book appointment
POST   /api/rendezvous/book-json                 # Book with JSON body
PUT    /api/rendezvous/{id}                      # Update
PUT    /api/rendezvous/{id}/cancel?cancelledBy=Client  # Cancel by path
PUT    /api/rendezvous                           # Update by JSON body
DELETE /api/rendezvous/{id}                      # Delete by path ID
DELETE /api/rendezvous                           # Delete by JSON body
```

**Example Request:** Book Appointment
```json
POST /api/rendezvous/book-json
{
  "idEs": 1,
  "idC": 2
}
```

**Example Response (Success):**
```json
{
  "success": true,
  "message": "Appointment booked successfully",
  "data": {
    "id": 1,
    "idEs": 1,
    "idC": 2,
    "status": "Active",
    "createdAt": "2025-12-20T10:35:00"
  }
}
```

**Example Response (Error - Slot Taken):**
```json
{
  "success": false,
  "message": "Booking failed",
  "errorDetails": "Employee service is not available (status: taken). Available slot IDs: [2, 3, 5]",
  "availableIds": [2, 3, 5],
  "timestamp": "2025-12-20T10:36:00"
}
```

### 6. Notifications (N)

```http
GET    /api/notifications            # List all
GET    /api/notifications/{id}       # Get by ID
POST   /api/notifications            # Create manual notification
DELETE /api/notifications/{id}       # Delete by path ID
DELETE /api/notifications            # Delete by JSON body
```

---

## 🧪 Testing

### Interactive UI Testing

1. **Open Browser:** `http://localhost:8080/`
2. **Tabs:** Services, Employees, Clients, Employee Services, Rendezvous, Notifications, Admin Tools
3. **Each Tab Provides:**
   - Live entity table
   - Available IDs list
   - Operation form (GET, CREATE, UPDATE, DELETE)
   - Real-time response display with error details

### Running Tests

```bash
# Run all tests
mvn test

# Run specific test class
mvn test -Dtest=RendezVousServiceTest

# Run with coverage
mvn test jacoco:report
```

### Unit Tests (Included)

- `ServiceServiceTest` — Service CRUD operations
- `RendezVousServiceTest` — Booking and cancellation logic
- `ValidationTest` — Time/date validation rules
- Controller tests with MockMvc

### Integration Tests

Uses Testcontainers with reusable PostgreSQL container (singleton pattern for speed).

---

## 📊 Data Model

### Entities & Relationships

```
Services (S)
  │
  ├─ (1:*) ──────────────────── Employee_Services (ES)
  │                              │
  │ Employees (E)                │
  │  │                           │
  │  └─ (1:*) ────────────────── │
  │                              │
  │                              ├─ (1:*) ────────── Rendez_vous (R)
  │                                                  │
  │ Clients (C)                                      │
  │  │                                               │
  │  └─ (1:*) ────────────────────────────────────── │
  │                                                  │
  │                                                  └─ (1:*) ──── Notifications (N)
```

### Key Fields

**Services (S)**
- `id`: PK
- `lib`: Service name
- `time_value`: Duration {15, 20, 25, 30} mins

**Employees (E)**
- `id`: PK
- `lib`: Employee name

**Clients (C)**
- `id`: PK
- `lib`: Client name
- `number`: Phone number

**Employee_Services (ES)**
- `id`: PK
- `id_e`: FK to Employee
- `id_s`: FK to Service
- `date`: YYYY-MM-DD
- `start`, `end`: HH:mm time slots
- `x2`: Boolean (double duration)
- `time_value`: Computed (S.time_value * (X_2 ? 2 : 1))
- `status`: 'free' | 'taken'

**Rendez_vous (R)**
- `id`: PK
- `id_es`: FK to EmployeeService
- `id_c`: FK to Client
- `status`: 'Active' | 'Cancelled by Client' | 'Cancelled by Employee'

**Notifications (N)**
- `id`: PK
- `id_r`: FK to RendezVous
- `type`: 'booked' | 'cancelled'
- `value`: Status/message text

---

## ⚠️ Error Handling

### Error Response Format

All errors return:

```json
{
  "success": false,
  "message": "Validation failed",
  "errorDetails": "Field: start - Start time must be between 08:00 and 17:00. You provided: 07:30",
  "availableIds": [1, 2, 3],
  "timestamp": "2025-12-20T10:40:00"
}
```

### Common Errors

| Status | Scenario | Example |
|--------|----------|---------|
| 400 | Invalid input (bad time format, invalid date) | Start time must be between 08:00 and 17:00 |
| 404 | Entity not found | Service with id 999 not found |
| 409 | Business rule violation | Employee service is already taken |
| 500 | Server error | Internal server error |

---

## 📝 Logging

Logs are configured in `application.properties`:

```properties
logging.level.com.daleelteq.booking=DEBUG
logging.level.org.springframework.web=INFO
logging.level.org.hibernate.SQL=DEBUG
```

### Log Locations

- **Console:** Visible in IDE or terminal
- **File:** (Optional) Configure in `logback-spring.xml`

### Example Log Entries

```
2025-12-20 10:35:00.123 DEBUG [main] RendezVousService : Booking rendezvous for ES id: 1, Client id: 2
2025-12-20 10:35:00.234 INFO  [main] RendezVousService : RendezVous created with id: 1
2025-12-20 10:35:00.235 DEBUG [main] GlobalExceptionHandler : Entity not found: Service with id 999
```

---

## 🔄 Hot Reload (DevTools)

Changes to source files trigger automatic restart:

1. **Make code change** (e.g., modify a method in a service)
2. **IDE auto-compiles** (or manually compile)
3. **DevTools detects change** and restarts application
4. **No manual restart needed** — app is live after 2-3 seconds

Excluded from restart (to avoid unnecessary resets):
- Templates (can reload separately)
- Static assets
- Logback config

---

## 📦 Build & Deployment

### Build JAR

```bash
mvn clean package
```

Output: `target/booking-service-0.0.1-SNAPSHOT.jar`

### Run JAR

```bash
java -jar target/booking-service-0.0.1-SNAPSHOT.jar
```

### Docker (Optional)

Create `Dockerfile`:

```dockerfile
FROM openjdk:25
COPY target/booking-service-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
```

Build & run:

```bash
docker build -t booking-service .
docker run -p 8080:8080 --env-file .env booking-service
```

---

## 📚 Dependencies

See `pom.xml` for full list. Key dependencies:

- `spring-boot-starter-web` — REST APIs
- `spring-boot-starter-data-jpa` — Database layer
- `spring-boot-starter-thymeleaf` — Web UI
- `postgresql` — JDBC driver
- `lombok` — Boilerplate reduction
- `spring-boot-devtools` — Hot reload
- `testcontainers` — Container-based testing

---

## 🤝 Contributing

1. Create a feature branch
2. Make changes
3. Run tests: `mvn test`
4. Build: `mvn clean package`
5. Push and create a PR

---

## 📄 License

Proprietary — DaleelTeq

---

## 🆘 Troubleshooting

### Q: Application won't start
**A:** Check:
1. Java 25 installed: `java -version`
2. PostgreSQL running: `psql -U postgres -c "SELECT version()"`
3. `.env` file has correct credentials

### Q: "Cannot connect to database"
**A:**
```bash
# Test connection
psql -U booking_user -d booking_db -c "SELECT 1"
```

### Q: Port 8080 already in use
**A:** Change in `application.properties`:
```properties
server.port=8081
```

### Q: Maven not found
**A:** Use Maven Wrapper:
```bash
./mvnw clean package  # On Linux/Mac
mvnw.cmd clean package  # On Windows
```

### Q: Hot Reload not working
**A:** In IntelliJ: Enable **Build Project Automatically**
- File → Settings → Compiler → Check "Build project automatically"

---

## 📞 Support

For issues, check logs and error responses. They include:
- Exact field that failed validation
- Valid range/format for that field
- Available IDs for testing next request

---

**Version:** 0.0.1  
**Last Updated:** 2025-12-20  
**Spring Boot:** 4.0.0  
**Java:** 25


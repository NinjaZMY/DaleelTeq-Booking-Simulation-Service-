# DaleelTeq Booking Simulation Service

A Spring Boot 4 REST API for managing appointment bookings with employee scheduling, timeslots, and client notifications.

## 📋 Table of Contents

- [Features](#features)
- [Tech Stack](#tech-stack)
- [Prerequisites](#prerequisites)
- [Installation & Setup](#installation--setup)
- [Database Setup](#database-setup)
- [Running the Application](#running-the-application)
- [API Endpoints](#api-endpoints)
- [Environment Configuration](#environment-configuration)
- [Testing](#testing)

## ✨ Features

- **Service Management**: Define available services with configurable durations (15, 20, 25, 30 minutes)
- **Employee Management**: Manage employees and their schedules
- **Client Management**: Register and manage clients
- **Timeslot Management (ES)**: Create employee-service timeslots with date/time validation
- **Double Duration Support**: Enable `x_2` flag to double a timeslot's duration
- **Rendez-vous Booking**: Book appointments linking clients to timeslots
- **Notification System**: Automatic audit trail of booking/cancellation events
- **Time Window Validation**: Configurable working hours (default 09:00–16:00)
- **Comprehensive Error Handling**: Precise, actionable error messages with available IDs
- **Case-Insensitive Routing**: All routes accept mixed case (e.g., `/api/ES`, `/api/es`)
- **Hot Reload**: Spring DevTools for instant code changes without restart
- **Web UI**: Thymeleaf dashboard for testing all operations
- **Full CRUD**: Complete Create, Read, Update, Delete operations for all entities

## 🛠️ Tech Stack

- **Java 25**
- **Spring Boot 4.0**
- **Spring Data JPA**
- **Spring Web (REST)**
- **Spring DevTools (Hot Reload)**
- **PostgreSQL 18**
- **Lombok**
- **Thymeleaf**
- **Maven**
- **Testcontainers** (for integration tests)
- **Mockito** (for unit tests)

## 📦 Prerequisites

- **Java 25** installed and configured
- **PostgreSQL 18** installed and running
- **Maven** (local installation recommended; Maven Wrapper included)
- **IntelliJ IDEA** (or any IDE with Maven support)

### Install PostgreSQL 18

#### Windows
1. Download from [postgresql.org](https://www.postgresql.org/download/windows/)
2. Run the installer
3. Note the superuser password

#### macOS
```bash
brew install postgresql@18
brew services start postgresql@18
```

#### Linux (Ubuntu)
```bash
sudo apt update
sudo apt install postgresql-18
sudo systemctl start postgresql
```

## 🚀 Installation & Setup

### 1. Clone the Repository

```bash
cd /path/to/DaleelTeq-Booking-Simulation-Service
```

### 2. Create Environment File

Copy `.env.example` to `.env` and configure:

```bash
cp .env.example .env
```

Edit `.env`:
```env
DB_USERNAME=booking_user
DB_PASSWORD=changeme
```

### 3. Database Setup

#### Step 1: Create Database and User

Connect to PostgreSQL as superuser:

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

#### Step 2: Grant Schema Permissions (as postgres superuser)

```bash
psql -U postgres -d booking_db -f setup-db-permissions.sql
```

This grants the `booking_user` permission to create tables in the public schema.

#### Step 3: Create Schema (as booking_user)

```bash
psql -U booking_user -d booking_db -f src/main/resources/db/schema-postgres18.sql
```

The schema will:
- Create all tables with proper constraints
- Add indexes for performance
- Insert sample data (4 services, 3 employees, 3 clients)

#### Step 4: Verify Tables Created

```bash
psql -U booking_user -d booking_db -c "\dt"
```

Expected output: 6 tables (clients, employee_x_services, employees, es_notification, rendez_vous, services)

#### Step 5: Verify Sample Data

```bash
psql -U booking_user -d booking_db -c "SELECT COUNT(*) FROM services;"
```

Expected: **4** services

## ▶️ Running the Application

### Start the Application

```bash
mvn spring-boot:run
```

### Verify Application Started

Expected output:
```
[main] c.d.b.BookingApplication : Starting BookingApplication
[main] t.m.s.TomcatWebServer : Tomcat started on port(s): 8080 (http)
```

Access the Web UI: `http://localhost:8080`

### Alternative: Run JAR

```bash
mvn clean package
java -jar target/booking-simulation-service-1.0.0.jar
```

### Using IntelliJ IDEA

Right-click `BookingApplication.java` → "Run 'BookingApplication.main()'"

## 🌐 Accessing the Application

- **Web UI**: http://localhost:8080
- **API Base**: http://localhost:8080/api
- **H2 Console** (if enabled): http://localhost:8080/h2-console

## 📡 API Endpoints

### Services `/api/services`

```
GET    /api/services              - Get all services
GET    /api/services/{id}         - Get service by ID
POST   /api/services              - Create service
PUT    /api/services/{id}         - Update service by path ID
PUT    /api/services/entity       - Update service by body ID
DELETE /api/services/{id}         - Delete service
DELETE /api/services/clear        - Delete all services
```

Example POST:
```json
{
  "lib": "Haircut",
  "timeValue": 15
}
```

### Employees `/api/employees`

```
GET    /api/employees             - Get all employees
GET    /api/employees/{id}        - Get employee by ID
POST   /api/employees             - Create employee
PUT    /api/employees/{id}        - Update employee
PUT    /api/employees/entity      - Update employee by body ID
DELETE /api/employees/{id}        - Delete employee
DELETE /api/employees/clear       - Delete all employees
```

### Clients `/api/clients`

```
GET    /api/clients               - Get all clients
GET    /api/clients/{id}          - Get client by ID
POST   /api/clients               - Create client
PUT    /api/clients/{id}          - Update client
PUT    /api/clients/entity        - Update client by body ID
DELETE /api/clients/{id}          - Delete client
DELETE /api/clients/clear         - Delete all clients
```

Example POST:
```json
{
  "lib": "Client Name",
  "number": "+20123456789"
}
```

### Timeslots (ES) `/api/es`

```
GET    /api/es                    - Get all timeslots
GET    /api/es/{id}               - Get timeslot by ID
POST   /api/es                    - Create timeslot
PUT    /api/es/{id}               - Update timeslot
PUT    /api/es/entity             - Update timeslot by body ID
DELETE /api/es/{id}               - Delete timeslot
DELETE /api/es/clear              - Delete all timeslots
GET    /api/es/free/{date}        - Get free timeslots for date
```

Example POST:
```json
{
  "idE": 1,
  "idS": 1,
  "date": "2026-02-20",
  "start": "09:00",
  "x2": false
}
```

Server calculates:
- `end` = start + (serviceTimeValue * (x2 ? 2 : 1))
- `timeValue` = serviceTimeValue * (x2 ? 2 : 1)

Time validation: start must be between 09:00 and 16:00 - timeValue

### Rendez-vous (Bookings) `/api/rendezvous`

```
GET    /api/rendezvous            - Get all bookings
GET    /api/rendezvous/{id}       - Get booking by ID
POST   /api/rendezvous            - Book appointment
PATCH  /api/rendezvous/{id}/cancel - Cancel booking
DELETE /api/rendezvous/{id}       - Delete booking
DELETE /api/rendezvous/clear      - Delete all bookings
```

Example POST (Book):
```json
{
  "idES": 1,
  "idC": 1
}
```

Example PATCH (Cancel):
```json
{
  "by": "Client"
}
```

Cancellation updates:
- Sets ES.status back to `free`
- Creates Notification with type `cancelled`

### Notifications `/api/notifications`

```
GET    /api/notifications         - Get all notifications
GET    /api/notifications/{id}    - Get notification by ID
GET    /api/notifications/rendez-vous/{idR} - Get notifications for RV
GET    /api/notifications/type/{type}       - Get notifications by type
POST   /api/notifications         - Create notification (for testing)
DELETE /api/notifications/{id}    - Delete notification
DELETE /api/notifications/clear   - Delete all notifications
```

### Database Management

```
GET    /api/db-status             - Get count of records per table
DELETE /api/clear-db              - Clear entire database
```

Example DELETE /api/clear-db:
```json
{
  "confirm": true
}
```

## 🔧 Environment Configuration

### .env File

Create `.env` in project root (not committed to Git):

```env
# Database
DB_USERNAME=booking_user
DB_PASSWORD=changeme
SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/booking_db

# Time Window (optional, defaults shown)
APP_TIME_WINDOW_START=09:00
APP_TIME_WINDOW_END=16:00

# Spring Profile
SPRING_PROFILES_ACTIVE=dev
```

### application.properties

Key configurations:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/booking_db
spring.datasource.username=${DB_USERNAME:booking_user}
spring.datasource.password=${DB_PASSWORD:changeme}

spring.jpa.hibernate.ddl-auto=validate

# Hot Reload
spring.devtools.livereload.enabled=true
spring.devtools.restart.enabled=true

# Time Window
app.time-window.start=09:00
app.time-window.end=16:00
```

## 🧪 Testing

### Unit Tests

```bash
mvn test
```

Tests are located in `src/test/java/com/daleelteq/booking/`

### Integration Tests with Testcontainers

```bash
mvn verify
```

Uses Reusable Singleton pattern for PostgreSQL container:
- Database starts once per test suite
- Shared across all integration tests
- Significant performance improvement

### Skip Tests During Build

```bash
mvn clean package -DskipTests
```

## 📝 Error Response Examples

### Entity Not Found
```json
{
  "status": 404,
  "error": "Not Found",
  "message": "Service with id 999 not found. Available service ids: [1,2,3,4]"
}
```

### Validation Error - Invalid Time Window
```json
{
  "status": 422,
  "error": "Unprocessable Entity",
  "message": "Invalid time: end 16:15 exceeds allowed window 09:00–16:00 for timeValue 30. For a 30-minute slot, start must be between 09:00 and 15:30."
}
```

### Validation Error - Timeslot Already Booked
```json
{
  "status": 409,
  "error": "Conflict",
  "message": "Timeslot ES id 12 is already taken. Available free ES ids: [14,16,18]"
}
```

### Missing Required Field
```json
{
  "status": 400,
  "error": "Bad Request",
  "message": "Missing 'id' in request body for update. Available service ids: [1,2,3]"
}
```

## 🔄 Hot Reload

The application includes Spring DevTools for automatic reload:

1. Start the app: `mvn spring-boot:run`
2. Edit Java files, HTML templates, or `application.properties`
3. Changes reload automatically within 1-2 seconds
4. No need to restart the application

## 🗂️ Project Structure

```
src/
├── main/
│   ├── java/com/daleelteq/booking/
│   │   ├── BookingApplication.java
│   │   ├── config/
│   │   │   ├── WebConfig.java              (Case-insensitive routing)
│   │   │   ├── TimeWindowConfig.java       (Time window properties)
│   │   │   └── DotenvEnvironmentPostProcessor.java
│   │   ├── controller/                     (REST endpoints)
│   │   ├── domain/                         (JPA entities)
│   │   ├── dto/                            (Data transfer objects)
│   │   ├── exception/                      (Exception handlers)
│   │   ├── repository/                     (Data access layer)
│   │   └── service/                        (Business logic)
│   └── resources/
│       ├── application.properties
│       ├── db/schema-postgres18.sql
│       └── templates/index.html
└── test/
    └── java/com/daleelteq/booking/
        ├── service/                        (Unit tests)
        └── integration/                    (Integration tests)
```

## 📊 Data Model Summary

| Table | Columns | Purpose |
|-------|---------|---------|
| **services** | id, lib, time_value | Service definitions |
| **employees** | id, lib | Employee records |
| **clients** | id, lib, number | Client records |
| **employee_x_services** | id, id_e, id_s, date, start, end, time_value, x_2, status | Available timeslots |
| **rendez_vous** | id, id_es, id_c, status, created_at | Client bookings |
| **es_notification** | id, id_r, type, value, x_2, time_value | Booking audit trail |

## 🆘 Troubleshooting

### "Connection refused" to PostgreSQL
- Ensure PostgreSQL is running: `psql -U postgres`
- Check port 5432 is accessible
- Verify credentials in `.env`

### Maven build fails
- Ensure Java 25: `java -version`
- Clear cache: `mvn clean`
- Check Maven path: `mvn -v`

### IntelliJ doesn't recognize Maven
- Right-click `pom.xml` → "Add as Maven Project"
- File → Project Structure → Check SDK is Java 25

### Hot reload not working
- Check DevTools dependencies in `pom.xml`
- Ensure `spring.devtools.restart.enabled=true`
- Build project: Ctrl+Shift+F9 (IntelliJ)

## 📄 License

This project is part of the DaleelTeq system.

## 📞 Support

For issues or questions, refer to the project documentation or contact the development team.

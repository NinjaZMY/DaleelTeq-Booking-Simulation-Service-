# DaleelTeq Booking Simulation Service

A Spring Boot 4 REST API for managing appointment bookings with employee scheduling, timeslots, and client notifications.

## 📋 Table of Contents

- [Features](#-features)
- [Tech Stack](#-tech-stack)
- [Prerequisites](#-prerequisites)
- [Project Structure](#-project-structure)
- [Installation & Setup](#-installation--setup)
- [Maven Build Profiles](#-maven-build-profiles)
- [Running the Application](#-running-the-application)
- [Frontend Development (Angular)](#-frontend-development-angular)
- [API Endpoints](#-api-endpoints)
- [Environment Configuration](#-environment-configuration)
- [Testing](#-testing)
- [Troubleshooting](#-troubleshooting)

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

### Backend
- **Java 25**
- **Spring Boot 4.0**
- **Spring Data JPA**
- **Spring Web (REST)**
- **Spring DevTools (Hot Reload)**
- **PostgreSQL 18**
- **Lombok**
- **Thymeleaf** (legacy UI)
- **Maven**
- **Testcontainers** (for integration tests)
- **Mockito** (for unit tests)

### Frontend (Angular)
- **Angular 21.0.1**
- **TypeScript 5.6.3**
- **Node.js 24.11.1** (auto-installed by Maven)
- **npm 10.9.0** (auto-installed by Maven)
- **SCSS** for styling
- **HMR (Hot Module Reload)** enabled for development

## 📦 Prerequisites

### Backend Requirements
- **Java 25** installed and configured
- **PostgreSQL 18** installed and running
- **Maven** (local installation recommended; Maven Wrapper also available)
- **IntelliJ IDEA** (or any IDE with Maven support)

### Frontend Requirements (for Angular development)
- **Node.js 24.11.1** and **npm 10.9.0** (automatically installed by Maven during `npm-install` phase, or install manually if developing without Maven)
- **npm** for running `npm start` in `frontend/` directory

**Note**: During Maven build, Node.js and npm are automatically downloaded to `frontend/node/` directory.

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

## 📁 Project Structure

```
DaleelTeq-Booking-Simulation-Service/
├── frontend/                              # Angular 21 application
│   ├── src/
│   │   ├── app/
│   │   │   ├── app.component.ts
│   │   │   ├── app.component.html
│   │   │   ├── app.component.scss
│   │   │   ├── app.module.ts
│   │   │   ├── config/
│   │   │   │   └── app.config.ts
│   │   │   └── services/
│   │   │       └── api.service.ts
│   │   ├── styles.scss
│   │   ├── index.html
│   │   └── main.ts
│   ├── node/                              # Node.js/npm (auto-downloaded by Maven)
│   ├── angular.json
│   ├── tsconfig.json
│   ├── package.json
│   ├── proxy.conf.json                    # Backend API proxy config
│   └── .gitignore
├── src/
│   ├── main/
│   │   ├── java/com/daleelteq/booking/
│   │   │   ├── BookingApplication.java
│   │   │   ├── config/
│   │   │   │   ├── WebConfig.java        (Case-insensitive routing)
│   │   │   │   ├── TimeWindowConfig.java
│   │   │   │   └── DotenvEnvironmentPostProcessor.java
│   │   │   ├── controller/                # REST & Web controllers
│   │   │   ├── domain/                    # JPA entities
│   │   │   ├── dto/                       # Data transfer objects
│   │   │   ├── repository/                # Data access layer
│   │   │   ├── service/                   # Business logic
│   │   │   └── exception/                 # Exception handlers
│   │   ├── resources/
│   │   │   ├── application.properties
│   │   │   ├── application.yml
│   │   │   ├── db/
│   │   │   │   └── schema-postgres18.sql
│   │   │   ├── static/                    # Angular build output (production)
│   │   │   └── templates/
│   │   │       └── index.html             # Legacy Thymeleaf UI
│   │   └── ...
│   └── test/
│       └── java/com/daleelteq/booking/    # Unit & integration tests
├── .env.example                           # Environment template (DO NOT commit .env)
├── .gitignore
├── setup-db-permissions.sql
├── pom.xml                                # Maven config (includes Angular build)
└── README.md
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

## 🔨 Maven Build Profiles

This project uses **Maven profiles** to optimize build times during development and production deployments.

### Overview

| Profile | Command | Time | Angular Build | Use Case |
|---------|---------|------|---|---|
| **dev** (DEFAULT) | `mvn clean package -DskipTests` | ~28 seconds | ❌ Skipped | Local development & backend testing |
| **prod** | `mvn clean package -DskipTests -P prod` | ~5-10 minutes | ✅ Included | Production deployments |

### Development Build (Default)

**Fastest build** - Skip Angular compilation, perfect for backend development and testing.

```bash
# Standard command (dev profile active by default)
mvn clean package -DskipTests
```

**Or explicitly use dev profile:**
```bash
mvn clean package -DskipTests -P dev
```

**What happens:**
- ✅ Java backend compiles
- ❌ Angular build **SKIPPED**
- ❌ npm install **SKIPPED**
- ❌ Angular files **NOT** copied to static/
- 📦 JAR created with existing dist files (if available)

**Duration:** ~28 seconds

**Output:**
```
target/booking-simulation-service-1.0.0.jar
```

**When to use:**
- Local development
- Testing backend APIs
- Quick iteration on Java code
- Backend database/service changes
- Running unit/integration tests

**How to run:**
```bash
# Build
mvn clean package -DskipTests

# Run
java -jar target/booking-simulation-service-1.0.0.jar

# Access at: http://localhost:8080
```

### Production Build

**Full build** - Include Angular compilation for production deployment.

```bash
mvn clean package -DskipTests -P prod
```

**What happens:**
- ✅ Java backend compiles
- ✅ Node.js/npm installed (if needed)
- ✅ npm install runs
- ✅ Angular builds (`npm run build:prod`)
- ✅ Angular dist files copied to `src/main/resources/static/`
- 📦 JAR created with bundled Angular files

**Duration:** ~5-10 minutes (first build takes longer for dependencies)

**Output:**
```
target/booking-simulation-service-1.0.0.jar (with Angular bundled)
```

**When to use:**
- Production deployments
- Final releases
- When frontend and backend must be together
- CI/CD pipelines
- Docker image builds

**How to run:**
```bash
# Build
mvn clean package -DskipTests -P prod

# Run
java -jar target/booking-simulation-service-1.0.0.jar

# Access at: http://localhost:8080 (includes Angular UI)
```

### Development vs Production Comparison

#### Development Workflow
```bash
# Terminal 1: Build backend
mvn clean package -DskipTests
java -jar target/booking-simulation-service-1.0.0.jar

# Terminal 2: Develop Angular separately
cd frontend
npm run start  # HMR on port 4200
# Access Angular: http://localhost:4200 (proxies to backend)
```

**Benefits:**
- ⚡ Super fast builds (~28 sec)
- 🔄 Angular HMR for instant code refresh
- 🐛 Better error visibility in both console windows
- 🔧 Independent backend and frontend development
- 📊 Clear separation of concerns

#### Production Workflow
```bash
# Single command builds everything
mvn clean package -DskipTests -P prod
java -jar target/booking-simulation-service-1.0.0.jar

# Access at: http://localhost:8080
# Angular, backend, and UI all included
```

**Benefits:**
- 📦 Single JAR file deployment
- 🚀 No separate build steps
- 🔒 Optimized and minified
- 🌐 No CORS issues
- 🎯 Exactly what users get

### Switching Between Profiles

**Check active profile:**
```bash
mvn help:active-profiles
```

**Output:**
```
[INFO] Active Profiles for Project 'com.daleelteq:booking-simulation-service':
[INFO]   The following profiles are active:
[INFO]    - dev (source: pom.xml)
```

**Override profile:**
```bash
# Use production profile
mvn clean package -DskipTests -P prod

# Use development profile (explicit)
mvn clean package -DskipTests -P dev

# Multiple profiles (if needed)
mvn clean package -DskipTests -P dev,other-profile
```

### Frontend in Development

While using **dev profile**, manage Angular separately:

```bash
cd frontend

# Development server with HMR (auto-reload)
npm run start
# Access: http://localhost:4200

# Build when ready for production profile
npm run build:prod

# Production build for Maven
npm run build:prod
```

### Angular Build Output

- **Development build** (dev profile):
  - Uses existing `frontend/dist/` folder
  - No new build unless you run `npm run build:prod`

- **Production build** (prod profile):
  - Runs `npm run build:prod` automatically
  - Generates optimized, minified bundle
  - Copies to `src/main/resources/static/`
  - Included in final JAR

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

## 🅰️ Frontend Development (Angular)

### Option 1: Integrated Build (Maven handles everything)

This is the simplest for initial setup. Maven automatically builds Angular as part of the Spring Boot build.

```bash
# Build and run (Angular builds to static/)
mvn spring-boot:run
```

Access the application at:
- **Angular UI**: http://localhost:8080 (production build served by Spring Boot)
- **Legacy Thymeleaf UI**: http://localhost:8080/ui
- **API**: http://localhost:8080/api

**Note**: Changes to Angular code require rebuilding. Use Option 2 for faster development.

### Option 2: Separate Development Servers (Recommended for Development)

This setup uses HMR (Hot Module Reload) for instant code changes without rebuild/restart.

**Terminal 1: Start Spring Boot Backend**
```bash
mvn spring-boot:run
```
- Backend runs on **http://localhost:8080**
- API endpoints at **http://localhost:8080/api/**

**Terminal 2: Start Angular Dev Server with HMR**
```bash
cd frontend
npm install  # First time only
npm start
```
- Angular runs on **http://localhost:4200**
- Proxies API calls to `http://localhost:8080/api` (via `proxy.conf.json`)
- **HMR enabled**: Changes auto-reload instantly
- Console shows all TypeScript/build errors in real-time

### Fallback: Legacy Thymeleaf UI

If Angular has issues, the legacy Thymeleaf-based dashboard is always available:
- **URL**: http://localhost:8080/ui
- Complete CRUD interface for testing
- No JavaScript build required

### Angular Development Features

**HMR (Hot Module Reload)**:
- File changes auto-detect and recompile
- Page refreshes automatically in browser
- No manual restart needed
- Preserves component state during reload

**Error Console**:
```bash
# Terminal running `npm start` displays:
- TypeScript compilation errors
- Build warnings
- Runtime exceptions
- Network request logs
```

### Frontend Build Commands

```bash
cd frontend

# Development server with HMR (auto-reload)
npm start

# Production build (minified, optimized)
npm run build:prod

# Build specific to development
npm run build:dev

# Run unit tests
npm test

# Run linter
npm lint
```

### Frontend Structure

```
frontend/
├── src/
│   ├── app/
│   │   ├── app.component.ts       # Root component
│   │   ├── app.component.html     # Root template
│   │   ├── app.component.scss     # Component styles
│   │   ├── app.module.ts          # Module declarations
│   │   ├── config/
│   │   │   └── app.config.ts      # Configuration (API URLs, etc.)
│   │   └── services/
│   │       └── api.service.ts     # HTTP client for backend API
│   ├── styles.scss                # Global styles
│   ├── main.ts                    # Bootstrap file
│   └── index.html                 # Entry HTML
├── angular.json                   # Angular CLI config
├── tsconfig.json                  # TypeScript config
├── proxy.conf.json                # Dev server proxy to backend
├── package.json                   # npm dependencies
└── .gitignore
```

### API Proxy Configuration

**Development** (`npm start`):
- Angular dev server proxies `/api/*` requests to `http://localhost:8080`
- Configured in `frontend/proxy.conf.json`
- Allows development without CORS issues

**Production** (Maven build):
- Angular build output (`dist/`) copied to `src/main/resources/static/`
- Served by Spring Boot on same origin
- API calls go directly to `/api/*`

### Troubleshooting Angular Development

**Port 4200 already in use**:
```bash
# Use different port
ng serve --port 4201
```

**npm ci vs npm install**:
- Maven uses `npm install` to create `package-lock.json` on first run
- Subsequent builds can use `npm ci` for reproducible installs

**HMR not working**:
- Verify `npm start` is running in `frontend/` directory
- Check browser console for errors
- Kill `npm start` process and restart

**Angular build fails**:
```bash
# Clear cache and rebuild
rm -rf node_modules dist
npm install
npm run build:prod
```

## 🌐 Accessing the Application

- **Angular UI** (development): http://localhost:4200
- **Spring Boot** (backend + legacy UI): http://localhost:8080
- **Legacy Thymeleaf Dashboard**: http://localhost:8080/ui
- **API Base**: http://localhost:8080/api

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

## 🔀 API vs UI (Routing Conventions)

This project exposes two separate HTTP surfaces to avoid route collisions:

- API endpoints (machine-friendly JSON):
  - Base path: `/api`
  - Implemented by controllers under `src/main/java/com/daleelteq/booking/controller/rest`
  - Example: `GET /api/clients` returns JSON list of clients

- UI endpoints (browser-friendly views/Thymeleaf):
  - Base path: `/ui` (the main interactive dashboard)
  - Implemented by controllers under `src/main/java/com/daleelteq/booking/controller` and `.../controller/web`
  - Example: `GET /ui/home` shows the Thymeleaf dashboard

Important mapping notes:
- The application root `/` is handled by `WebIndexController` (located at `controller/web`) and populates the model used by `index.html`.
- Lightweight UI controllers use `/ui/*` to avoid accidentally shadowing `/` or any `/api/*` endpoints.
- If you are calling the API from Postman/scripts, always use the `/api` routes. Use `/ui` or `/` for browser-based testing.

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

### Backend (Spring Boot)

**"Connection refused" to PostgreSQL**
- Ensure PostgreSQL is running: `psql -U postgres`
- Check port 5432 is accessible
- Verify credentials in `.env`

**Maven build fails**
- Ensure Java 25: `java -version`
- Clear cache: `mvn clean`
- Check Maven path: `mvn -v`
- For `npm ci` errors: Maven now uses `npm install` by default

**IntelliJ doesn't recognize Maven**
- Right-click `pom.xml` → "Add as Maven Project"
- File → Project Structure → Check SDK is Java 25

**Spring Boot hot reload not working**
- Check DevTools dependencies in `pom.xml`
- Ensure `spring.devtools.restart.enabled=true` in application.properties
- Build project: Ctrl+Shift+F9 (IntelliJ)
- File changes should auto-reload within 1-2 seconds

**Port 8080 already in use**
```bash
# Find and kill process using port 8080
netstat -ano | findstr :8080
taskkill /PID <PID> /F
```

### Frontend (Angular)

**Port 4200 already in use**
```bash
# Use different port
cd frontend
npm start -- --port 4201
```

**Angular build fails during Maven build**
- Clear frontend cache: `rm -rf frontend/node_modules frontend/dist`
- Run Maven again: `mvn clean package`
- Check Node.js version: `frontend/node/node.exe --version` should be v24.11.1

**HMR (Hot Module Reload) not working**
- Verify `npm start` is running in `frontend/` directory
- Check browser console (F12) for errors
- Restart `npm start` process
- Check `frontend/proxy.conf.json` for correct backend URL

**npm ci fails**
- Ensure `package-lock.json` exists in `frontend/`
- If missing, run: `npm install` to generate it
- Maven uses `npm install` automatically

**Angular dev server not proxying API calls**
- Verify backend is running on http://localhost:8080
- Check `frontend/proxy.conf.json` has correct `target`
- Restart `npm start`
- Check network tab (F12) to see actual request URLs

### General

**`.env` file issues**
- Ensure `.env` exists in project root (not committed to Git)
- Check `.gitignore` includes `.env`
- Verify `DotenvEnvironmentPostProcessor` is loaded (check Spring logs)

**Database schema errors**
- Drop and recreate database:
  ```bash
  psql -U postgres -c "DROP DATABASE booking_db;"
  psql -U postgres -c "CREATE DATABASE booking_db;"
  psql -U postgres -d booking_db -f setup-db-permissions.sql
  psql -U booking_user -d booking_db -f src/main/resources/db/schema-postgres18.sql
  ```

**Random port access issues**
- Clear Maven cache: `rm -rf ~/.m2/repository` (Unix) or `%USERPROFILE%\.m2\repository` (Windows)
- Rebuild: `mvn clean install`

## 📄 License

This project is part of the DaleelTeq system.

## 📞 Support

For issues or questions, refer to the project documentation or contact the development team.

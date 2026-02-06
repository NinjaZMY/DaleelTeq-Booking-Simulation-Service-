# DaleelTeq Booking Simulation Service

A Spring Boot 4 REST API with Angular 21 frontend for managing appointment bookings with employee scheduling, timeslots, and client notifications.

## 📋 Table of Contents

- [Features](#features)
- [Tech Stack](#tech-stack)
- [Prerequisites](#prerequisites)
- [Project Structure](#project-structure)
- [Installation & Setup](#installation--setup)
- [Database Configuration](#database-configuration)
- [Running the Application](#running-the-application)
- [Frontend Development](#frontend-development)
- [API Endpoints](#api-endpoints)
- [Environment Configuration](#environment-configuration)
- [Testing](#testing)
- [Legacy UI](#legacy-ui)

## ✨ Features

- **Service Management**: Define available services with durations (15, 20, 25, 30 minutes)
- **Employee Management**: Manage employees and their schedules
- **Client Management**: Register and manage clients with contact information
- **Timeslot Management (ES)**: Create employee-service timeslots with date/time validation
- **Double Duration Support**: Enable `x_2` flag to double timeslot duration
- **Rendez-vous Booking**: Book appointments linking clients to timeslots
- **Notification System**: Automatic audit trail of booking/cancellation events
- **Time Window Validation**: Configurable working hours (09:00–16:00)
- **Precise Error Handling**: Detailed, actionable error messages with available IDs
- **Case-Insensitive Routing**: All routes accept mixed case (`/api/ES`, `/api/es`)
- **Hot Reload**: Spring DevTools + Angular HMR for instant feedback
- **Dual Frontend**:
  - **Angular 21** with HMR for development
  - **Thymeleaf** legacy UI at `/ui` for testing/fallback
- **Full CRUD**: Complete operations for all entities

## 🛠️ Tech Stack

### Backend
- **Java 25**
- **Spring Boot 4.0**
- **Spring Data JPA**
- **Spring Web (REST)**
- **Spring DevTools (Hot Reload)**
- **PostgreSQL 18**
- **Lombok**
- **Maven**
- **Testcontainers**
- **Mockito**

### Frontend (Angular)
- **Angular 21.0.1**
- **TypeScript 5.6.3**
- **Node.js 24.11.1**
- **npm 10.9.0**
- **HMR (Hot Module Reload) enabled**
- **SCSS for styling**

## 📦 Prerequisites

### Backend Requirements
- **Java 25** installed and configured
- **PostgreSQL 18** installed and running
- **Maven** (local installation recommended; Maven Wrapper also available)
- **IntelliJ IDEA** (or any IDE with Maven support)

### Frontend Requirements (for development)
- **Node.js 24.11.1** (installed automatically by Maven during build)
- **npm 10.9.0** (installed automatically by Maven during build)

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
├── frontend/                          # Angular 21 application
│   ├── src/
│   │   ├── app/
│   │   │   ├── config/
│   │   │   │   └── app.config.ts
│   │   │   ├── services/
│   │   │   │   └── api.service.ts
│   │   │   ├── app.component.ts
│   │   │   ├── app.component.html
│   │   │   ├── app.component.scss
│   │   │   └── app.module.ts
│   │   ├── styles.scss
│   │   ├── index.html
│   │   └── main.ts
│   ├── angular.json
│   ├── tsconfig.json
│   ├── package.json
│   ├── proxy.conf.json              # Backend API proxy config
│   └── .gitignore
├── src/
│   ├── main/
│   │   ├── java/com/daleelteq/booking/
│   │   │   ├── BookingApplication.java
│   │   │   ├── config/
│   │   │   ├── controller/
│   │   │   ├── domain/
│   │   │   ├── dto/
│   │   │   ├── repository/
│   │   │   ├── service/
│   │   │   └── exception/
│   │   ├── resources/
│   │   │   ├── application.properties
│   │   │   ├── application.yml
│   │   │   ├── db/
│   │   │   │   └── schema-postgres18.sql
│   │   │   ├── static/              # Angular build output goes here
│   │   │   └── templates/
│   │   │       └── index.html       # Legacy Thymeleaf UI
│   │   └── ...
│   └── test/
├── pom.xml                            # Maven config (includes Angular build)
├── .env.example                       # Environment template
├── .gitignore
└── README.md
```

## 🚀 Installation & Setup

### 1. Clone the Repository

```bash
cd /path/to/DaleelTeq-Booking-Simulation-Service
```

### 2. Create Environment File

```bash
cp .env.example .env
```

Edit `.env` and set database credentials:
```env
DB_USERNAME=booking_user
DB_PASSWORD=changeme
```

**⚠️ Important**: Add `.env` to `.gitignore` to prevent pushing credentials.

## 🗄️ Database Configuration

### Step 1: Create Database and User (PostgreSQL Superuser)

```bash
psql -U postgres
```

```sql
CREATE DATABASE booking_db;
CREATE USER booking_user WITH PASSWORD 'changeme';
GRANT ALL PRIVILEGES ON DATABASE booking_db TO booking_user;
\q
```

### Step 2: Grant Schema Permissions

```bash
psql -U postgres -d booking_db -f setup-db-permissions.sql
```

### Step 3: Load Database Schema

```bash
psql -U booking_user -d booking_db -f src/main/resources/db/schema-postgres18.sql
```

### Step 4: Verify Tables

```bash
psql -U booking_user -d booking_db -c "\dt"
```

Expected output should show tables: `clients`, `employees`, `services`, `employee_x_services`, `rendez_vous`, `es_notification`

## ▶️ Running the Application

### Option A: Using Maven (Builds Angular + Backend)

**Production Build**:
```bash
mvn clean package
java -jar target/booking-simulation-service-1.0.0.jar
```

**Development Mode** (Watch mode):
```bash
mvn spring-boot:run
```

The app will start on **http://localhost:8080**

### Option B: Separate Development Servers (Recommended for Development)

#### Terminal 1: Start Spring Boot Backend
```bash
mvn spring-boot:run
```
- Backend runs on **http://localhost:8080**
- API endpoints available at **http://localhost:8080/api/**

#### Terminal 2: Start Angular Dev Server with HMR
```bash
cd frontend
npm install  # Only if node_modules not present
npm start
```
- Angular runs on **http://localhost:4200**
- Automatically proxies API calls to `http://localhost:8080/api`
- **HMR enabled**: Changes auto-reload instantly
- Console shows all errors in real-time

## 🅰️ Frontend Development

### Angular Development Server Setup

**1. Prerequisites**
- Node.js 24.11.1 and npm 10.9.0 (or use Maven to install)

**2. First Time Setup**
```bash
cd frontend
npm install
```

**3. Start Dev Server with HMR**
```bash
npm start
```

The dev server will open on **http://localhost:4200**

**4. Features**
- ✅ **HMR Enabled**: Changes hot-reload automatically
- ✅ **Error Detection**: Console errors display immediately in terminal
- ✅ **API Proxy**: `/api/*` routes proxy to Spring Boot backend
- ✅ **Live Recompile**: TypeScript auto-compiles on save
- ✅ **Browser DevTools**: Full Angular DevTools support

**5. Build for Production**
```bash
npm run build:prod
```
Output goes to `dist/booking-simulation-frontend/` and gets copied to `src/main/resources/static/` during Maven build.

### Angular File Structure
```
frontend/src/
├── app/
│   ├── app.component.ts        # Root component
│   ├── app.component.html      # Root template
│   ├── app.component.scss      # Component styles
│   ├── app.module.ts           # Module declarations
│   ├── config/
│   │   └── app.config.ts       # Configuration (API URLs, time windows)
│   └── services/
│       └── api.service.ts      # HTTP client for backend API
├── styles.scss                  # Global styles
├── main.ts                       # Bootstrap file
└── index.html                    # Entry HTML
```

### Important: API Proxy Configuration

**Development** (npm start):
- Angular dev server proxies all `/api/*` requests to `http://localhost:8080` (Spring Boot)
- See `frontend/proxy.conf.json`

**Production** (Maven build):
- Angular dist is copied into Spring Boot's `static/` folder
- API calls go directly to same origin (no proxy needed)

## 📡 API Endpoints

All endpoints accept case-insensitive routing (e.g., `/api/ES`, `/api/es`).

### Services (`/api/services`)
- `GET /api/services` - List all services
- `GET /api/services/:id` - Get service by ID
- `POST /api/services` - Create service
- `PUT /api/services/:id` - Update service
- `DELETE /api/services/:id` - Delete service
- `DELETE /api/services/clear` - Delete all services

### Employees (`/api/employees`)
- `GET /api/employees` - List all employees
- `GET /api/employees/:id` - Get employee by ID
- `POST /api/employees` - Create employee
- `PUT /api/employees/:id` - Update employee
- `DELETE /api/employees/:id` - Delete employee
- `DELETE /api/employees/clear` - Delete all employees

### Clients (`/api/clients`)
- `GET /api/clients` - List all clients
- `GET /api/clients/:id` - Get client by ID
- `POST /api/clients` - Create client (requires: lib, number)
- `PUT /api/clients/:id` - Update client
- `DELETE /api/clients/:id` - Delete client
- `DELETE /api/clients/clear` - Delete all clients

### Employee-Service Timeslots (ES) (`/api/es`)
- `GET /api/es` - List all timeslots
- `GET /api/es/:id` - Get timeslot by ID
- `POST /api/es` - Create timeslot
- `PUT /api/es/:id` - Update timeslot
- `DELETE /api/es/:id` - Delete timeslot
- `DELETE /api/es/clear` - Delete all timeslots

### Rendez-vous (`/api/rendezvous`)
- `GET /api/rendezvous` - List all appointments
- `GET /api/rendezvous/:id` - Get appointment by ID
- `POST /api/rendezvous` - Book appointment
- `PATCH /api/rendezvous/:id/cancel` - Cancel appointment
- `DELETE /api/rendezvous/:id` - Delete appointment
- `DELETE /api/rendezvous/clear` - Delete all appointments

### Notifications (`/api/notifications`)
- `GET /api/notifications` - List all notifications
- `GET /api/notifications/:id` - Get notification by ID
- `POST /api/notifications` - Create notification
- `DELETE /api/notifications/:id` - Delete notification
- `DELETE /api/notifications/clear` - Delete all notifications

### Database Control (`/api`)
- `DELETE /api/clear-db` - Delete all data (caution: truncates all tables)

## 🔐 Environment Configuration

### `.env` File Template

```env
# Database Configuration
DB_USERNAME=booking_user
DB_PASSWORD=changeme

# Time Window (24-hour format)
START_HOUR=09
END_HOUR=16
```

### Application Properties

Edit `src/main/resources/application.yml`:

```yaml
spring:
  devtools:
    livereload:
      enabled: true
    restart:
      enabled: true
      additional-paths: src/,resources/application.properties
  datasource:
    url: jdbc:postgresql://localhost:5432/booking_db
    username: ${DB_USERNAME:booking_user}
    password: ${DB_PASSWORD:changeme}
```

## 🧪 Testing

### Run Unit Tests
```bash
mvn test
```

### Run Integration Tests (with Testcontainers)
```bash
mvn verify
```

### Test via UI
1. **Angular Dev UI** (http://localhost:4200):
   - Full interactive dashboard
   - Real-time error console
   - HMR auto-refresh on code changes

2. **Legacy Thymeleaf UI** (http://localhost:8080/ui):
   - Fallback testing interface
   - Available at `/ui` endpoint

3. **Direct API Testing**:
   ```bash
   # List all services
   curl -X GET http://localhost:8080/api/services
   
   # Create service
   curl -X POST http://localhost:8080/api/services \
     -H "Content-Type: application/json" \
     -d '{"lib":"Consultation","timeValue":30}'
   ```

## 🌐 Legacy UI

The original Thymeleaf-based UI is preserved for testing and fallback purposes.

**Access URL**: http://localhost:8080/ui

**Features**:
- Complete CRUD interface for all entities
- Real-time data refresh
- Modal-based interactions
- JSON API tester embedded
- Direct database control buttons

## 📝 Notes

- **Hot Reload**: Both Spring Boot (DevTools) and Angular (HMR) support instant recompilation. Changes are reflected without restarting.
- **Error Handling**: Comprehensive error messages include available IDs and validation hints.
- **API Response Format**: All endpoints return wrapped responses:
  ```json
  {
    "success": true,
    "data": [...],
    "message": "Operation successful",
    "error": null,
    "details": null
  }
  ```
- **Time Validation**: All times must be within configured window (default 09:00–16:00).
- **Database Credentials**: Store in `.env` file and never commit to version control.

## 📖 Additional Resources

- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [Angular Documentation](https://angular.io/docs)
- [PostgreSQL Documentation](https://www.postgresql.org/docs/)
- [Maven Documentation](https://maven.apache.org/guides/)

## 📄 License

Internal Project - All Rights Reserved

# Angular Frontend Setup & Development Guide

## ✅ Quick Start (5 minutes)

### 1. Prerequisites Check
```bash
# Check Java version
java -version  # Should be 25

# Check Maven
mvn -v  # Should be 3.x

# Check PostgreSQL
psql -V  # Should be 14+
```

### 2. Environment Setup
```bash
# In project root
cp .env.example .env

# Edit .env with your database credentials
# DB_USERNAME=booking_user
# DB_PASSWORD=changeme
```

### 3. Database Setup
```bash
# As PostgreSQL superuser
psql -U postgres -f setup-db-permissions.sql
psql -U booking_user -d booking_db -f src/main/resources/db/schema-postgres18.sql
```

### 4. Run Both Servers

**Terminal 1 - Spring Boot Backend**:
```bash
mvn spring-boot:run
```
Runs on http://localhost:8080

**Terminal 2 - Angular Dev Server**:
```bash
cd frontend
npm install  # First time only
npm start
```
Runs on http://localhost:4200 with HMR enabled

## 🎯 Angular Development

### Access Points
- **New Angular UI**: http://localhost:4200 (Hot reload enabled)
- **Legacy Thymeleaf UI**: http://localhost:8080/ui (Fallback)
- **API Backend**: http://localhost:8080/api/*

### HMR (Hot Module Reload)
When running `npm start` in `frontend/` directory:
- ✅ Changes to `.ts`, `.html`, `.scss` files auto-reload
- ✅ Component state is preserved during reload
- ✅ Errors display immediately in terminal

### API Proxy Configuration
The Angular dev server automatically proxies API calls:
- Any request to `/api/*` is forwarded to `http://localhost:8080`
- No CORS issues during development
- See `frontend/proxy.conf.json` for details

### Build for Production
```bash
cd frontend
npm run build:prod
```
Output: `frontend/dist/booking-simulation-frontend/`
This gets copied into Spring Boot's `static/` folder during Maven build.

## 🔧 Development Workflow

### 1. Create a New Component
```bash
cd frontend
ng generate component components/my-feature
```

### 2. Add a Service
```bash
ng generate service services/my-service
```

### 3. Create Modules
```bash
ng generate module modules/my-module
```

### 4. Development Commands
```bash
# Start dev server with HMR
npm start

# Run unit tests
npm test

# Build production
npm run build:prod

# Watch mode for development
npm run watch
```

## 📦 Current Directory Structure

```
frontend/
├── src/
│   ├── app/
│   │   ├── app.component.ts        ← Root component
│   │   ├── app.component.html      ← Root template
│   │   ├── app.component.scss      ← Root styles
│   │   ├── app.module.ts           ← Root module
│   │   ├── config/
│   │   │   └── app.config.ts       ← Configuration (API base, time window)
│   │   └── services/
│   │       └── api.service.ts      ← HTTP service for backend API calls
│   ├── index.html                  ← Angular entry point
│   ├── main.ts                     ← Bootstrap
│   └── styles.scss                 ← Global styles
├── angular.json                    ← Angular CLI config (includes HMR setup)
├── proxy.conf.json                 ← Dev server API proxy
├── package.json                    ← Dependencies
├── tsconfig.json                   ← TypeScript config
├── .gitignore                      ← Git ignore for node_modules, dist, etc.
└── .angular/                       ← Angular cache (auto-generated)
```

## 🌐 API Service Usage

The `ApiService` is a wrapper around fetch API for convenient API calls:

```typescript
import { ApiService } from './services/api.service';

constructor(private api: ApiService) { }

// GET request
this.api.get('/services')
  .then(response => {
    // response.data contains the array
    console.log(response.data);
  })
  .catch(error => console.error(error));

// POST request
this.api.post('/services', { lib: 'Consultation', timeValue: 30 })
  .then(response => console.log(response))
  .catch(error => console.error(error));

// PUT request
this.api.put('/services/1', { lib: 'Updated Service', timeValue: 45 })
  .then(response => console.log(response))
  .catch(error => console.error(error));

// DELETE request
this.api.delete('/services/1')
  .then(response => console.log(response))
  .catch(error => console.error(error));
```

## 🐛 Debugging

### Angular Console Errors
When running `npm start`, all errors are logged to the terminal immediately. No need to open browser DevTools.

Example error output:
```
ERROR in ./src/app/my.component.ts:15:2
Property 'unknown' does not exist on type 'MyComponent'
```

### Browser Console
Chrome DevTools will also show errors in the **Console** tab.

### VS Code Extensions (Recommended)
- **Angular Language Service**: Syntax highlighting, IntelliSense
- **Angular Snippets**: Code generation snippets
- **REST Client**: Test API endpoints without Postman

## 📡 API Response Format

All backend endpoints return a wrapped response:

```json
{
  "success": true,
  "data": [],
  "message": "Operation successful",
  "error": null,
  "details": null
}
```

Access the array in components:
```typescript
this.api.get('/services').then(response => {
  const services = response.data;  // ← The actual array
  // process services...
});
```

## 🚀 Production Deployment

### Option 1: Maven Build (Recommended)
```bash
# In project root
mvn clean package

# Start production server
java -jar target/booking-simulation-service-1.0.0.jar
```

The Angular app is embedded in the JAR and served from `/`.

### Option 2: Separate Deployment
1. Build Angular: `cd frontend && npm run build:prod`
2. Build Backend: `mvn clean package -DskipFrontend` (skip Angular rebuild)
3. Deploy backend JAR and frontend dist separately

## 🔑 Environment Variables

### Frontend `.env` (if needed)
Create `frontend/.env.local` for local overrides:
```env
NG_APP_API_BASE=http://localhost:8080/api
NG_APP_TIMEOUT=30000
```

### Backend `.env`
See project root `.env.example`

## 📚 Learning Resources

- [Angular Official Docs](https://angular.io/docs)
- [Angular CLI Guide](https://angular.io/cli)
- [TypeScript Handbook](https://www.typescriptlang.org/docs/)
- [RxJS Documentation](https://rxjs.dev/)

## ⚠️ Common Issues

### Issue: `npm command not found`
**Solution**: Install Node.js 24.11.1 or let Maven install it (`mvn install`)

### Issue: `Port 4200 already in use`
**Solution**: 
```bash
# Change port
ng serve --port 4201

# Or kill process on 4200
lsof -ti:4200 | xargs kill -9  # macOS/Linux
netstat -ano | findstr :4200   # Windows (find PID, then taskkill)
```

### Issue: API calls fail with 404
**Solution**: Ensure Spring Boot is running on port 8080 and `proxy.conf.json` is correct

### Issue: HMR not working
**Solution**: Check that `ng serve` includes `--poll 2000` flag (it does by default in `package.json`)

## ✅ Verification Checklist

- [ ] Backend running on http://localhost:8080 (`mvn spring-boot:run`)
- [ ] Frontend running on http://localhost:4200 (`npm start` in `frontend/`)
- [ ] API requests work in browser console:
  ```javascript
  fetch('/api/services').then(r => r.json()).then(d => console.log(d.data))
  ```
- [ ] HMR working (edit `app.component.html`, page auto-reloads)
- [ ] Console shows no errors
- [ ] Legacy UI accessible at http://localhost:8080/ui

## 📞 Support

For issues:
1. Check console output (`npm start` terminal for Angular, Maven for backend)
2. Check browser DevTools → Console tab
3. Verify all prerequisites are installed
4. Check database connectivity: `psql -U booking_user -d booking_db -c "SELECT 1"`

---

**Next Steps**:
1. [Read the main README.md](./README.md) for architecture overview
2. Start both servers as described above
3. Access http://localhost:4200 and test the UI
4. Refer to `REST_CONTROLLERS_IMPLEMENTATION.md` for API details

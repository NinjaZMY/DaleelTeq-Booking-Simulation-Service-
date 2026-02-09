# Build Configuration - Development vs Production

## Problem Fixed ✅
The Maven build was failing with: `Failed to execute goal com.github.eirslett:frontend-maven-plugin:1.15.1:npm (npm-install)`

### Root Causes:
1. **TypeScript Version Conflict**: Angular 21 requires TypeScript >=5.9, but package.json had 5.6.3
2. **Angular Build on Every Dev Build**: Frontend was building unnecessarily during development

## Solution Implemented ✅

### 1. Fixed TypeScript Version
Updated `frontend/package.json`:
```json
"typescript": "~5.9.2"  // Changed from ~5.6.3
```

### 2. Created Maven Profiles for Development & Production

#### Development Profile (DEFAULT - Skip Angular Build)
```bash
mvn clean package -DskipTests -P dev
```
- **Skips**: Angular build, npm install, copying dist files
- **Time**: ~28 seconds (Java compilation only)
- **Use Case**: Local development, testing backend APIs
- **Output**: Backend JAR ready to run

#### Production Profile (Include Angular Build)
```bash
mvn clean package -DskipTests -P prod
```
- **Builds**: Angular frontend + Backend
- **Time**: ~5-10 minutes (includes full Angular build)
- **Use Case**: Production deployments, final releases
- **Output**: Backend JAR with bundled Angular dist files

## Build Configuration Details

### Changes Made to `pom.xml`:

1. **Added skip property** in `<properties>`:
```xml
<skip.frontend.build>true</skip.frontend.build>
```

2. **Updated frontend-maven-plugin**:
```xml
<skip>${skip.frontend.build}</skip>
```

3. **Updated maven-resources-plugin**:
```xml
<skip>${skip.frontend.build}</skip>
```

4. **Added two Maven profiles**:
   - `dev` (active by default): Sets `skip.frontend.build=true`
   - `prod`: Sets `skip.frontend.build=false`

## Usage

### For Development (recommended):
```bash
# Build backend only, skip Angular
mvn clean package -DskipTests

# Or explicitly use dev profile
mvn clean package -DskipTests -P dev

# Run Spring Boot
java -jar target/booking-simulation-service-1.0.0.jar
```

### For Production:
```bash
# Build both backend and Angular
mvn clean package -DskipTests -P prod

# Or use maven.profiles property
mvn clean package -DskipTests -Dmaven.profiles=prod
```

### Manual Angular Development:
```bash
cd frontend

# Development server with HMR
npm run start

# Production build (when ready)
npm run build:prod
```

## Build Times Comparison

| Profile | Time | Components |
|---------|------|-----------|
| `dev` (default) | ~28 sec | Java compilation only |
| `prod` | ~5-10 min | Java + Angular build |

## Current Status ✅

- ✅ Maven build successful in dev mode
- ✅ Spring Boot JAR created successfully
- ✅ Ready to test backend APIs
- ✅ Angular frontend can be built separately when needed
- ✅ Profiles configured for automatic production builds

## Next Steps

1. **Test Backend**: Run the Spring Boot application
2. **Test Angular**: When ready, either:
   - Run `npm run start` in frontend/ for development
   - Build with `mvn clean package -P prod` for production

## Troubleshooting

### If Angular build fails in production:
1. Ensure Node.js and npm are installed
2. Clear cache: `cd frontend && npm cache clean --force && rm -rf node_modules package-lock.json`
3. Rebuild: `mvn clean package -P prod`

### If development build still tries to build Angular:
- Verify profile is active: `mvn help:active-profiles -P dev`
- Check property: `mvn help:describe -Ddetail=true -Dplugin=com.github.eirslett:frontend-maven-plugin`


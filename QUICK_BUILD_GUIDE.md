# Quick Build Commands

## Development (DEFAULT - No Angular Build)
```bash
mvn clean package -DskipTests
# Takes ~28 seconds, builds backend only
```

## Production (With Angular Build)
```bash
mvn clean package -DskipTests -P prod
# Takes ~5-10 minutes, builds backend + Angular
```

## Run the Application
```bash
java -jar target/booking-simulation-service-1.0.0.jar
```

## Angular Development (Separate from Maven)
```bash
cd frontend
npm run start
# Starts Angular dev server on port 4200 with HMR
```

## Angular Production Build (Separate)
```bash
cd frontend
npm run build:prod
# Creates dist folder for Maven to package
```

## Check Active Profile
```bash
mvn help:active-profiles -P dev
```

---

**Key Points:**
- Default build skips Angular (dev profile active by default)
- Add `-P prod` to include Angular in Maven build
- Angular can be developed independently with `npm run start`
- Use dev profile for quick backend testing
- Use prod profile for final deployments


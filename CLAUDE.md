# CLAUDE.md

Full-stack app that loads Excel data into a Postgres database and lets users search it.

## Layout
- `excel-database-loader-api/`: Spring Boot (Java 25) backend, Maven, Liquibase, Apache POI, Testcontainers.
- `excel-database-loader-ui/`: React + Vite + Tailwind frontend (npm, vitest, eslint).
- Root `pom.xml` is a Maven aggregator; the UI module runs `npm install` and `npm run build` through exec-maven-plugin.

## Commands
- Full build and tests: `mvn -B clean verify` (the `ExcelDatabaseLoaderApplicationTests` context test needs Docker for Testcontainers).
- UI: `cd excel-database-loader-ui && npm ci && npm run build` (prebuild runs vitest and eslint), `npm test`, `npm run lint`.
- Docker: `docker-compose.yml`, `build_images.sh`.

## Conventions
- UI formatting via prettier (4-space indent, single quotes, semicolons).
- eslint is kept on 9.x because eslint-plugin-react does not yet support eslint 10.

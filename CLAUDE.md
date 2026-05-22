# Property Dealer- Claude Settings

## Project Overview
This is a Spring Boot microservices HR CRM systemproperty management application for real estate agencies.

- Right now it's a single microservice and will be enhanced into multi module applciation 
- Parent Maven project with 5 microservices
- Each service will independent, owns its own DB schema
- Services communicate via REST (no shared DB joins across services)
- All ports: check the existing application.properties files for assigned ports (8081-8085)
- this project uses theamleader as the base, but we will be adding more services and features on top of it, so expect some divergence over time.

## Non-negotiable conventions
- Java 17, Spring Boot 3.2.x
- Constructor injection ONLY — no @Autowired on fields, ever
- comman UUID generator will be added and before saving any entity, we will set the id using the generator.
- All entities use UUID id 
- Liquibase for DB migrations — files named V{n}__{description}.xml, e.g. V1__create_lead_pipeline_table.xml
- DTOs separate from entities — never return JPA entities from controllers
- Global exception handler in every service — no raw 500s to clients
- Every public service method needs a unit test


## Database
- h2 for development, Neon for production
- Each service has its own schema
- Naming: snake_case tables, e.g. `lead_pipeline`, `call_recordings`
- Migrations: Liquibase, files in `src/main/resources/db/`
- Never write raw SQL — use Spring Data JPA repositories

## Testing rules
- Every service endpoint needs an integration test using @SpringBootTest
- Mock the Claude API calls using WireMock in tests
- Run `./mvnw test` before asking me to review any PRs

## Environment variables

[//]: # (- `ANTHROPIC_API_KEY` — Claude API key &#40;never hardcode, never log&#41;)
- `DB_URL`, `DB_USER`, `DB_PASS` — NEON connection

[//]: # (- `TWILIO_ACCOUNT_SID`, `TWILIO_AUTH_TOKEN` — calling service)

[//]: # (- `WHATSAPP_TOKEN` — Meta WhatsApp API token)

## What NOT to do
- Do not create new services without updating the parent pom.xml
- Do not use `@Autowired` field injection — use constructors
- Do not add dependencies without checking if they conflict with Spring BOM
- Do not call the Claude API synchronously in a web request thread — use async/reactive 
# domain-common-notifications

Domain layer microservice for multi-channel notification orchestration. This service manages the full notification lifecycle -- routing, delivery (email via SendGrid, SMS via Twilio), auditing, read-status tracking, deletion, and per-party preference management -- within the Firefly platform.

Repository: [https://github.com/firefly-oss/domain-common-notifications](https://github.com/firefly-oss/domain-common-notifications)

## Table of Contents

- [Overview](#overview)
- [Architecture](#architecture)
- [Setup](#setup)
- [API Endpoints](#api-endpoints)
- [SDK](#sdk)
- [Testing](#testing)

---

## Overview

`domain-common-notifications` provides a reactive REST API for sending notifications through email and SMS channels, querying notification history per party, managing read/unread state, and storing per-party channel preferences. Each send operation is orchestrated via a multi-step saga that routes the message to the correct channel, delivers it through the configured provider, and records an audit trail in the core platform.

### Key Features

- **Multi-channel delivery** -- sends notifications via email (SendGrid) or SMS (Twilio), with automatic channel selection when set to `AUTO`.
- **Saga-orchestrated workflows** -- the `SendNotificationSaga` routes, delivers, and audits each notification using the FireflyFramework Transactional Saga Engine with retry policies and compensating actions.
- **Bulk send** -- dispatches multiple notifications in a single request, executing a saga per message.
- **Notification lifecycle management** -- retrieve notifications by party (paginated), fetch individual notification details, mark single or all notifications as read, and delete notifications.
- **Notification preferences** -- retrieve and update per-party channel preferences.
- **Event-driven architecture** -- publishes domain events (`notification.delivered`, `notification.sent`) via Kafka through the FireflyFramework EDA module.
- **CQRS** -- separates commands (send, mark-read, delete, update preferences) from queries (get by party, get detail, get preferences) through dedicated command and query buses.
- **Reactive, non-blocking architecture** -- built on Spring WebFlux and Project Reactor for high-throughput, low-latency processing.
- **OpenAPI documentation** -- integrated Swagger UI via SpringDoc for interactive API exploration.
- **Observability** -- Prometheus metrics via Micrometer and Spring Boot Actuator health/readiness/liveness endpoints.

---

## Architecture

### Module Structure

| Module | Purpose |
|--------|---------|
| `domain-common-notifications-core` | Business logic: command/query handlers, service interfaces/implementations, saga workflows, domain models, and constants. |
| `domain-common-notifications-interfaces` | Interface adapters: bridges between web layer and core domain; depends on core. |
| `domain-common-notifications-infra` | Infrastructure: configuration properties, external SDK client factory (email/SMS APIs), provider integrations (SendGrid, Twilio). |
| `domain-common-notifications-web` | Deployable Spring Boot application: REST controllers, OpenAPI config, actuator endpoints. |
| `domain-common-notifications-sdk` | Auto-generated client SDK for consumers of this service's API, produced from the OpenAPI spec via `openapi-generator-maven-plugin`. |

### Dependency Flow

```
web --> interfaces --> core --> infra
```

### Technology Stack

| Technology | Purpose |
|------------|---------|
| Java 25 | Language runtime |
| Spring Boot (WebFlux) | Reactive web framework |
| Project Reactor | Reactive streams |
| [FireflyFramework Parent](https://github.com/fireflyframework/) | Parent POM and dependency management |
| [FireflyFramework Transactional Saga Engine](https://github.com/fireflyframework/) | Distributed saga orchestration (`SagaResult`, `@Saga`, `@SagaStep`) |
| [FireflyFramework CQRS](https://github.com/fireflyframework/) | Command and query bus (`CommandBus`, `QueryBus`, `@CommandHandlerComponent`, `@QueryHandlerComponent`) |
| [FireflyFramework Notifications Core](https://github.com/fireflyframework/) | Notification provider abstractions (`EmailProvider`, `SMSProvider`) |
| [FireflyFramework Notifications SendGrid](https://github.com/fireflyframework/) | SendGrid email provider implementation |
| [FireflyFramework Notifications Twilio](https://github.com/fireflyframework/) | Twilio SMS provider implementation |
| [FireflyFramework EDA](https://github.com/fireflyframework/) | Event-driven architecture with Kafka publishers |
| [FireflyFramework Utils](https://github.com/fireflyframework/) | Common utilities |
| [FireflyFramework Domain](https://github.com/fireflyframework/) | Domain abstractions (`fireflyframework-starter-domain`) |
| [FireflyFramework Validators](https://github.com/fireflyframework/) | Validation utilities |
| [FireflyFramework Web](https://github.com/fireflyframework/) | Common web configurations |
| core-common-notification-services-sdk | SDK client for the core Notification Services platform (audit APIs) |
| SpringDoc OpenAPI (WebFlux UI) | API documentation |
| Micrometer + Prometheus | Metrics and monitoring |
| OpenAPI Generator | SDK code generation from OpenAPI spec |
| MapStruct | Object mapping |
| Lombok | Boilerplate reduction |

---

## Setup

### Prerequisites

- **Java 25** (or later)
- **Apache Maven 3.9+**
- Access to the FireflyFramework Maven repository for `org.fireflyframework` dependencies
- Access to the `core-common-notification-services-sdk` artifact
- A running Kafka broker (default: `localhost:9092`) for event publishing
- SendGrid API key and/or Twilio credentials for outbound delivery

### Environment Variables

| Variable | Default | Description |
|----------|---------|-------------|
| `SERVER_ADDRESS` | `localhost` | Address the server binds to |
| `SERVER_PORT` | `8080` | HTTP port |
| `SENDGRID_API_KEY` | *(empty)* | SendGrid API key for email delivery |
| `TWILIO_ACCOUNT_SID` | *(empty)* | Twilio account SID for SMS delivery |
| `TWILIO_AUTH_TOKEN` | *(empty)* | Twilio auth token for SMS delivery |
| `TWILIO_PHONE_NUMBER` | *(empty)* | Twilio sender phone number for SMS |

Additional configuration is managed in `application.yaml`:

| Property | Default | Description |
|----------|---------|-------------|
| `firefly.cqrs.command.timeout` | `30s` | Command bus execution timeout |
| `firefly.cqrs.query.timeout` | `15s` | Query bus execution timeout |
| `firefly.cqrs.query.cache-ttl` | `15m` | Query result cache TTL |
| `firefly.notifications.email.provider` | `sendgrid` | Email delivery provider |
| `firefly.notifications.sms.provider` | `twilio` | SMS delivery provider |
| `api-configuration.common-platform.notification-services.base-path` | `http://localhost:8095` | Base URL for core notification services SDK |

### Build

```bash
# Full build
mvn clean install

# Skip tests
mvn clean install -DskipTests
```

### Run

```bash
# Run via Spring Boot Maven plugin
mvn -pl domain-common-notifications-web spring-boot:run

# Or run the packaged JAR
java -jar domain-common-notifications-web/target/domain-common-notifications.jar
```

---

## API Endpoints

All endpoints return reactive responses. The base API path prefix is `/api/v1/notifications`.

### Notification Endpoints (`/api/v1/notifications`)

| Method | Path | Summary | Description |
|--------|------|---------|-------------|
| `POST` | `/api/v1/notifications/send` | Send notification | Send a single notification through the appropriate channel (EMAIL, SMS, or AUTO) |
| `POST` | `/api/v1/notifications/send-bulk` | Send bulk notifications | Send multiple notifications in bulk; each is dispatched through its own saga |
| `GET` | `/api/v1/notifications/party/{partyId}` | Get notifications for party | Retrieve paginated notifications for a given party (`page` and `size` query params) |
| `GET` | `/api/v1/notifications/{notificationId}` | Get notification detail | Retrieve details of a specific notification; returns 404 if not found |
| `PATCH` | `/api/v1/notifications/{notificationId}/read` | Mark notification as read | Mark a specific notification as read (`partyId` query param required) |
| `POST` | `/api/v1/notifications/party/{partyId}/read-all` | Mark all notifications as read | Mark all notifications for a party as read |
| `DELETE` | `/api/v1/notifications/{notificationId}` | Delete notification | Delete a specific notification (`partyId` query param required) |

### Notification Preferences Endpoints (`/api/v1/notifications/preferences`)

| Method | Path | Summary | Description |
|--------|------|---------|-------------|
| `GET` | `/api/v1/notifications/preferences/{partyId}` | Get notification preferences | Retrieve notification channel preferences for a party |
| `PUT` | `/api/v1/notifications/preferences/{partyId}` | Update notification preferences | Update notification channel preferences for a party (body: `Map<String, Boolean>`) |

### Saga Workflow: SendNotificationSaga

Each `POST /send` invocation triggers the following DAG-based saga:

```
routeNotification (Layer 0)
    +-- deliverEmail (Layer 1, retry=3, backoff=2000ms, jitter)
    |       +-- auditEmail (Layer 2, dependsOn deliverEmail, retry=2, backoff=500ms)
    +-- deliverSms (Layer 1, retry=3, backoff=2000ms, jitter)
            +-- auditSms (Layer 2, dependsOn deliverSms, retry=2, backoff=500ms)
```

Only the branch matching the resolved channel executes. Compensating actions log warnings for irreversible delivery operations.

---

## SDK

The `domain-common-notifications-sdk` module generates a reactive Java client SDK from the service's OpenAPI specification using `openapi-generator-maven-plugin` (v7.0.1).

### Generated Artifacts

| Package | Contents |
|---------|----------|
| `com.firefly.domain.common.notifications.sdk.api` | `NotificationsApi`, `NotificationPreferencesApi` -- typed API clients |
| `com.firefly.domain.common.notifications.sdk.model` | `SendNotificationCommand`, `SendBulkNotificationCommand`, `NotificationDTO`, `NotificationPreferencesDTO` -- request/response models |
| `com.firefly.domain.common.notifications.sdk.invoker` | `ApiClient` and authentication helpers |

### Usage

Add the SDK dependency to your consumer project:

```xml
<dependency>
    <groupId>com.firefly</groupId>
    <artifactId>domain-common-notifications-sdk</artifactId>
    <version>1.0.0-SNAPSHOT</version>
</dependency>
```

The SDK uses the `webclient` library with reactive support, returning `Mono` and `Flux` types.

### Regenerating the SDK

The SDK is generated during the Maven build from the OpenAPI spec produced by the `-web` module:

```bash
mvn clean install
```

The input spec is read from `domain-common-notifications-web/target/openapi/openapi.yml`.

---

## Testing

### Running Tests

```bash
# Run all tests
mvn test

# Run tests for a specific module
mvn -pl domain-common-notifications-core test
mvn -pl domain-common-notifications-web test
```

### Test Coverage

The project includes unit tests for the following components:

| Module | Test Class | Covers |
|--------|-----------|--------|
| `core` | `SendEmailHandlerTest` | Email delivery handler |
| `core` | `SendSmsHandlerTest` | SMS delivery handler |
| `core` | `AuditEmailHandlerTest` | Email audit recording |
| `core` | `AuditSmsHandlerTest` | SMS audit recording |
| `core` | `MarkNotificationReadHandlerTest` | Single notification read-status |
| `core` | `MarkAllNotificationsReadHandlerTest` | Bulk read-status update |
| `core` | `DeleteNotificationHandlerTest` | Notification deletion |
| `core` | `GetNotificationsByPartyHandlerTest` | Party notification retrieval |
| `core` | `GetNotificationDetailHandlerTest` | Single notification detail |
| `core` | `GetPreferencesHandlerTest` | Preferences retrieval |
| `core` | `UpdatePreferencesHandlerTest` | Preferences update |
| `core` | `NotificationServiceImplTest` | Service orchestration logic |
| `web` | `NotificationControllerTest` | Notification REST endpoints |
| `web` | `PreferencesControllerTest` | Preferences REST endpoints |

Dependencies for testing include `spring-boot-starter-test` and `reactor-test`.

---

## Monitoring

### Actuator Endpoints

The web module includes `spring-boot-starter-actuator` with the following endpoints:

| Endpoint | Purpose |
|----------|---------|
| `/actuator/health` | Application health check with liveness/readiness probes |
| `/actuator/info` | Build information |
| `/actuator/prometheus` | Prometheus-compatible metrics |

### Metrics

Prometheus metrics are exported via `micrometer-registry-prometheus`. CQRS command and query metrics are enabled by default. Scrape the `/actuator/prometheus` endpoint for time-series data.

### API Documentation

Interactive API documentation is available at:

- **Swagger UI**: `/swagger-ui.html`
- **OpenAPI JSON**: `/v3/api-docs`

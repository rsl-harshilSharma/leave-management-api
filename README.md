# Leave Management API

A Spring Boot REST API for creating, retrieving, updating, and deleting employee leave requests. The application uses a controller-service-repository architecture and a thread-safe in-memory repository; data is cleared when the application stops.

## Requirements

- Java 17 or newer
- Maven 3.9 or newer (or use the Maven wrapper if one is added)

## Build and run

```bash
mvn clean test
mvn spring-boot:run
```

The `dev` profile is enabled by default. The API listens on `http://localhost:8080`.

## API

| Method | Path | Description | Success |
|---|---|---|---|
| `GET` | `/leaves` | List all leave requests | `200 OK` |
| `GET` | `/leaves/{id}` | Get one leave request | `200 OK` |
| `POST` | `/leaves` | Create a leave request | `201 Created` |
| `PUT` | `/leaves/{id}` | Replace an existing request's details | `200 OK` |
| `DELETE` | `/leaves/{id}` | Delete a leave request | `204 No Content` |

Create and update requests use the following JSON shape. `leaveType` must be one of `ANNUAL`, `SICK`, `PERSONAL`, or `UNPAID`. New requests start with the server-managed `PENDING` status; updates retain the current status.

```json
{
  "employeeId": "employee-123",
  "leaveType": "ANNUAL",
  "startDate": "2026-12-10",
  "endDate": "2026-12-12",
  "reason": "Family trip"
}
```

Requests are validated for required fields, maximum string lengths, valid enum/date values, and a start date no later than the end date. A missing ID returns `404 Not Found`; invalid or malformed input returns `400 Bad Request` with a structured error response.

## Testing

```bash
mvn test
```

Service unit tests use Mockito. Controller tests exercise the HTTP layer with MockMvc, including successful requests, validation errors, and not-found handling.

## Screenshots for submission

After running the app and tests, save screenshots in `screenshots/` and include:

- `application-startup.png` — terminal showing the Spring Boot application started
- `api-create-leave.png` — successful `POST /leaves` request and response (for example, in Postman)
- `api-list-leaves.png` — successful `GET /leaves` response
- `tests-passing.png` — terminal showing `mvn test` succeeded

Do not include access tokens, private employee data, or other credentials in screenshots.

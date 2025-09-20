# Parking Lot System (Complete Demo)

This repository contains a complete **Parking Lot Management System** built with **Spring Boot**, demonstrating end-to-end parking operations including slot allocation, ticketing, and payment.

---

## Features

* **Google OAuth2 Login** (placeholders provided; can be replaced with real credentials).
* **Roles & Access Control**:

    * `ADMIN`: manage parking lots, slots, and pricing rules.
    * `USER`: park and retrieve vehicles, view tickets and payments.
* **Vehicle Management**:

    * Vehicle entry with **ticket generation**.
    * Vehicle exit with **fee calculation** and **payment processing**.
* **Slot Allocation**:

    * Pessimistic locking to prevent double allocation.
    * Supports multiple vehicle types (`CAR`, `BIKE`, `TRUCK`) across floors.
* **Pricing Rules**:

    * Configurable per vehicle type via admin.
    * Hourly rate calculation applied on exit.
* **DataLoader**:

    * Seeds H2 database with sample lots, slots, vehicles, active tickets, exited tickets, payments, and pricing rules.
* **Service Layer**:

    * Clean separation via **interfaces + implementations** for maintainable design.
* **Postman Collection**:

    * Ready-to-use for demo requests: entry, exit, tickets, payments, admin operations.

---

## Getting Started

1. **Configure Google OAuth2**
   Replace placeholders in `src/main/resources/application.properties`:

   ```properties
   spring.security.oauth2.client.registration.google.client-id=YOUR_GOOGLE_CLIENT_ID
   spring.security.oauth2.client.registration.google.client-secret=YOUR_GOOGLE_CLIENT_SECRET
   ```

2. **Set Admin Emails** (optional)

   ```properties
   app.admin.emails=youremail@example.com
   ```

   Users with these emails get the `ADMIN` role.

3. **Build & Run**

   ```bash
   mvn clean package
   mvn spring-boot:run
   ```

4. **H2 Console**
   Access the in-memory database at: [http://localhost:8080/h2-console](http://localhost:8080/h2-console)

---

## API Endpoints

### **User APIs**

| Method | Endpoint                        | Description                      |
| ------ | ------------------------------- | -------------------------------- |
| POST   | `/api/user/entry`               | Park a vehicle (creates ticket)  |
| POST   | `/api/user/exit/{ticketId}`     | Exit vehicle and process payment |
| GET    | `/api/user/tickets/{plateNo}`   | View active ticket for a vehicle |
| GET    | `/api/user/payments/{ticketId}` | View payment receipt             |

### **Admin APIs**

| Method | Endpoint                        | Description                 |
| ------ | ------------------------------- | --------------------------- |
| POST   | `/api/admin/lots`               | Add a new parking lot       |
| POST   | `/api/admin/lots/{lotId}/slots` | Add a slot in a parking lot |
| DELETE | `/api/admin/slots/{slotId}`     | Remove a parking slot       |
| POST   | `/api/admin/pricing`            | Add or update pricing rule  |
| GET    | `/api/admin/lots`               | List all parking lots       |

> All requests require OAuth2 authentication. Include `Authorization: Bearer <token>` in headers.
> Use `postman_collection.json` for preconfigured examples and demo flow.

---

## Demo Data

* Parking lot: `"Arun Plaza"` with 3 floors.
* Slots per floor:

    * 5 CAR slots
    * 3 BIKE slots
    * 2 TRUCK slots
* Vehicles:

    * `MH12AB1234` (CAR, active ticket)
    * `MH14XY5678` (BIKE, exited ticket + payment)
* Pricing rules: CAR = 10/hr, BIKE = 5/hr, TRUCK = 15/hr

---

## Notes

* **Transactional & Locking**: All slot allocations use pessimistic locks to ensure concurrency safety.
* **Service Layer**: Interfaces + Impl for clean, testable architecture.
* **H2 Database**: Data is loaded on startup via `DataLoader`. Can be replaced with MySQL/PostgreSQL for production.

---

## Postman Demo

* Import `postman_collection.json` for end-to-end testing.
* Supports automatic token injection via pre-request scripts for admin/user flows.

---

**Enjoy parking management with safe, concurrent slot allocation! 🚗🛵🚚**

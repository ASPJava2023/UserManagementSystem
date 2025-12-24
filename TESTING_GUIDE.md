# Spring Boot Backend API - Validation Guide

This guide details how to validate the application using **Postman** or **Swagger UI**.

## 🚀 Prerequisites
1.  **Application Running**: Ensure the app is running on port **8084**.
    ```bash
    mvn spring-boot:run
    ```
2.  **Base URL**: `http://localhost:8084`
3.  **Swagger UI**: [http://localhost:8084/swagger-ui/index.html](http://localhost:8084/swagger-ui/index.html)
4.  **H2 Console**: [http://localhost:8084/h2-console](http://localhost:8084/h2-console)
    *   **JDBC URL**: `jdbc:h2:mem:testdb`
    *   **User**: `sa`
    *   **Password**: `password`

---

## 🧪 Step-by-Step Testing Flow

### 1. Verify Master Data (Locations)
Before registering, we need valid Country, State, and City IDs.

**Endpoints:**
*   `GET /api/locations/countries`
*   `GET /api/locations/states/{countryId}`
*   `GET /api/locations/cities/{stateId}`

**Test:**
Fetch all countries to get an ID.
```http
GET http://localhost:8084/api/locations/countries
```
*Note the `id` of "India" (likely `1`).*

---

### 2. User Registration (Sign Up)
Register a new user to receive a temporary password.

**Endpoint:** `POST /api/auth/signup`

**Payload:**
```json
{
  "name": "John Doe",
  "email": "john.doe@example.com",
  "phoneNumber": "9876543210",
  "countryId": 1,
  "stateId": 1,
  "cityId": 1
}
```

**Expected Result:**
*   **Status**: `201 Created`
*   **Body**: `{"success": true, "message": "User registered successfully...", ...}`

**🔑 CRITICAL STEP (Retrieve Password):**
Since we are testing locally without a real email server, check the **Application Console Logs**.
Look for a line like:
> `INFO ... EmailServiceImpl : Email Body: Hello John Doe... temporary password: <PASSWORD>`
>
> *Copy this password!*

---

### 3. First Login (Temporary Password)
Attempt to login with the temporary password.

**Endpoint:** `POST /api/auth/login`

**Payload:**
```json
{
  "email": "john.doe@example.com",
  "password": "<PASTE_PASSWORD_FROM_LOGS>"
}
```

**Expected Result:**
*   **Status**: `200 OK`
*   **Body**:
    ```json
    {
      "success": true,
      "data": {
        "isAuthenticated": true,
        "isFirstLogin": true,
        "message": "Password reset required"
      }
    }
    ```

---

### 4. Reset Password
Force the user to change their password.

**Endpoint:** `POST /api/auth/reset-password`

**Payload:**
```json
{
  "email": "john.doe@example.com",
  "oldPassword": "<PASTE_PASSWORD_FROM_LOGS>",
  "newPassword": "NewSecurePassword123",
  "confirmPassword": "NewSecurePassword123"
}
```

**Expected Result:**
*   **Status**: `200 OK`
*   **Body**: `{"success": true, "message": "Password reset successfully...", ...}`

---

### 5. Login with New Password
Login again to access the dashboard.

**Endpoint:** `POST /api/auth/login`

**Payload:**
```json
{
  "email": "john.doe@example.com",
  "password": "NewSecurePassword123"
}
```

**Expected Result:**
*   **Status**: `200 OK`
*   **Body**:
    ```json
    {
      "success": true,
      "data": {
        "isFirstLogin": false,
        "message": "Login Successful"
      }
    }
    ```

---

### 6. Access Dashboard
Fetch the dashboard data including the random quote.

**Endpoint:** `GET /api/dashboard?email=john.doe@example.com`

**Expected Result:**
*   **Status**: `200 OK`
*   **Body**:
    ```json
    {
      "success": true,
      "data": {
        "userName": "John Doe",
        "quote": "<Random Quote from API>",
        "logoutFlag": false
      }
    }
    ```

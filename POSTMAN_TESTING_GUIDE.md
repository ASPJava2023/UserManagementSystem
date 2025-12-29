# Postman Testing Guide

## Prerequisite
- Helper Application (Backend) running on **port 8086**.
- H2 Database must be running (in-memory).
- All requests should use `Content-Type: application/json`.

## 1. Signup (Registration)
*Create a new user. The system will auto-generate a password and log it (for now) or pretend to email it.*

**Method:** `POST`
**URL:** `http://localhost:8086/api/auth/signup`

**Body (JSON):**
```json
{
  "name": "John Doe",
  "email": "john.doe@example.com",
  "phoneNumber": "1234567890",
  "countryId": 1,
  "stateId": 1,
  "cityId": 1
}
```

**Expected Response (201 Created):**
```json
{
  "success": true,
  "message": "User registered successfully, check email for password.",
  "data": true
}
```

---

## 2. Login (First Time)
*Login using the email and the generated password (check console logs or email debug).*

**Method:** `POST`
**URL:** `http://localhost:8086/api/auth/login`

**Body (JSON):**
```json
{
  "email": "john.doe@example.com",
  "password": "<GENERATED_PASSWORD_FROM_LOGS>"
}
```

**Expected Response (200 OK):**
```json
{
  "success": true,
  "message": "Password reset required",
  "data": {
    "message": "Password reset required",
    "isAuthenticated": true,
    "passwordResetRequired": true,
    "userId": 1,
    "name": "John Doe",
    "token": "<JWT_TOKEN>"
  }
}
```
*Note: Copy the `token` for the next steps.*

---

## 3. Reset Password
*User must reset their password on first login.*

**Method:** `POST`
**URL:** `http://localhost:8086/api/auth/reset-password`
**Headers:**
- `Authorization`: `Bearer <JWT_TOKEN>` (Actually this endpoint is public normally in this flow, but valid user check happens inside. Wait, `resetPassword` implementation does not check token in `AuthController`, it blindly takes email. But typically we might want it secured. In current `SecurityConfig`, `/api/auth/**` is `permitAll`. So no token needed for this specific implementation, but safer if we did.)

**Body (JSON):**
```json
{
  "email": "john.doe@example.com",
  "oldPassword": "<GENERATED_PASSWORD_FROM_LOGS>",
  "newPassword": "NewPassword123",
  "confirmPassword": "NewPassword123"
}
```

**Expected Response (200 OK):**
```json
{
  "success": true,
  "message": "Password reset successfully. Please login.",
  "data": true
}
```

---

## 4. Login (After Reset)
*Login with the new password.*

**Method:** `POST`
**URL:** `http://localhost:8086/api/auth/login`

**Body (JSON):**
```json
{
  "email": "john.doe@example.com",
  "password": "NewPassword123"
}
```

**Expected Response (200 OK):**
```json
{
  "success": true,
  "message": "Login Successful",
  "data": {
    "message": "Login Successful",
    "isAuthenticated": true,
    "passwordResetRequired": false,
    "userId": 1,
    "name": "John Doe",
    "token": "<NEW_JWT_TOKEN>"
  }
}
```

---

## 5. Get Dashboard (Secured)
*Access the dashboard data using the new token.*

**Method:** `GET`
**URL:** `http://localhost:8086/api/dashboard`
**Headers:**
- `Authorization`: `Bearer <NEW_JWT_TOKEN>`

**Expected Response (200 OK):**
```json
{
  "success": true,
  "message": "Dashboard data fetched",
  "data": {
    "userName": "John Doe",
    "quote": "Success is not final, failure is not fatal: it is the courage to continue that counts. - Winston Churchill",
    "logoutFlag": false
  }
}
```

---

## 6. Get Quote (Secured)
*Get just the quote.*

**Method:** `GET`
**URL:** `http://localhost:8086/api/dashboard/quote`
**Headers:**
- `Authorization`: `Bearer <NEW_JWT_TOKEN>`

**Expected Response (200 OK):**
```json
{
  "success": true,
  "message": "Quote fetched",
  "data": {
    "content": "Believe in yourself!",
    "author": "Unknown"
  }
}
```

# Testing Guide

This guide details how to validate the User Management System (Backend & Frontend).

## 🚀 Prerequisites

### Backend
*   **Port**: 8086 (`server.port=8086`)
*   **Database**: H2 In-Memory (`jdbc:h2:mem:testdb`)
*   **Credentials**: `sa` / `password`
*   **Console**: [http://localhost:8086/h2-console](http://localhost:8086/h2-console)
*   **Swagger UI**: [http://localhost:8086/swagger-ui/index.html](http://localhost:8086/swagger-ui/index.html)

### Frontend
*   **Port**: 5173 (Default Vite port)
*   **URL**: [http://localhost:5173](http://localhost:5173)

---

## 🧪 Postman API Testing Flow

Use the following sequence to test the backend APIs directly using Postman.
**Headers**: All requests should generally use `Content-Type: application/json`.

### 1. Master Data (Public)
Fetch locations for dropdowns.
*   **GET** `http://localhost:8086/api/locations/countries`
*   **GET** `http://localhost:8086/api/locations/states/{countryId}`
*   **GET** `http://localhost:8086/api/locations/cities/{stateId}`

### 2. Signup (Registration)
Register a new user. The system generates a random password.
*   **Method**: `POST`
*   **URL**: `http://localhost:8086/api/auth/signup`
*   **Body**:
    ```json
    {
      "name": "Jane Doe",
      "email": "jane.doe@example.com",
      "phoneNumber": "9876543210",
      "countryId": 1,
      "stateId": 1,
      "cityId": 1
    }
    ```
*   **Verification**: Check backend console logs for the "FALLBACK EMAIL LOGGER" to see the generated password.

### 3. Login (First Time)
Login with the temporary password found in the logs.
*   **Method**: `POST`
*   **URL**: `http://localhost:8086/api/auth/login`
*   **Body**:
    ```json
    {
      "email": "jane.doe@example.com",
      "password": "<PASTE_PASSWORD_FROM_LOGS>"
    }
    ```
*   **Response**: Returns a JWT token and `passwordResetRequired: true`.

### 4. Reset Password
Mandatory password change flow.
*   **Method**: `POST`
*   **URL**: `http://localhost:8086/api/auth/reset-password`
*   **Body**:
    ```json
    {
      "email": "jane.doe@example.com",
      "oldPassword": "<PASTE_PASSWORD_FROM_LOGS>",
      "newPassword": "MyNewPassword123",
      "confirmPassword": "MyNewPassword123"
    }
    ```

### 5. Login (Subsequent)
Login with the new password.
*   **Method**: `POST`
*   **URL**: `http://localhost:8086/api/auth/login`
*   **Body**:
    ```json
    {
      "email": "jane.doe@example.com",
      "password": "MyNewPassword123"
    }
    ```
*   **Response**: Returns a new JWT token and `passwordResetRequired: false`.

### 6. Dashboard (Secured)
Access protected resources.
*   **Method**: `GET`
*   **URL**: `http://localhost:8086/api/dashboard`
*   **Headers**: `Authorization: Bearer <YOUR_JWT_TOKEN>`

### 7. Admin Login
Login as the pre-seeded admin user.
*   **Method**: `POST`
*   **URL**: `http://localhost:8086/api/auth/login`
*   **Body**:
    ```json
    {
      "email": "admin@example.com",
      "password": "password"
    }
    ```
*   **Response**: Returns JWT token, `role: "ROLE_ADMIN"`.

### 8. Admin User Management (Secured)
All requests require header: `Authorization: Bearer <ADMIN_JWT_TOKEN>`

#### A. List Users (with Pagination & Search)
*   **Method**: `GET`
*   **URL**: `http://localhost:8086/api/admin/users?page=0&size=5&search=jane`

#### B. Create User (Admin)
*   **Method**: `POST`
*   **URL**: `http://localhost:8086/api/admin/users`
*   **Body**:
    ```json
    {
      "name": "New Admin User",
      "email": "new.admin@example.com",
      "phoneNumber": "1234567890",
      "countryId": 1,
      "stateId": 1,
      "cityId": 1,
      "role": "ADMIN"
    }
    ```

#### C. Update User
*   **Method**: `PUT`
*   **URL**: `http://localhost:8086/api/admin/users/{id}`
*   **Body**:
    ```json
    {
      "name": "Updated Name",
      "email": "updated@example.com",
      "phoneNumber": "1111111111",
      "countryId": 1,
      "stateId": 1,
      "cityId": 1,
      "role": "USER"
    }
    ```

#### D. Delete User
*   **Method**: `DELETE`
*   **URL**: `http://localhost:8086/api/admin/users/{id}`

### 9. Access Control Test
Try to access Admin APIs with a Normal User Token.
*   **Method**: `GET`
*   **URL**: `http://localhost:8086/api/admin/users`
*   **Header**: `Authorization: Bearer <USER_JWT_TOKEN>`
*   **Expected Response**: `403 Forbidden`

---

## 🖥️ Frontend Testing Flow

1.  Open **[http://localhost:5173](http://localhost:5173)**.
2.  **Signup**: Fill the form. Dropdowns for Country/State/City should work. Submit.
3.  **Check Logs**: Go to the backend terminal to get the temporary password.
4.  **Login**: Use email and temporary password.
5.  **Reset Password**: You should be automatically redirected to the Reset Password screen. Enter details.
6.  **Login Again**: You will be redirected to Login. Use the new password.
7.  **Dashboard**: You should see the Welcome message and the "Quote of the Day".

### 8. Admin Flows
1.  **Admin Login**: Login with `admin@example.com` / `password`.
2.  **Redirect**: You should be redirected to the Admin Dashboard.
3.  **User Management**: Navigate to "User Management".
4.  **List**: You should see a list of users.
5.  **Search**: Try searching for a user.
6.  **Create**: Click "Add User", fill details, select Role (ADMIN/USER), and submit.
7.  **Edit**: Edit an existing user.
8.  **Delete**: Delete a user.


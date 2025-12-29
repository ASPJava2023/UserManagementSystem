# User Management System (Mini Project)

## 📖 Project Overview
This project is a comprehensive **User Management System** built with **Spring Boot** (Backend) and **React + Vite** (Frontend). It demonstrates a complete user lifecycle including registration with dynamic master data, secure authentication using **JWT**, role-based access flows, and interactive dashboards.

The application mimics real-world enterprise scenarios such as forced password rotation on first login, stateful-to-stateless authentication transitions, and third-party API integrations with fallback mechanisms.

---

## ✨ Key Features

### Backend (Spring Boot)
*   **Secure Authentication**: Implemented using Spring Security and **JWT (JSON Web Tokens)**.
*   **Dynamic Registration**: Hierarchy-based dropdowns (Country -> State -> City) powered by database master data.
*   **Password Management**:
    *   System-generated temporary passwords sent via email (simulated in logs).
    *   **Forced Password Reset** on first login (`passwordResetRequired` flag).
*   **Resilient Dashboard**: Fetches daily quotes from an external API (`Quotable.io`) with a graceful **fallback mechanism** (custom Hindi quote) if the service is down.
*   **Tech Stack**:
    *   Java 17
    *   Spring Boot 3.x
    *   Spring Security & JWT
    *   Spring Data JPA & H2 Database
    *   OpenFeign (External API Client)
    *   Lombok & Swagger/OpenAPI

### Frontend (React + Vite)
*   **Modern UI**: Clean, responsive interface built with **Bootstrap 5**.
*   **Smart Routing**: Protected routes using `react-router-dom`.
*   **State Management**: efficient handling of auth state and tokens using `localStorage`.
*   **Interceptor**: Axios interceptors to automatically attach JWT tokens to logout/dashboard requests.
*   **Components**:
    *   **Signup**: Dynamic form handling.
    *   **Login**: Conditional redirects based on reset status.
    *   **Dashboard**: Displays personalized content and quotes.

---

## 🛠️ How to Run

### 1. Backend Setup
1.  Navigate to the project root.
2.  Ensure you have **Java 17** and **Maven** installed.
3.  Run the application:
    ```bash
    mvn spring-boot:run
    ```
4.  The server will start on **port 8086**.
    *   **Swagger API Docs**: [http://localhost:8086/swagger-ui/index.html](http://localhost:8086/swagger-ui/index.html)
    *   **H2 Console**: [http://localhost:8086/h2-console](http://localhost:8086/h2-console)

### 2. Frontend Setup
1.  Navigate to the `frontend` folder:
    ```bash
    cd frontend
    ```
2.  Install dependencies:
    ```bash
    npm install
    ```
3.  Start the dev server:
    ```bash
    npm run dev
    ```
4.  Open [http://localhost:5173](http://localhost:5173) in your browser.

---

## 🧪 Testing
For detailed testing instructions (both Manual and Postman), please refer to **[Testing_Guide.md](Testing_Guide.md)**.

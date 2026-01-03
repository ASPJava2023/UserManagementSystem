# Stage 1: Build Backend
FROM maven:3.9-eclipse-temurin-17 AS backend-build
WORKDIR /backend
COPY backend/pom.xml .
COPY backend/src ./src
RUN mvn clean package -DskipTests

# Stage 2: Build Frontend
FROM node:18-alpine AS frontend-build
WORKDIR /frontend
COPY frontend/package*.json ./
RUN npm ci
COPY frontend/ .
# In single container, backend is at /api relative to root, no full URL needed if Nginx handles it
ARG VITE_API_BASE_URL=/api
ENV VITE_API_BASE_URL=$VITE_API_BASE_URL
RUN npm run build

# Stage 3: Final Combined Image
FROM eclipse-temurin:17-jre-alpine

# Install Nginx and Supervisor
RUN apk add --no-cache nginx supervisor

# Create necessary directories for Nginx
RUN mkdir -p /run/nginx

# Configure Nginx
COPY configs/nginx-single.conf /etc/nginx/http.d/default.conf
# Remove default nginx config if it exists
RUN rm -f /etc/nginx/http.d/default.conf.bak

# Copy Frontend Build
COPY --from=frontend-build /frontend/dist /usr/share/nginx/html

# Copy Backend JAR
WORKDIR /app
COPY --from=backend-build /backend/target/*.jar app.jar

# Configure Supervisor
COPY configs/supervisord.conf /etc/supervisord.conf

# Expose HTTP port
EXPOSE 80

# Run Supervisor
CMD ["/usr/bin/supervisord", "-c", "/etc/supervisord.conf"]

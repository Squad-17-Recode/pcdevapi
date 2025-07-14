# Use Eclipse Temurin JDK with Alpine (Java 24 - early access)
FROM eclipse-temurin:24-jdk-alpine

# Set working directory
WORKDIR /app

# Install Maven and curl
RUN apk add --no-cache maven curl

# Copy Maven wrapper and pom.xml first (for better caching)
COPY mvnw mvnw.cmd pom.xml ./
COPY .mvn .mvn

# Make Maven wrapper executable
RUN chmod +x mvnw

# Download dependencies (this layer will be cached if pom.xml doesn't change)
RUN ./mvnw dependency:go-offline -B

# Copy source code
COPY src ./src

# Build the application
RUN ./mvnw clean package -DskipTests

# Create final image
FROM eclipse-temurin:24-jre-alpine

# Set working directory
WORKDIR /app

# Copy built JAR from previous stage
COPY --from=0 /app/target/*.jar app.jar

# Install curl for health checks
RUN apk add --no-cache curl

# Create non-root user for security
RUN addgroup -g 1001 -S appuser && \
    adduser -S appuser -u 1001 -G appuser
RUN chown -R appuser:appuser /app
USER appuser

# Expose port
EXPOSE 8080

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=60s --retries=3 \
    CMD curl -f http://localhost:8080/actuator/health || exit 1

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]

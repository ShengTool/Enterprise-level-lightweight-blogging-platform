# LiteBlog Backend Dockerfile
FROM eclipse-temurin:17.0.11_9-jdk-alpine AS builder

WORKDIR /app

# Copy maven files
COPY pom.xml .

# Download dependencies first to leverage caching
RUN apk add --no-cache maven && \
    mvn dependency:go-offline -B

# Copy source code
COPY src ./src

# Build the application
RUN mvn package -DskipTests -B

# Runtime stage
FROM eclipse-temurin:17.0.11_9-jre-alpine

WORKDIR /app

# Create upload directory and set permissions
RUN mkdir -p /app/uploads && \
    chmod 755 /app/uploads

# Copy the built jar
COPY --from=builder /app/target/blog-backend-1.0-SNAPSHOT.jar app.jar

# Copy entrypoint script
COPY docker-entrypoint.sh /app/docker-entrypoint.sh
RUN chmod +x /app/docker-entrypoint.sh

# Expose port
EXPOSE 8080

# Health check
HEALTHCHECK --interval=30s --timeout=10s --start-period=30s --retries=3 \
    CMD wget -qO- http://localhost:8080/actuator/health || exit 1

ENTRYPOINT ["/app/docker-entrypoint.sh"]
CMD ["java", "-jar", "app.jar"]

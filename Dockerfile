# === STAGE 1: Build the application ===
FROM maven:3.9.6-eclipse-temurin-21-jammy AS builder
WORKDIR /app

# Copy pom.xml and download dependencies to leverage Docker caching
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy source code and build the production JAR
COPY src ./src
RUN mvn clean package -DskipTests

# === STAGE 2: Create the final lightweight runtime container ===
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app

# Copy ONLY the compiled jar file from the builder stage
COPY --from=builder /app/target/transactguard-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080
ENV SPRING_PROFILES_ACTIVE=prod

# Run the lightweight jar
CMD ["java", "-jar", "app.jar"]
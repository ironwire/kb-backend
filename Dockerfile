FROM openjdk:17-jdk-slim AS build
WORKDIR /workspace/app

# Copy maven wrapper and pom.xml
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .

# Make the mvnw script executable
RUN chmod +x ./mvnw

# Download dependencies
RUN ./mvnw dependency:go-offline -B

# Copy source code
COPY src src

# Build the application
RUN ./mvnw package -DskipTests
RUN mkdir -p target/dependency && (cd target/dependency; jar -xf ../*.jar)

# Production stage
FROM openjdk:17-slim
VOLUME /tmp

# Copy the built application
ARG DEPENDENCY=/workspace/app/target/dependency
COPY --from=build ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY --from=build ${DEPENDENCY}/META-INF /app/META-INF
COPY --from=build ${DEPENDENCY}/BOOT-INF/classes /app

# Create upload directory
RUN mkdir -p /app/uploads && chmod 777 /app/uploads

# Set Spring profile to prod
ENV SPRING_PROFILES_ACTIVE=prod

# Run the application
ENTRYPOINT ["java","-cp","app:app/lib/*","org.yiqixue.kbbackend.KbBackendApplication"]
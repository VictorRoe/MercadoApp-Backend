# Etapa 1: Build
FROM eclipse-temurin:21-jdk-alpine AS build
WORKDIR /app

# Copiar archivos de configuración de Gradle
COPY gradlew .
COPY gradle gradle
COPY build.gradle.kts .
COPY settings.gradle.kts .

RUN chmod +x gradlew

# Descargar dependencias (esto se cachea si no cambian los archivos anteriores)
RUN ./gradlew dependencies --no-daemon

# Copiar el código fuente y construir el jar
COPY src src
RUN ./gradlew bootJar -x test --no-daemon

# Etapa 2: Runtime (Imagen final ligera)
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Crear un usuario sin privilegios para mayor seguridad
RUN addgroup -S spring && adduser -S spring -G spring
USER spring

# Copiar el JAR desde la etapa de build
COPY --from=build /app/build/libs/*.jar app.jar

# Optimización de JVM para entornos con poca RAM (Render Free)
# - MaxRAMPercentage: Evita que la JVM intente usar más RAM de la disponible en el contenedor
# - Xss256k: Reduce el tamaño del stack de cada hilo para ahorrar memoria
ENTRYPOINT ["java", \
            "-XX:MaxRAMPercentage=75.0", \
            "-Xss256k", \
            "-XX:+UseSerialGC", \
            "-jar", \
            "app.jar"]
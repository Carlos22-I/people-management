# Etapa de construcción (Build)
FROM maven:3.8.5-openjdk-17 AS build
WORKDIR /app
COPY . .
RUN chmod +x mvnw && ./mvnw clean package -DskipTests

# Etapa de ejecución (Runtime) - Usamos Eclipse Temurin en lugar de OpenJDK
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
# Copiamos el jar generado en la etapa anterior
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]

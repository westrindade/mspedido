FROM maven:3.8.4 AS builder

WORKDIR /app

COPY pom.xml .
COPY src ./src
COPY libs ./libs

RUN mvn clean package -DskipTests

FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

 # Copie o arquivo JAR do seu projeto para dentro do contêiner
COPY --from=builder /app/target/*.jar /app/mspedidos.jar

# Comando para executar o projeto quando o contêiner for iniciado
CMD ["java", "-jar", "/app/mspedidos.jar"]
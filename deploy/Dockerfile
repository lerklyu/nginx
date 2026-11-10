# Стадия 1. Сборка приложения
FROM maven:3.9-eclipse-temurin-21 AS builder

WORKDIR /app

# Сначала копируем файлы Maven
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .

# Делаем Maven Wrapper исполняемым
RUN chmod +x mvnw

# Загружаем зависимости
RUN ./mvnw -B dependency:go-offline

# Копируем исходный код
COPY src ./src

# Собираем приложение
RUN ./mvnw -B package

# Стадия 2. Запуск приложения
FROM eclipse-temurin:21-jre

WORKDIR /app

# Устанавливаем curl для проверки здоровья приложения
RUN apt-get update \
    && apt-get install -y --no-install-recommends curl \
    && rm -rf /var/lib/apt/lists/*

# Копируем собранный JAR из первой стадии
COPY --from=builder /app/target/amlicode-*.jar app.jar

EXPOSE 8080

# Проверяем доступность приложения
HEALTHCHECK --interval=30s --timeout=5s --start-period=60s --retries=3 \
    CMD curl -fsS http://localhost:8080/actuator/health || exit 1

ENTRYPOINT ["java", "-jar", "app.jar"]

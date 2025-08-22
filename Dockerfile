FROM ghcr.io/graalvm/jdk-community:21 AS builder

WORKDIR /app

# Copia pom.xml e baixa dependências (caching)
COPY pom.xml .
RUN microdnf install -y maven \
  && mvn -B dependency:resolve dependency:resolve-plugins

# Copia o código-fonte
COPY src ./src

# Compila o jar tradicional (não nativo)
RUN mvn package -DskipTests

# --- Imagem final leve ---
FROM ghcr.io/graalvm/jdk-community:21 AS runtime

WORKDIR /app

# Copia o jar gerado
COPY --from=builder /app/target/java-elite-proj-1.0.jar .

EXPOSE 8080

CMD ["java", "-jar", "java-elite-proj-1.0.jar"]
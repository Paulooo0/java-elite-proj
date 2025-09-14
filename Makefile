-include .env
export

run:
	docker compose up

run-app:
	export DB_HOST
	export DB_USER
	export DB_PASSWORD
	export DB_NAME
	export DB_PORT
	export DB_IMAGE
	export APP_PORT

	./mvnw clean package -DskipTests
	./mvnw spring-boot:run

run-db:
	docker compose up db pgadmin -d

stop:
	docker compose stop

down:
	docker compose down
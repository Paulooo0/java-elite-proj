-include .env
export

run:
	docker compose up -d

	export DB_HOST
	export DB_USER
	export DB_PASSWORD
	export DB_NAME
	export DB_PORT
	export DB_IMAGE
	export APP_PORT

	./mvnw spring-boot:run

run-app:
	export DB_HOST
	export DB_USER
	export DB_PASSWORD
	export DB_NAME
	export DB_PORT
	export DB_IMAGE
	export APP_PORT

	./mvnw spring-boot:run

stop-db:
	docker compose stop

down-db:
	docker compose down

up-db:
	docker compose up -d
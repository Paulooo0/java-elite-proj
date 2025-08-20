native-build-dev:
	./mvnw spring-boot:build-image -Pnative -DspringAot.native-image.args="-J-XX:ActiveProcessorCount=4 -J-Xmx2g"

run:
	./mvnw spring-boot:run
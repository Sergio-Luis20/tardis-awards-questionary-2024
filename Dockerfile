FROM openjdk:21-jdk-slim AS build
COPY . .

RUN --mount=type=secret,id=private_base64,dst=/etc/secrets/private_base64 \
    base64 -d private_base64 > /src/main/resources/private.key
RUN --mount=type=secret,id=public_base64,dst=/etc/secrets/public_base64 \
    base64 -d public_base64 > /src/main/resources/public.key \
RUN --mount=type=secret,id=simple-questions,dst=/etc/secrets/simple-questions \
    base64 -d simple-questions > /src/main/resources/simple-questions.csv

RUN chmod +x ./mvnw
RUN ./mvnw clean package -q -DskipTests

FROM openjdk:21-jdk-slim

EXPOSE 8080
COPY --from=build /target/tardis-awards-questionary.jar tardis-awards-questionary.jar
ENTRYPOINT ["java", "-jar", "tardis-awards-questionary.jar"]
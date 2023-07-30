FROM eclipse-temurin:17.0.7_7-jre-jammy

COPY ./app.jar .

EXPOSE 8080

ENTRYPOINT [ "java", "-jar", "./app.jar" ]
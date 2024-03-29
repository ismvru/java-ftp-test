FROM openjdk:18-slim
COPY target/ftptest*-jar-with-dependencies.jar /app.jar
ENTRYPOINT [ "java", "-jar", "/app.jar" ]
CMD ["-h"]
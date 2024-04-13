FROM azul/zulu-openjdk-alpine:21-latest
MAINTAINER Bill Yu
LABEL authors="Bill Yu"
LABEL version="1.0.0"

ENV TZ=Asia/Shanghai

WORKDIR /
COPY target/geekoj-backend-question-service-1.0.0.jar app.jar
EXPOSE 8102

ENTRYPOINT ["java", "-server", "-jar", "app.jar"]